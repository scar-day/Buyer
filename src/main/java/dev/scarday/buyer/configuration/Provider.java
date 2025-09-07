package dev.scarday.buyer.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Provider extends OkaeriConfig {
    @Builder.Default boolean enable = false;
    String token;
    @Builder.Default List<Long> ids = List.of();
    @Builder.Default Map<String, Object> options = Map.of(); // под разные параметры...
}
