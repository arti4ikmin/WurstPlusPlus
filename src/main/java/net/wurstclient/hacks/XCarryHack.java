/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.PacketOutputListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;

@SearchTags({"xcarry", "extra inventory", "crafting carry"})
public final class XCarryHack extends Hack implements PacketOutputListener
{
	
	private final CheckboxSetting dangerousMode = new CheckboxSetting(
		"Ignore safety checks",
		"NOT RECOMMENDED TO ENABLE!!\nWill disable following checks:\n1. Is in inventory screen (allows to get out of sync for other containers\n2. Disable general sync check.",
		false);
	
	private final CheckboxSetting disableInCreative = new CheckboxSetting(
		"Disable in Creative",
		"Turns off XCarry in Creative mode, since there is no 2x2 crafting grid.",
		false);
	
	public XCarryHack()
	{
		super("XCarry");
		setCategory(Category.ITEMS);
		addSetting(dangerousMode);
		addSetting(disableInCreative);
	}
	
	@Override
	protected void onEnable()
	{
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
		if(event
			.getPacket() instanceof CloseHandledScreenC2SPacket closeScreenPacket)
		{
			if(disableInCreative.isChecked()
				&& MC.player.getAbilities().creativeMode)
			{
				return;
			}
			
			if(!dangerousMode.isChecked())
			{
				// only in main inv + synced?
				if(MC.player.playerScreenHandler != null && closeScreenPacket
					.getSyncId() == MC.player.playerScreenHandler.syncId)
				{
					event.cancel();
				}
			}else
			{
				event.cancel();
			}
		}
	}
}
