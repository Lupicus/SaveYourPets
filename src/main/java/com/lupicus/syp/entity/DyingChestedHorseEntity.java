package com.lupicus.syp.entity;

import java.util.UUID;

import com.lupicus.syp.Main;
import com.lupicus.syp.advancements.ModTriggers;
import com.lupicus.syp.config.MyConfig;
import com.lupicus.syp.item.ModItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class DyingChestedHorseEntity extends AbstractChestedHorse implements IDying
{
	protected long woundedTime;
	protected int woundedTicks;

	protected DyingChestedHorseEntity(EntityType<? extends AbstractChestedHorse> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("WoundedTime"))
		{
			woundedTime = compound.getLong("WoundedTime");
			woundedTicks = tickCount;
			if (compound.contains("WoundedTicks"))
				woundedTicks -= compound.getInt("WoundedTicks");
			else
				woundedTicks -= (int) (level.getGameTime() - woundedTime);
			entityData.set(DATA_POSE, Pose.DYING);
			setHealth(0.0F);
			deathTime = 20;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) { // write
		super.addAdditionalSaveData(compound);
		if (isDying())
		{
			compound.putLong("WoundedTime", woundedTime);
			compound.putInt("WoundedTicks", tickCount - woundedTicks);
		}
	}

	@Override
	public void die(DamageSource cause)
	{
		if (!isTamed())
		{
			super.die(cause);
			return;
		}
		if (!isDying())
		{
			Player player = null;
			UUID uuid = getOwnerUUID();
			if (uuid != null)
				player = level.getPlayerByUUID(uuid);
			if (player instanceof ServerPlayer && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES))
			{
				ResourceLocation res = getType().getRegistryName();
				String type;
				if (res.getNamespace().equals("minecraft"))
				{
					type = res.getPath();
					if (type.equals("trader_llama"))
						type = "llama";
				}
				else
				{
					type = "generic";
				}
				MutableComponent msg = new TranslatableComponent(Main.MODID + ".pet_dying." + type);
				if (hasCustomName())
					msg.append(new TextComponent(" ")).append(getCustomName());
				if (MyConfig.showLoc)
					msg.append(new TextComponent(" " + formatLoc(position())));
				player.sendMessage(msg, player.getUUID());
			}
			unRide();
			this.entityData.set(DATA_POSE, Pose.DYING);
			woundedTime = level.getGameTime();
			woundedTicks = tickCount;
			setYRot(yBodyRot);
			yHeadRot = yBodyRot;
			setXRot(0.0F);
		}
	}

	@Override
	protected void tickDeath()
	{
		if (!isTamed())
		{
			super.tickDeath();
			return;
		}
		if (deathTime < 20)
		{
			deathTime++;
			if (deathTime == 10)
				reapplyPosition();
		}
		else if (!level.isClientSide)
		{
		    int time = (MyConfig.useWorldTicks) ? (int) (level.getGameTime() - woundedTime) : tickCount - woundedTicks;
		    if (time < MyConfig.deathTimer)
		    	return;
			if (MyConfig.autoHeal)
			{
				cureEntity(ModItems.GOLDEN_PET_BANDAGE);
			}
			else
			{
				super.die(DamageSource.GENERIC);
				level.broadcastEntityEvent(this, (byte) 60);
				remove(RemovalReason.KILLED);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id)
	{
		if (id == 101)
			cureEntity(null);
		else {
			super.handleEntityEvent(id);
			if (id == 3 && isTamed())
			{
				deathTime = 19;
				super.tickDeath();
			}
		}
	}

	@Override
	public boolean isDying()
	{
		return entityData.get(DATA_POSE) == Pose.DYING && isTamed() && !isRemoved();
	}

	@Override
	public InteractionResult dyingInteract(Player player, InteractionHand hand)
	{
		if (!isDying())
			return InteractionResult.PASS;
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		if (item == ModItems.PET_BANDAGE || item == ModItems.GOLDEN_PET_BANDAGE)
		{
			player.awardStat(Stats.ITEM_USED.get(item));
            if (!player.getAbilities().instabuild) {
            	itemstack.shrink(1);
            }
			if (player instanceof ServerPlayer) {
				ModTriggers.SAVE_PET.trigger((ServerPlayer) player, this);
				cureEntity(item);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		else if (player.isSecondaryUseActive()) {
			openInventory(player);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	void cureEntity(Item item)
	{
		if (level.isClientSide)
		{
			for (int i = 0; i < 10; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY(),
						this.getRandomZ(1.0D), d0, d1, d2);
			}
		}
		else
			level.broadcastEntityEvent(this, (byte) 101);
		setDeltaMovement(Vec3.ZERO);
		this.entityData.set(DATA_POSE, Pose.STANDING);
		setHealth(1.0F);
		deathTime = 0;
		if (!level.isClientSide && isInWall())
		{
			setPos(Math.floor(getX()) + 0.5, getY(), Math.floor(getZ()) + 0.5);
			if (isInWall())
				setHealth(3.0F);
		}
		else
			reapplyPosition();
		if (item == ModItems.GOLDEN_PET_BANDAGE)
			addEffect(new MobEffectInstance(MobEffects.REGENERATION, MyConfig.healTime, 1));
	}

	@Override
	protected boolean isImmobile() {
		if (isDying())
			return true;
		return super.isImmobile();
	}

	@Override
	protected AABB makeBoundingBox()
	{
		return (deathTime >= 10) ? dyingBoundingBox() : super.makeBoundingBox();
	}

	private AABB dyingBoundingBox()
	{
		double ang = (double) getYRot() * (Math.PI / 180.0);
		EntityDimensions size = getDimensions(Pose.STANDING);
		double width = size.width / 2.0;
		double length = size.height / 2.0;
		double lcos = length * Math.cos(ang);
		double lsin = length * Math.sin(ang);
		double x0 = getX();
		double x1 = x0 + lcos;
		double y0 = getY();
		double z0 = getZ();
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
		return new AABB(xmin, y0, zmin, xmax, y0 + width, zmax);
	}
}
