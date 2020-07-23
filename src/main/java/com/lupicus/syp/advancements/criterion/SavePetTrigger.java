package com.lupicus.syp.advancements.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lupicus.syp.Main;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SavePetTrigger extends AbstractCriterionTrigger<SavePetTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Main.MODID, "save_pet");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
		return new SavePetTrigger.Instance(entitypredicate);
	}

	public void trigger(ServerPlayerEntity player, AnimalEntity entity) {
		this.func_227070_a_(player.getAdvancements(), (p_227251_2_) -> {
			return p_227251_2_.test(player, entity);
		});
	}

	public static class Instance extends CriterionInstance {
		private final EntityPredicate entity;

		public Instance(EntityPredicate entity) {
			super(SavePetTrigger.ID);
			this.entity = entity;
		}

		public static SavePetTrigger.Instance any() {
			return new SavePetTrigger.Instance(EntityPredicate.ANY);
		}

		public static SavePetTrigger.Instance func_215124_a(EntityPredicate p_215124_0_) {
			return new SavePetTrigger.Instance(p_215124_0_);
		}

		public boolean test(ServerPlayerEntity player, AnimalEntity entity) {
			return this.entity.test(player, entity);
		}

		@Override
		public JsonElement serialize() {
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("entity", this.entity.serialize());
			return jsonobject;
		}
	}
}
