package me.scarday.bungee.command;

import me.scarday.bungee.Main;
import me.scarday.bungee.util.ColorUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuyerCommand extends Command implements TabExecutor {

    private Main instance;
    public BuyerCommand(Main instance) {
        this();
        this.instance = instance;
    }

    public BuyerCommand() {
        super("buyer", "buyer.use", "покупка");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("Нельзя команду от консоли использовать");
            return;
        }

        if (!commandSender.hasPermission("buyer.use")) {
            commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.no-permission")));
            return;
        }

        if (strings.length < 1) {
            commandSender.sendMessage(ColorUtil.colorize(joinToString(instance.getConfig().getConfig().getStringList("messages.help"))));
            return;
        }

        switch (strings[0].toLowerCase()) {
            case "give":
            case "выдать": {

                if (strings.length < 2) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.no-args-player")));
                    return;
                }

                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(strings[1].toLowerCase());
                if (player == null) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.player-not-found")));
                    return;
                }

                Configuration section = instance.getConfig().getConfig().getSection("groups");
                assert section != null;

                if (strings.length < 3) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.no-args-donate")));
                    return;
                }

                if (section.getSection(strings[2].toLowerCase()) == null) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.donate-not-found")));
                    return;
                }

                Configuration getDonate = section.getSection(strings[2].toLowerCase());
                assert getDonate != null;

                for (ProxiedPlayer b : ProxyServer.getInstance().getPlayers()) {
                    String message = ColorUtil.colorize(joinToString(getDonate.getStringList("broadcast")).replace("%player%", player.getName()));
                    b.sendMessage(message);
                    instance.getProxy().getPluginManager().dispatchCommand(instance.getProxy().getConsole(), joinToString(getDonate.getStringList("give")).replace("%player%", player.getName()).replace("%group%", strings[2].toLowerCase()));
                }

                Date now = new Date();
                String format = new SimpleDateFormat(instance.getConfig().getConfig().getString("settings.pattern-time")).format(now);

                if (instance.getTelegram() != null) {
                    try {
                        instance.getTelegram().sendMessage(joinToString(getDonate.getStringList("tg-message")).replace("%player%", player.getName()).replace("%date%", format));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
            case "reload": {
                instance.getConfig().reloadConfig();
                commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.success-reload")));
                break;
            }
            default: {
                commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getConfig().getString("messages.command-not-found")));
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        List<String> completions = new ArrayList<>();
        if (commandSender.hasPermission("buyer.use")) {
            if (strings.length == 1) {
                completions.add("give");
                completions.add("reload");
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("give")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    completions.add(player.getName());
                }
            } else if (strings.length == 3) {
                Configuration section = instance.getConfig().getConfig().getSection("groups");
                if (section != null) {
                    completions.addAll(section.getKeys());
                }
            }
        }
        return completions;
    }

    private String joinToString(List<String> list) {
        return String.join("\n", list);
    }
}
