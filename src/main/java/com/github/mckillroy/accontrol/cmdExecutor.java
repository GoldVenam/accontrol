package com.github.mckillroy.accontrol;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

public class cmdExecutor implements CommandExecutor {

	public static P plugin;

	public cmdExecutor(P p) {
		plugin = p;
	}

	/**
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender,
	 *      org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("accontrol")) {
			// Do we have args? If not just show help message
			if (args.length < 1) {
				return showHelp(sender);
			}
			try {
				switch (P.commands.valueOf(args[0])) {
				case showsettings:
					return showSettings(sender);
				case listallowed:
					return listAllowed(sender);
				case setsafetyzone:
					return setSafetyzone(sender, args);
				case addallowed:
					return addAllowed(sender, args);
				case removeallowed:
					return removeAllowed(sender, args);
				case saveconfig:
					return saveConfig(sender);
				case reloadconfig:
					return reloadConfig(sender);
				case setdebug:
					return setDebug(sender, args);
				case setallallowed:
					return setAllAllowed(sender, args);
				case setplayerfactionok:
					return setPlayerFactionOK(sender, args);
				case setalliedfactionok:
					return setAlliedfactionOK(sender, args);
				case help:
					return showHelp(sender);

				default:
					return false;
				}
			} catch (Exception e) {

				
				showHelp(sender);
				sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED.toString()
						+ "Wrong argument. Please check usage info above.");

				if (P.config.getConfig().getBoolean("debug")) {
					sender.sendMessage(String.format("Exception: %s",
							e.getCause()));
					sender.sendMessage(String.format("Exception: %s",
							(Object[]) e.getStackTrace()));
					e.printStackTrace();
				}

				return true;
			}
		}
		return false;
	}

	/**
	 * Intializing configuration stuff
	 */

	public void reloadConfig() {
		P.config.reloadConfig();
		return;

	}

	/**
	 * @param sender
	 * @return
	 */
	public boolean reloadConfig(CommandSender sender) {
		if (sender.hasPermission("accontrol.reloadconfig")) {
			// start
			P.config.reloadConfig();
			showSettings(sender);
			sender.sendMessage("Configuration reloaded. Check values above.");
			return true;
			// end
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for reloadconfig.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean removeAllowed(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.removeallowed")) {
			// removeallowed
			// sender.sendMessage("removeallowed");
			Faction myFaction = Factions.i.getBestTagMatch(args[1]);

			// Return on NULL Faction
			if (myFaction == null) {
				sender.sendMessage("Faction not found - command cancelled !");
				return true;
			}

			// Debug message Faction found
			if (P.config.getConfig().getBoolean("debug")) {
				sender.sendMessage("Found matching faction: "
						+ myFaction.getTag());
			}
			// Faction already disallowed
			if (!P.config.getConfig()
					.getStringList("factions.AllowedFactionIds")
					.contains(myFaction.getId())) {
				sender.sendMessage(String.format(
						"%s is not allowed already - command cancelled.",
						myFaction.getTag()));
				return true;
			}

			// removing faction from list
			// sender.sendMessage(P.config.getConfig().getStringList("factions.AllowedFactionIds").toString());
			// listAllowed(sender);
			List<String> l = P.config.getConfig().getStringList(
					"factions.AllowedFactionIds");
			
			l.removeAll(Collections.singleton(myFaction.getId()));
			
			P.config.getConfig().set("factions.AllowedFactionIds", l);

			sender.sendMessage(String
					.format("Removed %s from list of allowed factions. Don't forget to save!",
							myFaction.getTag()));
			
			listAllowed(sender);

			// removeallowed end
			return true;
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for removeallowed.");
		return true;
	}

	public void saveConfig() {

		P.config.saveConfig();
	}

	/**
	 * @param sender
	 * @return
	 */
	public boolean saveConfig(CommandSender sender) {
		if (sender.hasPermission("accontrol.saveconfig")) {
			// / start
			saveConfig();
			sender.sendMessage("Configuration saved");
			return true;
			// / end
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for saveconfig.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean setAllAllowed(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.setallowed")) {
			// start
			P.config.getConfig().set("factions.AllAllowed",
					Boolean.parseBoolean(args[1]));
			sender.sendMessage(String.format(
					"AllAllowed set to %s. Don't forget to save!", P.config
							.getConfig().getBoolean("factions.AllAllowed")));
			return true;
			// end
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for setallowed.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean setAlliedfactionOK(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.setalliedfactionok")) {
			// start
			P.config.getConfig().set("factions.AlliedFactionOK",
					Boolean.parseBoolean(args[1]));
			sender.sendMessage(String
					.format("alliedFactionOK set to %s. Don't forget to save!",
							P.config.getConfig().getBoolean(
									"factions.AlliedFactionOK")));
			return true;
			// end
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for setplayerfactionok.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean setDebug(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.setdebug")) {
			// start
			P.config.getConfig().set("debug", Boolean.parseBoolean(args[1]));
			sender.sendMessage(String.format(
					"debug set to %s. Don't forget to save!", P.config
							.getConfig().getBoolean("debug")));
			return true;
			// end
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for setdebug.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean setPlayerFactionOK(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.setplayerfactionok")) {
			// start
			P.config.getConfig().set("factions.PlayerFactionOK",
					Boolean.parseBoolean(args[1]));
			sender.sendMessage(String
					.format("playerFactionOK set to %s. Don't forget to save!",
							P.config.getConfig().getBoolean(
									"factions.PlayerFactionOK")));
			return true;
			// end
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for setplayerfactionok.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean setSafetyzone(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.setsafetyzone")) {
			// setsafetyzone
			P.config.getConfig().set("safetyzone", Integer.decode(args[1]));
			sender.sendMessage(String.format("Safetyzone set to %s", P.config
					.getConfig().getInt("safetyzone")));
			// setsafetyzone end
			return true;
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for setsafezone.");
		return true;
	}

	/**
	 * @param sender
	 * @return
	 */
	public boolean showHelp(CommandSender sender) {
		sender.sendMessage("\n\u00a76/acc setdebug \u00a7b[true/false] \u00a7f: enables or disables debug output."
				+ "\n\u00a76/acc showsettings \u00a7f: List the currently active settings."
				+ "\n\u00a76/acc listallowed \u00a7f: Lists all factions you are allowed to fly over."
				+ "\n\u00a76/acc setsafetyzone \u00a7b[integer] \u00a7f: adds an additional buffer zone to the allowed distance from foreign land where flying is disallowed."
				+ "\n\u00a76/acc setallallowed \u00a7b[true/false] \u00a7f: Flying everywhere !"
				+ "\n\u00a76/acc setplayerfactionok \u00a7b[true/false] \u00a7f: Flying over Players own land allowed?"
				+ "\n\u00a76/acc setalliedfactionok \u00a7b[true/false] \u00a7f: Flying over allied factions land allowed?"
				+ "\n\u00a76/acc addallowed \u00a7b[faction tag] \u00a7f: Add a faction to the list of allowed factions to fly over."
				+ "\n\u00a76/acc removeallowed \u00a7b[faction tag] \u00a7f: Remove a faction from the list of allowed factions to fly over."
				+ "\n\u00a76/acc saveconfig \u00a7f: Save current settings to file."
				+ "\n\u00a76/acc reloadconfig \u00a7f: Reload settings from file and overwrite changes you made in between.");
		return true;
	}

	/**
	 * @param sender
	 * @return
	 */
	public boolean showSettings(CommandSender sender) {
		if (sender.hasPermission("accontrol.showsettings")) {
			// showsettings
			sender.sendMessage(P.CHAT_PREFIX + "Settings:");
			sender.sendMessage(String.format(ChatColor.BLUE + "debug:"
					+ ChatColor.GREEN + " %s",
					P.config.getConfig().getBoolean("debug")));
			sender.sendMessage(String.format(ChatColor.BLUE + "safetyzone:"
					+ ChatColor.GREEN + " %s",
					P.config.getConfig().getInt("safetyzone")));
			sender.sendMessage(String.format(ChatColor.BLUE + "allAllowed:"
					+ ChatColor.GREEN + " %s",
					P.config.getConfig().getBoolean("factions.AllAllowed")));
			sender.sendMessage(String.format(ChatColor.BLUE
					+ "playerFactionOK:" + ChatColor.GREEN + " %s", P.config
					.getConfig().getBoolean("factions.PlayerFactionOK")));
			sender.sendMessage(String.format(ChatColor.BLUE
					+ "alliedFactionOK:" + ChatColor.GREEN + " %s", P.config
					.getConfig().getBoolean("factions.AlliedFactionOK")));
			sender.sendMessage(String.format(ChatColor.BLUE
					+ "allowedFactionIds:" + ChatColor.GREEN + " %s", P.config
					.getConfig().getStringList("factions.AllowedFactionIds")));
			// sender.sendMessage(String.format(" :%s",P.));
			// showsettings end
			return true;
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for showsettings.");
		return true;
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean addAllowed(CommandSender sender, String[] args) {
		if (sender.hasPermission("accontrol.addallowed")) {
			// addallowed
			sender.sendMessage("addallowed");
			Faction myFaction = Factions.i.getBestTagMatch(args[1]);
			if (myFaction == null) {
				sender.sendMessage("Faction not found - command cancelled !");
				return true;
			}
			// is the faction already allowed?
			if (P.config.getConfig()
					.getStringList("factions.AllowedFactionIds")
					.contains(myFaction.getId())) {
				sender.sendMessage(String.format(
						"%s is already allowed - command cancelled.",
						myFaction.getTag()));
				return true;
			}
			// if not we can add it - if it exists
			List<String> l = P.config.getConfig().getStringList("factions.AllowedFactionIds");
			l.add(myFaction.getId());
			P.config.getConfig().set("factions.AllowedFactionIds",l);
			sender.sendMessage(String
					.format("Added %s to list of allowed factions. Don't forget to save!",
							myFaction.getTag()));
			listAllowed(sender);
			// addallowed end
			return true;
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for addallowed.");
		return true;
	}

	/**
	 * @param sender
	 * @return
	 */
	public boolean listAllowed(CommandSender sender) {
		if (sender.hasPermission("accontrol.listallowed")) {
			// listallowed
			sender.sendMessage(ChatColor.BLUE + P.CHAT_PREFIX + ChatColor.AQUA
					+ "Factions whose lands can be flight over:");
			String myOutput = "";
			for (String element : P.config.getConfig().getStringList(
					"factions.AllowedFactionIds")) {
				myOutput = myOutput + Factions.i.get(element).getTag() + " ";
			}
			sender.sendMessage(myOutput);
			// listallowed end
			return true;
		}
		sender.sendMessage(P.CHAT_PREFIX + ChatColor.RED
				+ "You don't have permission for listallowed.");
		return true;
	}

}
