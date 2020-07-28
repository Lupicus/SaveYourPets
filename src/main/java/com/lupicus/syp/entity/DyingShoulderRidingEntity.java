package com.lupicus.syp.entity;

import com.lupicus.syp.Main;
import com.lupicus.syp.advancements.ModTriggers;
import com.lupicus.syp.config.MyConfig;
import com.lupicus.syp.item.ModItems;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.ShoulderRidingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class DyingShoulderRidingEntity extends ShoulderRidingEntity implements IDying
{
	protected long woundedTime;
	double dx, ay, dz;

	protected DyingShoulderRidingEntity(EntityType<? extends ShoulderRidingEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("WoundedTime"))
		{
			woundedTime = compound.getLong("WoundedTime");
			dataManager.set(POSE, Pose.DYING);
			setHealth(0.0F);
			deathTime = 20;
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (isDying())
			compound.putLong("WoundedTime", woundedTime);
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		if (!isTamed())
		{
			super.onDeath(cause);
			return;
		}
		if (!isDying())
		{
			if (!world.isRemote && world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)
					&& getOwner() instanceof ServerPlayerEntity) {
				Class<? extends DyingShoulderRidingEntity> clazz = this.getClass();
				String type = clazz.getSimpleName().replace("Entity", "").toLowerCase();
				TextComponent msg = new TranslationTextComponent(Main.MODID + ".pet_dying." + type);
				if (hasCustomName())
					msg.func_230529_a_(new StringTextComponent(" ")).func_230529_a_(getCustomName());
				if (MyConfig.showLoc)
					msg.func_230529_a_(new StringTextComponent(" " + formatLoc(getPositionVec())));
				LivingEntity player = getOwner();
				player.sendMessage(msg, player.getUniqueID());
			}
			setMotion(Vector3d.ZERO);
			this.dataManager.set(POSE, Pose.DYING);
			woundedTime = world.getGameTime();
			rotationYaw = renderYawOffset;
			rotationYawHead = renderYawOffset;
			rotationPitch = 0.0F;
		}
	}

	@Override
	protected void onDeathUpdate()
	{
		if (!isTamed())
		{
			super.onDeathUpdate();
			return;
		}
		if (deathTime < 20)
		{
			deathTime++;
			if (deathTime == 10)
				modifyBoundingBox();
		}
		else if (!world.isRemote && world.getGameTime() - woundedTime >= MyConfig.deathTimer)
		{
			if (MyConfig.autoHeal)
			{
				cureEntity(ModItems.GOLDEN_PET_BANDAGE);
			}
			else
			{
				super.onDeath(DamageSource.GENERIC);
				remove();
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id)
	{
		if (id == 101)
			cureEntity(null);
		else {
			super.handleStatusUpdate(id);
			if (id == 3 && isTamed())
			{
				deathTime = 19;
				super.onDeathUpdate();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isDying()
	{
		return dataManager.get(POSE) == Pose.DYING && isTamed() && !removed;
	}

	@Override
	public ActionResultType dyingInteract(PlayerEntity player, Hand hand)
	{
		if (!isDying())
			return ActionResultType.PASS;
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		if (item == ModItems.PET_BANDAGE || item == ModItems.GOLDEN_PET_BANDAGE)
		{
            if (!player.abilities.isCreativeMode) {
            	itemstack.shrink(1);
            }
			if (player instanceof ServerPlayerEntity) {
				ModTriggers.SAVE_PET.trigger((ServerPlayerEntity) player, this);
				cureEntity(item);
			}
			return ActionResultType.func_233537_a_(world.isRemote);
		}
		return ActionResultType.PASS;
	}

	void cureEntity(Item item)
	{
		if (world.isRemote)
		{
			for (int i = 0; i < 10; ++i) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom(),
						this.getPosZRandom(1.0D), d0, d1, d2);
			}
		}
		else
			world.setEntityState(this, (byte) 101);
		setMotion(Vector3d.ZERO);
		this.dataManager.set(POSE, Pose.STANDING);
		setHealth(1.0F);
		deathTime = 0;
		recenterBoundingBox();
		if (item == ModItems.GOLDEN_PET_BANDAGE)
			addPotionEffect(new EffectInstance(Effects.REGENERATION, MyConfig.healTime, 1));
	}

	@Override
	public void setBoundingBox(AxisAlignedBB bb)
	{
		if (deathTime >= 10)
		{
			AxisAlignedBB oldbb = getBoundingBox();
			dx += bb.minX - oldbb.minX;
			ay = bb.minY;
			dz += bb.minZ - oldbb.minZ;
		}
		super.setBoundingBox(bb);
	}

	@Override
	public void setPosition(double x, double y, double z)
	{
		super.setPosition(x, y, z);
		if (deathTime >= 10)
			modifyBoundingBox();
	}

	@Override
	public void resetPositionToBB()
	{
		if (deathTime >= 10)
		{
			setRawPosition(getPosX() + dx, ay, getPosZ() + dz);
			dx = 0;
			dz = 0;
		    if (isAddedToWorld() && !world.isRemote && world instanceof ServerWorld)
		    	((ServerWorld) world).chunkCheck(this); // Forge - Process chunk registration after moving.
			return;
		}
		super.resetPositionToBB();
	}

	private void modifyBoundingBox()
	{
		double ang = (double) rotationYaw * (Math.PI / 180.0);
		EntitySize size = getSize(Pose.STANDING);
		double width = size.width / 2.0;
		double length = size.height / 2.0;
		double lcos = length * Math.cos(ang);
		double lsin = length * Math.sin(ang);
		double x0 = getPosX();
		double x1 = x0 + lcos;
		double y0 = getPosY();
		double z0 = getPosZ();
		double z1 = z0 + lsin;
		double hw = (width + length) / 2.0;
		double xmin = x1 - hw;
		double xmax = x1 + hw;
		if (x0 < xmin)
			xmin = x0;
		else if (x0 > xmax)
			xmax = x0;
		double zmin = z1 - hw;
		double zmax = z1 + hw;
		if (z0 < zmin)
			zmin = z0;
		else if (z0 > zmax)
			zmax = z0;
		super.setBoundingBox(new AxisAlignedBB(xmin, y0, zmin, xmax, y0 + width, zmax));
		dx = 0;
		ay = y0;
		dz = 0;
	}
}
