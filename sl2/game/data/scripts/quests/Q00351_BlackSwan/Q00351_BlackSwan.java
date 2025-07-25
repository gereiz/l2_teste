/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q00351_BlackSwan;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00351_BlackSwan extends Quest
{
	// NPCs
	private static final int GOSTA = 30916;
	private static final int IASON_HEINE = 30969;
	private static final int ROMAN = 30897;
	// Items
	private static final int ORDER_OF_GOSTA = 4296;
	private static final int LIZARD_FANG = 4297;
	private static final int BARREL_OF_LEAGUE = 4298;
	private static final int BILL_OF_IASON_HEINE = 4310;
	
	public Q00351_BlackSwan()
	{
		super(351);
		registerQuestItems(ORDER_OF_GOSTA, BARREL_OF_LEAGUE, LIZARD_FANG);
		addStartNpc(GOSTA);
		addTalkId(GOSTA, IASON_HEINE, ROMAN);
		addKillId(20784, 20785, 21639, 21640);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "30916-03.htm":
			{
				st.startQuest();
				giveItems(player, ORDER_OF_GOSTA, 1);
				break;
			}
			case "30969-02a.htm":
			{
				final int lizardFangs = getQuestItemsCount(player, LIZARD_FANG);
				if (lizardFangs > 0)
				{
					htmltext = "30969-02.htm";
					
					takeItems(player, LIZARD_FANG, -1);
					giveAdena(player, lizardFangs * 20, true);
				}
				break;
			}
			case "30969-03a.htm":
			{
				final int barrels = getQuestItemsCount(player, BARREL_OF_LEAGUE);
				if (barrels > 0)
				{
					htmltext = "30969-03.htm";
					
					takeItems(player, BARREL_OF_LEAGUE, -1);
					rewardItems(player, BILL_OF_IASON_HEINE, barrels);
					
					// Heine explains than player can speak with Roman in order to exchange bills for rewards.
					if (st.isCond(1))
					{
						st.setCond(2, true);
					}
				}
				break;
			}
			case "30969-06.htm":
			{
				// If no more quest items finish the quest for real, else send a "Return" type HTM.
				if (!hasQuestItems(player, BARREL_OF_LEAGUE, LIZARD_FANG))
				{
					htmltext = "30969-07.htm";
					st.exitQuest(true, true);
				}
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = (player.getLevel() < 32) ? "30916-00.htm" : "30916-01.htm";
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case GOSTA:
					{
						htmltext = "30916-04.htm";
						break;
					}
					case IASON_HEINE:
					{
						htmltext = "30969-01.htm";
						break;
					}
					case ROMAN:
					{
						htmltext = (hasQuestItems(player, BILL_OF_IASON_HEINE)) ? "30897-01.htm" : "30897-02.htm";
						break;
					}
				}
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isPet)
	{
		final QuestState st = getQuestState(player, false);
		if ((st == null) || !st.isStarted())
		{
			return null;
		}
		
		final int random = getRandom(4);
		if (random < 3)
		{
			giveItems(player, LIZARD_FANG, random < 2 ? 1 : 2);
			playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			if (getRandomBoolean())
			{
				giveItems(player, BARREL_OF_LEAGUE, 1);
			}
		}
		else if (getRandom(10) < (npc.getId() > 20785 ? 3 : 4))
		{
			giveItems(player, BARREL_OF_LEAGUE, 1);
			playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
		}
		
		return null;
	}
}
