package me.scarday.bungee.configuration;

import lombok.Getter;
import me.scarday.bungee.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Config {


    private String configFile;
    public Main instance;

    public Config(String configFile, Main instance) {
        this.instance = instance;
        this.configFile = configFile;
    }

    @Getter
    public Configuration config;

    public void saveDefaultConfig() {
        try {
            if (!instance.getDataFolder().exists()) {
                instance.getDataFolder().mkdir();
            }

            File file = new File(instance.getDataFolder(), configFile);
            if (!file.exists()) {
                Files.copy(instance.getResourceAsStream(configFile), file.toPath());
            }
            loadConfig();
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }
    public void loadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(instance.getDataFolder(), configFile));
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }
    public void reloadConfig() {
        loadConfig();
    }
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(instance.getDataFolder(), configFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
