package eu.BullCheat.AdvancedCombatTag.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.BullCheat.AdvancedCombatTag.AdvancedCombatTag.UntagReason;

public class EventCombatPlayerEnd extends Event {
	
	private static HandlerList hl = new HandlerList();

	public HandlerList getHandlers() { return hl; }
	public static HandlerList getHandlerList() { return hl; }
	
	private Player player;
	private UntagReason reason;
	
	public EventCombatPlayerEnd(Player player, UntagReason reason) {
		this.player = player;
		this.reason = reason;
	}
	
	public Player getPlayer() { return player; }
	public UntagReason getReason() { return reason; }

}
