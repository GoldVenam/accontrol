package com.github.mckillroy.accontrol;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collection;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

//import com.massivecraft.factions.Board;
import com.minesworn.autocraft.Autocraft;

/**
 * @author McYorlik
 * @version 0.1-dev
 */
public final class P extends JavaPlugin {
	// Plugin instance
	/**
	 * plugin is the single instance of the plugin P (singleton)
	 */
	public static P plugin;
	public static ConfigAccessor config;

	// Globals needed by AcListener

	/**
	 * This enum hold values needed in the command preprocess listener in the
	 * listener class
	 */
	public static enum aclvl2Cmds {
		pilot,p,turn,t,rotate,r;
	} // needed later for switch in command parsing of 2nd word in retrieved
	

	/**
	 * This is needed for parsing the commands later Commands: # <br>
	 * showsettings: print settings # <br>
	 * listallowed: list allowed factions # # <br>
	 * setsafetyzone #: set size of safezone to # # <br>
	 * addallowed #: add # as allowed faction for flight # <br>
	 * removeallowed #: remove # as allowed faction for flight # <br>
	 * saveconfig: save the current configuration enable: plugin //later # <br>
	 * disable: plugin // later #
	 * 
	 */
	public static enum commands {
		help, setdebug, showsettings, listallowed, setsafetyzone, setallallowed, setplayerfactionok, setalliedfactionok, addallowed, removeallowed, saveconfig, reloadconfig
	};

	// Stuff
	public static final Server SERVER = Bukkit.getServer();
	public static final Logger LOG = SERVER.getLogger();
	public static final String CHAT_PREFIX = "AcControl: ";

	// Singleton Constructor should be protected - but we wont do it here and
	// just (must) trust the system .. :P
	/**
	 * Constructor for plugin singleton instance.
	 */
	public P() {
		super();
		plugin = this;
	}

	/**
	 * Info sent to SERVER LOG with level INFO
	 * 
	 * @param text
	 *            Text to LOG
	 */
	public static void info(String text) {
		LOG.log(Level.INFO, P.CHAT_PREFIX.concat(" ").concat(text));
	}

	/**
	 * Info sent to SERVER LOG with level SEVERE
	 * 
	 * @param text
	 *            Text to LOG
	 */
	public static void severe(String text) {
		LOG.log(Level.SEVERE, P.CHAT_PREFIX.concat(" ").concat(text));
	}

	/**
	 * @param recipient
	 *            Player to send chat message to
	 * @param text
	 *            Message text
	 */
	public static void msg(org.bukkit.entity.Player recipient, String text) {
		recipient.sendMessage(P.CHAT_PREFIX.concat(" ").concat(text));
	}

	/**
	 * @param text
	 *            Text to broadcast in SERVER chat
	 */
	public static void broadcast(String text) {
		P.SERVER.broadcastMessage(P.CHAT_PREFIX.concat(" ").concat(text));
	}

	// Autocraft plugin related functions
	/**
	 * @param MyPlayer
	 *            Player to check for being pilot or not
	 * @return true if player is piloting a ship
	 */
	public static boolean isPilot(Player MyPlayer) {
		if (Autocraft.shipmanager.ships.containsKey(MyPlayer.getName())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		// initializing config
		// Config access
		config = new ConfigAccessor(plugin, "config.yml");
		config.reloadConfig();
		// registering Listener Class
		getServer().getPluginManager().registerEvents(new AcListener(), this);
		// This will throw a NullPointException if you don't have the command
		// defined in your plugin.yml file!
		getCommand("accontrol").setExecutor(new cmdExecutor(this));
		info("Plugin enabled !");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {
		saveConfig();
		HandlerList.unregisterAll(this);
		info("Plugin disabled");
	}

}
