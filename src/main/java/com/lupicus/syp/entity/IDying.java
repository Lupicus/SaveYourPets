package com.lupicus.syp.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface IDying
{
	public boolean isDying();

	public InteractionResult dyingInteract(Player player, InteractionHand hand);

	default String formatLoc(Vec3 pos)
	{
		return String.format("[%.1f, %.1f, %.1f]", pos.x, pos.y, pos.z);
	}
}
