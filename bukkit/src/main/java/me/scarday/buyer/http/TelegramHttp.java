package me.scarday.buyer.http;

import lombok.experimental.UtilityClass;
import me.scarday.buyer.Main;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TelegramHttp {

    private Main instance;

    private final List<String> ids;

    private final String token;

    public TelegramHttp(@NotNull List<String> ids, String token) {
        this.ids = ids;
        this.token = token;
    }

    public void sendMessage(String text) {
        CompletableFuture.runAsync(() -> {
            for (String chatId : ids) {
                try {
                    String urlString = "https://api.telegram.org/bot" + token + "/sendMessage?text=" + URLEncoder.encode(text, "UTF-8") + "&chat_id=" + chatId;
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder jsonResponse = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            jsonResponse.append(inputLine);
                        }
                        in.close();

                        Bukkit.getLogger().info("Произошла ошибка при отправке сообщения: " + jsonResponse);
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    Bukkit.getLogger().info("Произошла ошибка при отправке сообщения: " + e);
                }
            }
        });
    }


}