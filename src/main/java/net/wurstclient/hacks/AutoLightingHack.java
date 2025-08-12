/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.WurstRenderLayers;
import net.wurstclient.events.RenderListener;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.EnumSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.util.*;
import net.wurstclient.util.BlockPlacer.BlockPlacingParams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SearchTags({"auto light", "auto torch", "anti mob", "light"})
public final class AutoLightingHack extends Hack
	implements UpdateListener, RenderListener
{
	
	private final EnumSetting<PlacementMode> mode = new EnumSetting<>("Mode",
		"'Intelligent' places lights sparingly to cover large areas.\n"
			+ "'Normal' places a light in every single dark spot it finds.",
		PlacementMode.values(), PlacementMode.INTELLIGENT);
	
	private final SliderSetting lightLevelThreshold =
		new SliderSetting("Min Light Level",
			"Places lights until the block light level is above this value.", 2,
			0, 14, 1, ValueDisplay.INTEGER);
	private final SliderSetting searchRange =
		new SliderSetting("Search Range", "How far to search for dark spots.",
			16, 8, 32, 1, ValueDisplay.INTEGER);
	private final SliderSetting placeRange = new SliderSetting("Place Range",
		"How far to reach when placing blocks.\n(4.25 is recommended for servers)",
		4.25, 1, 6, 0.05, ValueDisplay.DECIMAL);
	private final SliderSetting delay =
		new SliderSetting("Delay", "Delay between placing blocks (in ticks).",
			15, 0, 20, 1, ValueDisplay.INTEGER);
	
	private final CheckboxSetting useTorches =
		new CheckboxSetting("Use Torches", true);
	private final CheckboxSetting useLanterns =
		new CheckboxSetting("Use Lanterns", false);
	private final CheckboxSetting useGlowstone =
		new CheckboxSetting("Use Glowstone", false);
	
	private final CheckboxSetting onlyWhenHolding = new CheckboxSetting(
		"Only When Holding",
		"Only places lights if you are holding a valid light source.", false);
	private final CheckboxSetting previewPlacement = new CheckboxSetting(
		"Preview Placement", "Shows where lights will be placed.", true);
	private final CheckboxSetting checkLOS = new CheckboxSetting(
		"Check Line of Sight", "Prevents placing lights through walls.", false);
	
	private final List<BlockPos> placementPreviews = new ArrayList<>();
	private int placeTimer = 0;
	
	public AutoLightingHack()
	{
		super("AutoLighting");
		setCategory(Category.BLOCKS);
		addSetting(mode);
		addSetting(lightLevelThreshold);
		addSetting(searchRange);
		addSetting(placeRange);
		addSetting(delay);
		addSetting(useTorches);
		addSetting(useLanterns);
		addSetting(useGlowstone);
		addSetting(onlyWhenHolding);
		addSetting(previewPlacement);
		addSetting(checkLOS);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
		placementPreviews.clear();
	}
	
	@Override
	public void onUpdate()
	{
		if(placeTimer > 0)
		{
			placeTimer--;
			return;
		}
		
		scanAndPlace();
	}
	
	private void scanAndPlace()
	{
		Item itemToPlace = getBestLightSource();
		if(itemToPlace == null || (onlyWhenHolding.isChecked()
			&& !MC.player.getMainHandStack().isOf(itemToPlace)))
		{
			placementPreviews.clear();
			return;
		}
		
		Vec3d eyesPos = RotationUtils.getEyesPos();
		BlockPos playerPos = BlockPos.ofFloored(eyesPos);
		int r = searchRange.getValueI();
		
		List<BlockPos> darkSpots = new ArrayList<>();
		for(BlockPos pos : BlockPos.iterate(playerPos.add(-r, -r, -r),
			playerPos.add(r, r, r)))
		{
			if(BlockUtils.getState(pos).isReplaceable()
				&& BlockUtils.getState(pos).isSideSolidFullSquare(MC.world,
					pos.down(), Direction.UP)
				&& MC.world.getLightLevel(LightType.BLOCK,
					pos) <= lightLevelThreshold.getValueI())
			{
				darkSpots.add(new BlockPos(pos));
			}
		}
		
		darkSpots.sort(
			Comparator.comparingDouble(pos -> pos.getSquaredDistance(eyesPos)));
		
		placementPreviews.clear();
		if(mode.getSelected() == PlacementMode.NORMAL)
		{
			placementPreviews.addAll(darkSpots);
		}else
		{
			Set<BlockPos> coveredSpots = new HashSet<>();
			int luminance = getLuminanceOfItem(itemToPlace);
			int lightSpread = luminance > 1 ? luminance - 1 : 0;
			
			for(BlockPos darkSpot : darkSpots)
			{
				if(coveredSpots.contains(darkSpot))
					continue;
				
				placementPreviews.add(darkSpot);
				
				for(int dx = -lightSpread; dx <= lightSpread; dx++)
				{
					for(int dy = -lightSpread; dy <= lightSpread; dy++)
					{
						for(int dz = -lightSpread; dz <= lightSpread; dz++)
						{
							if(Math.abs(dx) + Math.abs(dy)
								+ Math.abs(dz) < luminance)
							{
								coveredSpots.add(darkSpot.add(dx, dy, dz));
							}
						}
					}
				}
			}
		}
		
		if(placementPreviews.isEmpty())
			return;
		
		for(BlockPos target : placementPreviews)
		{
			if(target.getSquaredDistance(eyesPos) > placeRange.getValueSq())
				continue;
			
			BlockPlacingParams params =
				BlockPlacer.getBlockPlacingParams(target);
			if(params == null
				|| (checkLOS.isChecked() && !params.lineOfSight()))
				continue;
			
			if(!MC.player.getMainHandStack().isOf(itemToPlace))
			{
				if(!InventoryUtils.selectItem(stack -> stack.isOf(itemToPlace),
					36, true))
				{
					return;
				}
				return;
			}
			
			RotationUtils.getNeededRotations(params.hitVec())
				.sendPlayerLookPacket();
			InteractionSimulator.rightClickBlock(params.toHitResult());
			placeTimer = delay.getValueI();
			return;
		}
	}
	
	private Item getBestLightSource()
	{
		if(useGlowstone.isChecked()
			&& InventoryUtils.count(Items.GLOWSTONE) > 0)
			return Items.GLOWSTONE;
		if(useLanterns.isChecked() && InventoryUtils.count(Items.LANTERN) > 0)
			return Items.LANTERN;
		if(useTorches.isChecked() && InventoryUtils.count(Items.TORCH) > 0)
			return Items.TORCH;
		return null;
	}
	
	private int getLuminanceOfItem(Item item)
	{
		if(item == Items.GLOWSTONE || item == Items.LANTERN)
			return 28;
		if(item == Items.TORCH)
			return 24;
		return 0;
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(!previewPlacement.isChecked() || placementPreviews.isEmpty())
		{
			return;
		}
		
		int yellow_out_of_reach = 0x80FFFF00; // 50% transparent yellow
		int green_in_reach = 0x8000FF00; // 50% transparent green
		
		double placeRangeSq = placeRange.getValueSq();
		Vec3d eyesPos = RotationUtils.getEyesPos();
		
		matrixStack.push();
		RenderUtils.applyRenderOffset(matrixStack);
		
		VertexConsumer buffer =
			RenderUtils.getVCP().getBuffer(WurstRenderLayers.getQuads(true));
		
		for(BlockPos pos : placementPreviews)
		{
			int color = pos.getSquaredDistance(eyesPos) <= placeRangeSq
				? green_in_reach : yellow_out_of_reach;
			RenderUtils.drawSolidBox(matrixStack, buffer, new Box(pos), color);
		}
		
		RenderUtils.getVCP().draw();
		matrixStack.pop();
	}
	
	public enum PlacementMode
	{
		INTELLIGENT("Intelligent"),
		NORMAL("Normal");
		
		private final String name;
		
		PlacementMode(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
