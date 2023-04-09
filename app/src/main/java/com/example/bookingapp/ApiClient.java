package com.example.bookingapp;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // Адреса веб-сайта на якому розміщений сервер
    public static final String url_website = "http:/192.168.10.200:8080/restapi-master/";

    // Створення змінної retrofit зі значенням null
    private static Retrofit retrofit = null;

    // Створення об'єкту OkHttpClient, який містить параметри, такі як час очікування на підключення та на читання
    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES).build();

    // Метод який повертає retrofit-клієнта з налаштуваннями, якщо retrofit дорівнює null.
    // Інакше повертається налаштований клієнт Retrofit.
    public static Retrofit getClient() {
        if (retrofit == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setLenient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(url_website)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build();
        }
        return retrofit;
    }
}