package dev.scarday.buyer.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.scarday.buyer.configuration.Configuration;
import dev.scarday.buyer.configuration.ItemsConfiguration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.bukkit.entity.Player;

import static dev.scarday.buyer.util.ColorUtility.colorize;

@Command(name = "buyer")
@Permission("buyer.access")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BuyerCommand {
    Configuration configuration;
    ItemsConfiguration itemsConfiguration;

    @Permission("buyer.reload")
    void reload(@Context Player player) {
        configuration.load();
        itemsConfiguration.load();

        player.sendMessage(colorize(configuration.getMessages().getReloaded()));
    }
}
