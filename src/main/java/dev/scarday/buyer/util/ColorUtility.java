package dev.scarday.buyer.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ColorUtility {
    public static MiniMessage MM = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    public static Component colorize(@NotNull String message) {
        return MM.deserialize(message);
    }

    public static String colorize(@NotNull Component component) {
        return LEGACY_SERIALIZER.serialize(component);
    }
}