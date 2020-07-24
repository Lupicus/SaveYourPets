package com.lupicus.syp.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;

public interface IDying
{
	public boolean isDying();

	public ActionResultType dyingInteract(PlayerEntity player, Hand hand);

	default String formatLoc(Vector3d pos)
	{
		return String.format("[%.1f, %.1f, %.1f]", pos.x, pos.y, pos.z);
	}
}
