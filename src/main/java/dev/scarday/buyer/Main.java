package dev.scarday.buyer;

import dev.scarday.buyer.configuration.Configuration;
import dev.scarday.buyer.configuration.ItemsConfiguration;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.AccessLevel;
import lombok.Getter;
import dev.scarday.buyer.http.TelegramHttp;
import lombok.experimental.FieldDefaults;
import okhttp3.OkHttpClient;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Main extends JavaPlugin {

    @Getter
    Configuration configuration;

    @Getter
    ItemsConfiguration itemsConfiguration;

    @Getter
    OkHttpClient httpClient;

    @Getter
    TelegramHttp telegram;

    // TODO: переписать этот кал.

    @Override
    public void onEnable() {
        httpClient = new OkHttpClient();
        getLogger().info("HttpClient initialized!");

        configuration = ConfigManager.create(Configuration.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load(true);
        });

        itemsConfiguration = ConfigManager.create(ItemsConfiguration.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(getDataFolder(), "items.yml"));
            it.saveDefaults();
            it.load(true);
        });

        telegram = new TelegramHttp(this.configuration, httpClient);
    }

    @Override
    public void onDisable() {

    }
}