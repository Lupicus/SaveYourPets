package com.lupicus.syp.advancements.criterion;

import com.google.gson.JsonObject;
import com.lupicus.syp.Main;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

public class SavePetTrigger extends AbstractCriterionTrigger<SavePetTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Main.MODID, "save_pet");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public Instance func_230241_b_(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser parser) {
		EntityPredicate.AndPredicate entitypredicate = EntityPredicate.AndPredicate.func_234587_a_(json, "entity", parser);
		return new SavePetTrigger.Instance(predicate, entitypredicate);
	}

	public void trigger(ServerPlayerEntity player, AnimalEntity entity) {
		LootContext lootcontext = EntityPredicate.func_234575_b_(player, entity);
		this.func_235959_a_(player, (p_227251_1_) -> {
			return p_227251_1_.func_236323_a_(lootcontext);
		});
	}

	public static class Instance extends CriterionInstance {
		private final EntityPredicate.AndPredicate entity;

		public Instance(EntityPredicate.AndPredicate predicate, EntityPredicate.AndPredicate entity) {
			super(SavePetTrigger.ID, predicate);
			this.entity = entity;
		}

		public static SavePetTrigger.Instance any() {
			return new SavePetTrigger.Instance(EntityPredicate.AndPredicate.field_234582_a_, EntityPredicate.AndPredicate.field_234582_a_);
		}

		public static SavePetTrigger.Instance create(EntityPredicate predicate) {
			return new SavePetTrigger.Instance(EntityPredicate.AndPredicate.field_234582_a_, EntityPredicate.AndPredicate.func_234585_a_(predicate));
		}

		public boolean func_236323_a_(LootContext lootcontext) {
			return this.entity.func_234588_a_(lootcontext);
		}

		@Override
		public JsonObject func_230240_a_(ConditionArraySerializer serializer) {
			JsonObject jsonobject = super.func_230240_a_(serializer);
			jsonobject.add("entity", this.entity.func_234586_a_(serializer));
			return jsonobject;
		}
	}
}
