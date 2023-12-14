package com.lupicus.syp.advancements.criterion;

import java.util.Optional;

import com.lupicus.syp.advancements.ModTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class SavePetTrigger extends SimpleCriterionTrigger<SavePetTrigger.TriggerInstance>
{
	@Override
	public Codec<SavePetTrigger.TriggerInstance> codec() {
		return SavePetTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Animal entity) {
		LootContext lootcontext = EntityPredicate.createContext(player, entity);
		this.trigger(player, (inst) -> {
			return inst.matches(lootcontext);
		});
	}

	public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<SavePetTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create((inst) -> {
			return inst.group(ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(SavePetTrigger.TriggerInstance::player), ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "entity").forGetter(SavePetTrigger.TriggerInstance::entity)).apply(inst, SavePetTrigger.TriggerInstance::new);
		});

		public static Criterion<SavePetTrigger.TriggerInstance> savePet() {
			return ModTriggers.SAVE_PET.createCriterion(new SavePetTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
		}

		public static Criterion<SavePetTrigger.TriggerInstance> savePet(EntityPredicate.Builder predicate) {
			return ModTriggers.SAVE_PET.createCriterion(new SavePetTrigger.TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(predicate))));
		}

		public boolean matches(LootContext lootcontext) {
			return this.entity.isEmpty() || this.entity.get().matches(lootcontext);
		}

		@Override
		public void validate(CriterionValidator p_309538_) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(p_309538_);
			p_309538_.validateEntity(this.entity, ".entity");
		}

		public Optional<ContextAwarePredicate> player() {
			return this.player;
		}
	}
}
