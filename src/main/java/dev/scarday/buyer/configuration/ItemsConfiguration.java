package dev.scarday.buyer.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemsConfiguration extends OkaeriConfig {
    Map<String, String> items = new ConcurrentHashMap<>();

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class Item extends OkaeriConfig {
       List<String> commands = List.of();
    }
}
