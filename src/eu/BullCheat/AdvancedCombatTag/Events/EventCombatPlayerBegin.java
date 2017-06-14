package eu.BullCheat.AdvancedCombatTag.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.BullCheat.AdvancedCombatTag.AdvancedCombatTag.TagReason;

public class EventCombatPlayerBegin extends Event implements Cancellable {
	
	private static HandlerList hl = new HandlerList();

	public HandlerList getHandlers() { return hl; }
	public static HandlerList getHandlerList() { return hl; }
	
	private Player player;
	private TagReason reason;
	private boolean cancelled;
	
	public EventCombatPlayerBegin(Player player, TagReason reason) {
		this.player = player;
		this.reason = reason;
		this.cancelled = false;
	}
	
	public Player getPlayer() { return player; }
	public TagReason getReason() { return reason; }

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean c) {
		cancelled = c;
		
	}

}
