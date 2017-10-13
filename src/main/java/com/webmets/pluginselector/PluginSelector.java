package com.webmets.pluginselector;

import static net.md_5.bungee.api.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import be.maximvdw.spigotsite.api.user.User;

public class PluginSelector extends JavaPlugin {

	public User u = null;
	public boolean ready = false;
	public boolean busy = false;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		Bukkit.getScheduler().runTaskAsynchronously(this, new APILoader(this));
		getCommand("download").setExecutor(this);
		getCommand("findpl").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("download")) {
			if (!ready) {
				sender.sendMessage(RED + "Not ready for usage yet!");
				return true;
			}
			if (isBusy()) {
				sender.sendMessage(RED + "I am still downloading a plugin, please wait!");
				return true;
			}
			if (args.length <= 0) {
				sender.sendMessage(RED + "Usage: /download <plugin id> <should enable> [name]\n" + RED
						+ "- Downloads the plugin with given id from spigotmc and enables if specified. Saves to the given filename (id.jar if no name specified)");
				return true;
			} else {
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(RED + "Please enter a valid id!");
					return true;
				}
				if (id == -1) {
					sender.sendMessage(RED + "Invalid id!");
					return true;
				}
				if(args.length==1) {
					sender.sendMessage(RED + "Usage: /download <id> <true/false> [filename]\n id=id for the plugin. true/false=should the plugin be loaded, or only be downloaded. filename=optional, outfile name");
					return true;
				}else if (args.length == 2) {
					boolean load = false;
					try {
						load = Boolean.parseBoolean(args[1]);
					} catch (Exception e) {
						sender.sendMessage(RED + "Usage: /download <id> <true/false> [filename]\n id=id for the plugin. true/false=should the plugin be loaded, or only be downloaded. filename=optional, outfile name");
						return true;
					}
					Bukkit.getScheduler().runTaskAsynchronously(this, new Downloader(id, load, this));
					return true;
				} else if (args.length == 3) {
					boolean load = false;
					try {
						load = Boolean.parseBoolean(args[1]);
					} catch (Exception e) {
						sender.sendMessage(RED + "Usage: /download <id> <true/false> [filename]\n id=id for the plugin. true/false=should the plugin be loaded, or only be downloaded. filename=optional, outfile name");
						return true;
					}
					Bukkit.getScheduler().runTaskAsynchronously(this, new Downloader(id, load, this, args[2]));
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("findpl")) {
			if (!ready) {
				sender.sendMessage(RED + "Not ready for usage yet!");
				return true;
			}
			if (isBusy()) {
				sender.sendMessage(RED + "I am still downloading a plugin, please wait!");
				return true;
			}
			return true;
		}

		return true;
	}

	public User getUser() {
		return u;
	}

	public void setUser(User u) {
		this.u = u;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isBusy() {
		return busy;
	}
}
