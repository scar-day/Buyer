package me.scarday.bungee;

import lombok.Getter;
import me.scarday.bungee.command.BuyerCommand;
import me.scarday.bungee.configuration.Config;
import me.scarday.bungee.http.TelegramHttp;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public final class Main extends Plugin {

    private Main instance;

    private TelegramHttp telegram;

    private Config config;

    @Override
    public void onEnable() {
        config = new Config("config.yml", this);
        config.saveDefaultConfig();

        if (config.config.getBoolean("settings.tg.enable")) {
            telegram = new TelegramHttp(config.config.getStringList("settings.tg.chat-id"), config.config.getString("settings.tg.token"), this);
            getLogger().info("Интеграция с Telegram включена");
        } else {
            getLogger().info("Интеграция с Telegram выключена");
        }
        
        getProxy().getPluginManager().registerCommand(this, new BuyerCommand(this));
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterCommand(new BuyerCommand());
    }

}
