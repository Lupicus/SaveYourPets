package com.lupicus.syp.advancements.criterion;

import java.util.Optional;

import com.google.gson.JsonObject;
import com.lupicus.syp.advancements.ModTriggers;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class SavePetTrigger extends SimpleCriterionTrigger<SavePetTrigger.Instance>
{
	@Override
	public SavePetTrigger.Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext ctx) {
		Optional<ContextAwarePredicate> entitypredicate = EntityPredicate.fromJson(json, "entity", ctx);
		return new SavePetTrigger.Instance(player, entitypredicate);
	}

	public void trigger(ServerPlayer player, Animal entity) {
		LootContext lootcontext = EntityPredicate.createContext(player, entity);
		this.trigger(player, (inst) -> {
			return inst.matches(lootcontext);
		});
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final Optional<ContextAwarePredicate> entity;

		public Instance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) {
			super(player);
			this.entity = entity;
		}

		public static Criterion<SavePetTrigger.Instance> savePet() {
			return ModTriggers.SAVE_PET.createCriterion(new SavePetTrigger.Instance(Optional.empty(), Optional.empty()));
		}

		public static Criterion<SavePetTrigger.Instance> savePet(EntityPredicate.Builder predicate) {
			return ModTriggers.SAVE_PET.createCriterion(new SavePetTrigger.Instance(Optional.empty(), Optional.of(EntityPredicate.wrap(predicate))));
		}

		public boolean matches(LootContext lootcontext) {
			return this.entity.isEmpty() || this.entity.get().matches(lootcontext);
		}

		@Override
		public JsonObject serializeToJson() {
			JsonObject jsonobject = super.serializeToJson();
			entity.ifPresent((c) -> {
				jsonobject.add("entity", c.toJson());
			});
			return jsonobject;
		}
	}
}
