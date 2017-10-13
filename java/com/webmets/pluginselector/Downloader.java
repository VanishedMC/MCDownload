package com.webmets.pluginselector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import be.maximvdw.spigotsite.api.SpigotSite;
import be.maximvdw.spigotsite.api.resource.Resource;
import net.md_5.bungee.api.ChatColor;

public class Downloader implements Runnable {

	private int id;
	private PluginSelector plugin;
	String name;
	private boolean enable = false;

	public Downloader(int id, boolean load, PluginSelector plugin) {
		this.id = id;
		this.plugin = plugin;
		this.name = null;
		this.enable = load;
	}

	public Downloader(int id, boolean load, PluginSelector plugin, String name) {
		this.id = id;
		this.plugin = plugin;
		this.name = name;
		this.enable = load;
	}

	public void run() {
		plugin.setBusy(true);
		File f = null;
		if (name == null) {
			f = new File(plugin.getDataFolder().getParentFile(), id + ".jar");
		} else {
			if (name.endsWith(".jar")) {
				f = new File(plugin.getDataFolder().getParentFile(), name);
			} else {
				f = new File(plugin.getDataFolder().getParentFile(), name + ".jar");
			}
		}
		broadcastOp(ChatColor.GREEN + "Searching for plugins with the id " + id);
		Resource resource = null;
		try {
			resource = SpigotSite.getAPI().getResourceManager().getResourceById(id);
		} catch (Exception e) {
			broadcastOp(ChatColor.RED + "There was no plugin found with the given ID " + ChatColor.DARK_RED + id);
			plugin.setBusy(false);
			return;
		}
		if (resource == null || resource.getResourceName() == null || resource.getResourceName().equalsIgnoreCase("")) {
			broadcastOp(ChatColor.RED + "There was no plugin found with the given ID " + ChatColor.DARK_RED + id);
			plugin.setBusy(false);
			return;
		}
		broadcastOp(ChatColor.GREEN + "Plugin found: " + resource.getResourceName() + ", starting download..");
		try {
			resource.downloadResource(plugin.getUser(), f);
		} catch (Exception e) {
			broadcastOp(ChatColor.RED + "Failed to download the plugin " + id);
			plugin.setBusy(false);
			return;
		}
		if (name == null) {
			broadcastOp(ChatColor.GREEN + "Succesfully downloaded plugin " + ChatColor.DARK_GREEN + id);
		} else {
			broadcastOp(ChatColor.GREEN + "Succesfully downloaded plugin\n id:" + ChatColor.DARK_GREEN + id
					+ ChatColor.GREEN + " file:" + ChatColor.DARK_GREEN + f.getName());
		}

		if(enable){
			try {
				Plugin pl = Bukkit.getPluginManager().loadPlugin(f);
				Bukkit.getPluginManager().enablePlugin(pl);
			} catch (UnknownDependencyException e) {
				e.printStackTrace();
			} catch (InvalidPluginException e) {
				e.printStackTrace();
			} catch (InvalidDescriptionException e) {
				e.printStackTrace();
			}
		}
		plugin.setBusy(false);
	}

	public void broadcastOp(String msg) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.isOp()) {
				p.sendMessage(msg);
			}
		}
		System.out.println(msg);
	}

	public boolean doesResourceExist(int id) {
		try {

			URL url = new URL("https://www.spigotmc.org/resources/" + id + "/");
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String code = "", line = "";

			while ((line = br.readLine()) != null) {
				code = code + line;
			}
			System.out.println(code.split("<title>")[1].split("</title>")[0]);
			return code.split("<title>")[1].split("</title>")[0].contains("error");
		} catch (IOException e) {
			return false;
		}
	}
}
