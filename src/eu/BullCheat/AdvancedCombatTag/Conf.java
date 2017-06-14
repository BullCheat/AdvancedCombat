package eu.BullCheat.AdvancedCombatTag;

import com.google.common.collect.Lists;
import eu.BullCheat.net.cubespace.Yamler.Config.*;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

class Conf extends YamlConfig {
	
	Conf(Plugin p) {
		CONFIG_HEADER = new String[] { "Main config file of " + AdvancedCombatTag.class.getSimpleName(), "All placeholders are either %% (1st) or $$ (2nd)",
				"You can define a region Whitelist & Blacklist (WorldGuard).",
				"The Blacklist takes priority over the Whitelist.",
				"For instance, let's say I'm in a region that's whitelisted. If I attack someone, we will both be combat tagged.",
				"Now let's say I enter a blacklisted region (I'm now in both a whitelisted region and a blacklisted region!), we won't be combat tagged.",
				"Permission to execute /act (to edit this config directly from the game) : AdvancedCombatTag.admin"
		}; // On sait jamais si on change de nom
		CONFIG_FILE = new File(p.getDataFolder(), "config.yml");
		CONFIG_MODE = ConfigMode.PATH_BY_UNDERSCORE;
	}
	
	 @Comment("Duration of the combat, in seconds, supports decimal numbers (ie 3.75)")
	 static float combatDuration = 30;
	 
	 @Comment("If set to true, people who get kicked from the server will NOT die for combat-logging")
	 static boolean preventKick = true;

	 @Comment("Should the whitelist be enabled?")
	 static boolean whitelist_enabled = false;

	 @Comment("Whitelisted regions; if I'm in one of these regions, I will be combat tagged (unless I'm also in a blacklisted region and blacklist->enabled is true)")
	 static List<String> whitelist_regions = Lists.newArrayList("warzone", "pvpbox");

	 @Comment("Should the blacklist be enabled?")
	 static boolean blacklist_enabled = true;

	 @Comment("Blacklisted regions; if I'm in one of these regions, I won't be combat tagged (unconditionally)")
	 static List<String> blacklist_regions = Lists.newArrayList("yolo_pvp", "safezone");
	 
	 

}
