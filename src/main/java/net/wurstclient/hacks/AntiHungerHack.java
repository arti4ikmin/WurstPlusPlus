/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.PacketOutputListener;
import net.wurstclient.hack.DontSaveState;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.util.PacketUtils;

@DontSaveState
@SearchTags({"anti hunger"})
public final class AntiHungerHack extends Hack implements PacketOutputListener
{
	private final CheckboxSetting softSprint = new CheckboxSetting(
		"Soft NoSprint",
		"Doesnt send sprint packages to server = no hunger, you are sprinting client-side, might trigger AntiCheat.",
		false);
	
	public AntiHungerHack()
	{
		super("AntiHunger");
		setCategory(Category.MOVEMENT);
		addSetting(softSprint);
	}
	
	@Override
	protected void onEnable()
	{
		WURST.getHax().noFallHack.setEnabled(false);
		EVENTS.add(PacketOutputListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(PacketOutputListener.class, this);
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if(softSprint.isChecked()
			&& event.getPacket() instanceof ClientCommandC2SPacket sprintPacket)
		{
			if(sprintPacket
				.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING)
			{
				event.cancel();
				return;
			}
		}
		
		if(!(event.getPacket() instanceof PlayerMoveC2SPacket packet))
			return;
		
		if(!MC.player.isOnGround() || MC.player.fallDistance > 0.5F
			|| MC.interactionManager.isBreakingBlock() || MC.player.hasVehicle()
			|| MC.player.isTouchingWater() || MC.player.isSwimming()
			|| MC.player.isSubmergedInWater())
			return;
		
		event.setPacket(PacketUtils.modifyOnGround(packet, false));
	}
}
