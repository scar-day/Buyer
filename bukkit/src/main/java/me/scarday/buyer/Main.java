package me.scarday.buyer;

import lombok.Getter;
import me.scarday.buyer.command.BuyerCommand;
import me.scarday.buyer.http.TelegramHttp;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    private TelegramHttp telegram;

    @Override
    public void onEnable() {
        if (getConfig().getBoolean("settings.tg.enable")) {
            telegram = new TelegramHttp(getConfig().getStringList("settings.tg.chat-id"), getConfig().getString("settings.tg.token"));
            getLogger().info("Интеграция с Telegram включена");
        } else {
            getLogger().info("Интеграция с Telegram выключена");
        }

        saveDefaultConfig();
        getCommand("buyer").setExecutor(new BuyerCommand(this));
        getCommand("buyer").setTabCompleter(new BuyerCommand(this));
    }

    @Override
    public void onDisable() {

    }




}