package org.collegiumv.BungeeRelay.listeners;

import org.collegiumv.BungeeRelay.IRC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
    Plugin plugin;
    public ChatListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!IRC.sock.isConnected()) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String msg = event.getMessage();
        if (!msg.startsWith("/") || (msg.startsWith("/me "))) {
            if (msg.startsWith("/me ")) msg = "\001ACTION " + msg.substring(4) + "\001";
            IRC.out.println(":" + IRC.players.get(player).id + " PRIVMSG " + IRC.channel + " :" + msg);

            for (ProxiedPlayer o : plugin.getProxy().getPlayers()) {
                if (!player.getServer().getInfo().getName().equals(o.getServer().getInfo().getName())) o.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', IRC.config.getString("formats.msg"))
                        .replace("{SENDER}", player.getName())
                        .replace("{MESSAGE}", msg)));
            }
        }
    }
}
