package me.scarday.buyer.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

@UtilityClass
public class ColorUtil {

    public String colorize(String message) {

        if (message == null) {
            return message;
        }

        Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F\\d]{6})");
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4*8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer,
                    COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR
                            + group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4)
                            + COLOR_CHAR + group.charAt(5));
        }
        message = matcher.appendTail(buffer).toString();
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

}