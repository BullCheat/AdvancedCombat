package eu.BullCheat.AdvancedCombatTag;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.BullCheat.AdvancedCombatTag.Events.EventCombatPlayerBegin;
import eu.BullCheat.AdvancedCombatTag.Events.EventCombatPlayerEnd;
import eu.BullCheat.BullLIB.BullLIB;
import eu.BullCheat.net.cubespace.Yamler.Config.InvalidConfigurationException;
import fr.HeyFactions.Packets.CooldownsPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

public class AdvancedCombatTag extends JavaPlugin {
	
	static Conf c;
	
	private static HashMap<UUID, Long> combats = new HashMap<>();
	
	@Override
	public void onEnable() {
		c = new Conf(this);
		getServer().getPluginManager().registerEvents(new CombatListener(), this);
		try {
			c.init();
		} catch (InvalidConfigurationException e) {
			getLogger().severe("Can't init config, please check you put valid YAML and that config.yml is writeable (chmod?)");
			e.printStackTrace();
		}
		getCommand("advancedcombattag").setExecutor(new AdvancedCombatTagCommand());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArrayList<UUID> toRemove = new ArrayList<>();
				for (Entry<UUID, Long> e : combats.entrySet()) {
					if (e.getValue() < System.currentTimeMillis()) toRemove.add(e.getKey());
				}
				for (UUID uuid : toRemove) {
					Player p = Bukkit.getPlayer(uuid);
					if (p != null) {
						untag(p, UntagReason.TIME_OUT);
					} else {
						combats.remove(uuid);
						getLogger().warning("Removed " + uuid + " from combats while offline!");
					}
				}
			}
		}.runTaskTimer(this, 1, 1);
	}

	public static void notify(Player player, int remaining) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		new CooldownsPacket().setCombat(remaining).setEnderpearl(0).write(out);
		BullLIB.sendPluginMessage(player, out.toByteArray());
	}
	
	/**
	 * Untags a player
	 */
	
	public static void untag(Player player, UntagReason reason) {
		if (player != null && combats.containsKey(player.getUniqueId())) {
			EventCombatPlayerEnd event = new EventCombatPlayerEnd(player, reason);
			Bukkit.getPluginManager().callEvent(event);
			combats.remove(player.getUniqueId());
			if (reason != UntagReason.DISCONNECT && reason != UntagReason.KICK) notify(player, 0);
		}
	}
	
	/**
	 * Tag a player in combat
	 */
	public static void tag(Player player, TagReason reason) {
		if (player != null) {
			EventCombatPlayerBegin event = new EventCombatPlayerBegin(player, reason);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				combats.put(player.getUniqueId(), System.currentTimeMillis() + (int) (Conf.combatDuration * 1E3));
				notify(player, (int) Math.ceil(Conf.combatDuration));
			}
		}
	}
	

	/**
	 * 
	 * @param player The player to be tested
	 * @return The number of seconds of combat remaining. -1 means no combat. 0 means there is < 1 <b>millisecond</b> left
	 */
	public static int getCombat(Player player) {
		int t = getCombatMilis(player);
		if (t == -1) return -1;
		return (int) Math.ceil(t / 1E3);
	}


	/**
	 * 
	 * @param player The player to be tested
	 * @return The number of seconds of combat remaining. -1 means no combat. 0 means there is < 1 millisecond left
	 */
	public static int getCombatMilis(Player player) {
		if (!combats.containsKey(player.getUniqueId())) return -1;
		long to = combats.get(player.getUniqueId());
		long time = System.currentTimeMillis();
		return Math.max((int) (to - time), -1);
		
	}

	public enum TagReason {
		PLUGIN,
		ATTACKED_OTHER,
		WAS_ATTACKED;
	}
	public enum UntagReason {
		PLUGIN,
		TIME_OUT,
		KICK,
		DISCONNECT;
	}

}
