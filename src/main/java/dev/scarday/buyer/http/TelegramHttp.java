package dev.scarday.buyer.http;

import dev.scarday.buyer.configuration.Configuration;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class TelegramHttp {
    private static final String API_URL = "https://api.telegram.org/bot%s/sendMessage";

    private static final String CHAT_ID_FIELD = "chat_id";
    private static final String TEXT_FIELD = "text";

    private final Configuration configuration;
    private final OkHttpClient client;

    public void sendMessage(String message) {
        String token = configuration.getSettings().getTelegram().getToken();
        String url = String.format(API_URL, token);

        for (long userId : configuration.getSettings().getTelegram().getIds()) {
            HttpUrl httpUrl = HttpUrl.parse(url)
                    .newBuilder()
                    .addQueryParameter(CHAT_ID_FIELD, String.valueOf(userId))
                    .addQueryParameter(TEXT_FIELD, message)
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.printf("Не удалось исполнить запрос: %s", response.message());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}