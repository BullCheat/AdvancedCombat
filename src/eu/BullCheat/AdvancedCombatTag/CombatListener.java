package eu.BullCheat.AdvancedCombatTag;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.BullCheat.AdvancedCombatTag.AdvancedCombatTag.TagReason;
import eu.BullCheat.AdvancedCombatTag.AdvancedCombatTag.UntagReason;
import eu.BullCheat.BullLIB.BullLIB;
import eu.BullCheat.BullLIB.BullList;

public class CombatListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCombat(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player dd = (Player) e.getEntity();
			Player dr = (Player) e.getDamager();
			BullList<String> regions;
			regions = BullLIB.getRegions(dd.getLocation());
			if (regions != null) {
				if (test(regions)) AdvancedCombatTag.tag(dd, TagReason.WAS_ATTACKED);
			} else {
				if (!Conf.whitelist_enabled) AdvancedCombatTag.tag(dd, TagReason.WAS_ATTACKED);
			}
			regions = BullLIB.getRegions(dr.getLocation());
			if (regions != null) {
				if (test(regions)) AdvancedCombatTag.tag(dr, TagReason.ATTACKED_OTHER);	
			} else {
				if (!Conf.whitelist_enabled) AdvancedCombatTag.tag(dr, TagReason.ATTACKED_OTHER);
			}
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		if (Conf.preventKick) {
			AdvancedCombatTag.untag(e.getPlayer(), UntagReason.KICK);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		int c = AdvancedCombatTag.getCombat(e.getPlayer());
		if (c > -1) {
			e.getPlayer().setHealth(0);
			Bukkit.broadcast(new TranslatableComponent("heyfactions.death.combat",e.getPlayer().getName(), String.valueOf(c)));
			AdvancedCombatTag.untag(e.getPlayer(), UntagReason.DISCONNECT);
		}
	}
	
	private boolean test(BullList<String> list) {
		if (Conf.blacklist_enabled) {
			if (list.containsOneCI(Conf.blacklist_regions)) return false;
		}
		if (Conf.whitelist_enabled) {
			if (list.containsOneCI(Conf.whitelist_regions)) return true;
			return false;
		}
		return true;
	}

}
