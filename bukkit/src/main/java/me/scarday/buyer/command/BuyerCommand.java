package me.scarday.buyer.command;

import com.destroystokyo.paper.Title;
import me.clip.placeholderapi.PlaceholderAPI;
import me.scarday.buyer.Main;
import me.scarday.buyer.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuyerCommand implements CommandExecutor, TabCompleter {

    private final Main instance;

    public BuyerCommand(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)  {

        if (!commandSender.hasPermission("buyer.use")) {
            commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.no-permission")));
            return false;
        }

        if (strings.length < 1) {
            commandSender.sendMessage(ColorUtil.colorize(listToString(instance.getConfig().getStringList("messages.help"))));
            return true;
        }

        switch (strings[0].toLowerCase()) {
            case "give":
            case "выдать": {

                if (strings.length < 2) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.no-args-player")));
                    return false;
                }

                Player player = Bukkit.getServer().getPlayer(strings[1].toLowerCase());
                if (player == null) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.player-not-found")));
                    return false;
                }

                ConfigurationSection section = instance.getConfig().getConfigurationSection("groups");
                assert section != null;

                if (strings.length < 3) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.no-args-donate")));
                    return true;
                }

                if (section.getConfigurationSection(strings[2].toLowerCase()) == null) {
                    commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.donate-not-found")));
                    return true;
                }

                ConfigurationSection getDonate = section.getConfigurationSection(strings[2].toLowerCase());
                assert getDonate != null;
                for (Player b : Bukkit.getOnlinePlayers()) {
                    if (getDonate.getBoolean("enable-sound")) {
                        Sound sound = Sound.valueOf(getDonate.getString("sound"));
                        b.playSound(b.getLocation(), sound, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }

                    String msg = PlaceholderAPI.setPlaceholders(player, ColorUtil.colorize(listToString(getDonate.getStringList("broadcast")).replace("%player%", player.getName())));
                    b.sendMessage(msg);

                    for (String list: getDonate.getStringList("give")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), list.replace("%player%", player.getName()).replace("%group%", strings[2].toLowerCase()));
                    }
                }

                if (getDonate.getBoolean("enable-title")) {
                    String message = ColorUtil.colorize(getDonate.getString("title"));
                    String[] parts = message != null ? message.split(";", 2) : new String[0];

                    String title = parts[0];
                    String subtitle = parts.length > 1 ? parts[1] : "";

                    player.getPlayer().sendTitle(new Title(title, subtitle));
                }

                Date now = new Date();
                String format = new SimpleDateFormat(instance.getConfig().getString("settings.pattern-time")).format(now);

                if (instance.getTelegram() != null) {
                    try {
                        instance.getTelegram().sendMessage(listToString(getDonate.getStringList("tg-message")).replace("%player%", player.getName()).replace("%date%", format));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
            case "reload": {
                instance.reloadConfig();
                commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.success-reload")));
                break;
            }
            default: {
                commandSender.sendMessage(ColorUtil.colorize(instance.getConfig().getString("messages.command-not-found")));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();
        if (commandSender.hasPermission("buyer.use")) {
            if (strings.length == 1) {
                completions.add("give");
                completions.add("reload");
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("give")) {
                for (Player player : commandSender.getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            } else if (strings.length == 3) {
                ConfigurationSection section = instance.getConfig().getConfigurationSection("groups");
                if (section != null) {
                    completions.addAll(section.getKeys(false));
                }
            }
        }
        return completions;
    }

    private static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (String s : list) {
            sb.append(s).append("\n");
        }

        return sb.toString();
    }
}