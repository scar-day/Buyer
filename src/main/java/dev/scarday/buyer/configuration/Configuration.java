package dev.scarday.buyer.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration extends OkaeriConfig {
    Settings settings = new Settings();
    Messages messages = new Messages();

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Settings extends OkaeriConfig {
        String patternTime = "dd-MM-yyyy HH:mm:ss";
        Provider telegram = Provider.builder()
                .build();
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Messages extends OkaeriConfig {
        String noPermission = "<red>У вас недостаточно прав для использования данной команды";

        List<String> usage = List.of("<reset>", "<green>/buyer give <username> <donate>", "/buyer reload", "<reset>");
        String donateNotFound = "<red>Донат не найден";
        String playerNotFound = "<red>Игрок не найден";
        String reloaded = "<green>Конфигурация успешно перезагружена!";
        String incorrectUsage = "<red>Неправильное использование команды!";
    }

}
