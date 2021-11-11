package com.lupicus.syp.advancements.criterion;

import com.google.gson.JsonObject;
import com.lupicus.syp.Main;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class SavePetTrigger extends SimpleCriterionTrigger<SavePetTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Main.MODID, "save_pet");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public SavePetTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext parser) {
		EntityPredicate.Composite entitypredicate = EntityPredicate.Composite.fromJson(json, "entity", parser);
		return new SavePetTrigger.Instance(predicate, entitypredicate);
	}

	public void trigger(ServerPlayer player, Animal entity) {
		LootContext lootcontext = EntityPredicate.createContext(player, entity);
		this.trigger(player, (p_227251_1_) -> {
			return p_227251_1_.matches(lootcontext);
		});
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate.Composite entity;

		public Instance(EntityPredicate.Composite predicate, EntityPredicate.Composite entity) {
			super(SavePetTrigger.ID, predicate);
			this.entity = entity;
		}

		public static SavePetTrigger.Instance any() {
			return new SavePetTrigger.Instance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
		}

		public static SavePetTrigger.Instance create(EntityPredicate predicate) {
			return new SavePetTrigger.Instance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(predicate));
		}

		public boolean matches(LootContext lootcontext) {
			return this.entity.matches(lootcontext);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext serializer) {
			JsonObject jsonobject = super.serializeToJson(serializer);
			jsonobject.add("entity", this.entity.toJson(serializer));
			return jsonobject;
		}
	}
}
