package com.example.bookingapp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface BookingInterface {

    @GET("booking") // Метод GET, який отримує усі бронювання за певним запитом ключа
    Call<GetBooking> getAllBooking(
            @Query("key_query") String key_query // Анотація Query для вказівки запиту ключа для методу GET
    );

    @FormUrlEncoded // Анотація FormUrlEncoded для вказівки того, що параметри повинні кодуватися у форматі URL-кодування
    @POST("booking") // Метод POST, який додає нове бронювання до бази даних
    Call<AddBooking> postBooking(
      // Анотації Field для позначення полів, що відповідають вхідним параметрам для POST-запиту
      @Field("android_id") String AndroidID,
      @Field("booking_name") String nameEditText,
      @Field("booking_phone") String phoneEditText,
      @Field("booking_people") Integer NumberOfPeopleValue,
      @Field("booking_datetime") String formattedDateTime,
      @Field("booking_date") String SelectedDate,
      @Field("booking_time") String SelectedTime,
      @Field("booking_comment") String commentEditText
    );

    @FormUrlEncoded // Анотація FormUrlEncoded для вказівки того, що параметри повинні кодуватися у форматі URL-кодування
    @PUT("booking") // Метод PUT, який заміняє змінні бронювання у базі даних
    Call<AddBooking> putBooking(
            // Анотації Field для позначення полів, що відповідають вхідним параметрам для PUT-запиту
            @Field("android_id") String AndroidID,
            @Field("booking_id_old") String BookingID,
            @Field("booking_name") String nameEditText,
            @Field("booking_phone") String phoneEditText,
            @Field("booking_people") Integer NumberOfPeopleValue,
            @Field("booking_datetime") String formattedDateTime,
            @Field("booking_date") String SelectedDate,
            @Field("booking_time") String SelectedTime,
            @Field("booking_comment") String commentEditText
    );


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "Booking", hasBody = true) // Оголошення метода DELETE
    Call<AddBooking> deleteBooking(
            @Field("booking_id") String BookingID); // Відправляє запит на видалення бронювання з вказаним ID
}