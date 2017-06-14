package eu.BullCheat.AdvancedCombatTag;

import eu.BullCheat.BullLIB.BullLIB;
import eu.BullCheat.net.cubespace.Yamler.Config.InvalidConfigurationException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AdvancedCombatTagCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§6Edit config directly in-game!");
			sender.sendMessage("§6Usage: §e/act <variable> [value]");
			sender.sendMessage("§6Use tab to loop through variables' names or use /act dump.");
			sender.sendMessage("§6Do not set any value to see the current values.");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("dump")) {
				sender.sendMessage("§6Variables:");
				sender.sendMessage(BullLIB.dumpConfig(Conf.class,  "§e", ": §r", "").toArray());
				return true;
			}
			sender.sendMessage(BullLIB.dumpField(Conf.class, args[0], "§e", ": §r", ""));
			
		} else {
				// SET
				String result = BullLIB.setField(Conf.class, args[0], args, 1);
				if (result != null) // ERROR
					sender.sendMessage(result);
				try {
					AdvancedCombatTag.c.save();
				} catch (InvalidConfigurationException e) {
					sender.sendMessage("§cCouldn't save config, please check permissions (chmod?)");
				}
				// CONFIRM
				sender.sendMessage(BullLIB.dumpField(Conf.class, args[0], "§e", ": §r", ""));


		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!sender.hasPermission("AdvancedCombatTag.admin")) return new ArrayList<>();
		String argument = null;
		String value = null;
		if (args.length > 0) argument = args[0];
		if (args.length > 1) value = args[args.length-1]; 
		return BullLIB.tabCompleteFields(Conf.class, argument, value);
	}
	


}
