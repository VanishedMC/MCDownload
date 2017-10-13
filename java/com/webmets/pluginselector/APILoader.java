package com.webmets.pluginselector;

import org.bukkit.Bukkit;

import be.maximvdw.spigotsite.SpigotSiteCore;
import be.maximvdw.spigotsite.api.SpigotSite;
import be.maximvdw.spigotsite.api.user.exceptions.InvalidCredentialsException;
import be.maximvdw.spigotsite.api.user.exceptions.TwoFactorAuthenticationException;
import net.md_5.bungee.api.ChatColor;

public class APILoader implements Runnable {

	private PluginSelector plugin;

	public APILoader(PluginSelector plugin) {
		this.plugin = plugin;
	}

	public void run() {
		String name = "";
		String password = "";
		String twofa = null;
		if (!plugin.getConfig().contains("spigotName") || !plugin.getConfig().contains("spigotPassword")) {
			System.out.println("Failed to load configuration! shutting down");
			Bukkit.getPluginManager().disablePlugin(plugin);
			return;
		}
		name = plugin.getConfig().getString("spigotName");
		password = plugin.getConfig().getString("spigotPassword");
		if (plugin.getConfig().contains("spigot2FAkey")) {
			if (plugin.getConfig().getString("spigot2FAkey").equalsIgnoreCase("none")) {
				twofa = null;
			} else {
				twofa = plugin.getConfig().getString("spigot2FAkey");
			}
		}
		new SpigotSiteCore();
		try {
			if (twofa == null) {
				plugin.setUser(SpigotSite.getAPI().getUserManager().authenticate(name, password));
			} else {
				plugin.setUser(SpigotSite.getAPI().getUserManager().authenticate(name, password, twofa));
			}
			plugin.setReady(true);
			System.out.println(ChatColor.GREEN + "MCDownload has loaded up!");
		} catch (InvalidCredentialsException e) {
			System.out.println("[InvalidCredentialsException] Failed to load spigot account! shutting down..");
			Bukkit.getPluginManager().disablePlugin(plugin);
			e.printStackTrace();
		} catch (TwoFactorAuthenticationException e) {
			System.out.println("[2FAException] Failed to load spigot account! shutting down..");
			Bukkit.getPluginManager().disablePlugin(plugin);
			e.printStackTrace();
		}
	}

}
