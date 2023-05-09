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
      @Field("booking_name") String nameEditText,
      @Field("booking_phone") String phoneEditText,
      @Field("booking_people") Integer NumberOfPeopleValue,
      @Field("booking_date") String SelectedDate,
      @Field("booking_time") String SelectedTime,
      @Field("booking_comment") String commentEditText
    );

    @FormUrlEncoded // Анотація FormUrlEncoded для вказівки того, що параметри повинні кодуватися у форматі URL-кодування
    @PUT("booking") // Метод PUT
    Call<AddBooking> putBooking(
            // Анотації Field для позначення полів, що відповідають вхідним параметрам для PUT-запиту
            @Field("booking_id_old") String BookingID,
            @Field("booking_name") String nameEditText,
            @Field("booking_phone") String phoneEditText,
            @Field("booking_people") Integer NumberOfPeopleValue,
            @Field("booking_date") String SelectedDate,
            @Field("booking_time") String SelectedTime,
            @Field("booking_comment") String commentEditText
    );


    @FormUrlEncoded // Анотація FormUrlEncoded для вказівки того, що параметри повинні кодуватися у форматі URL-кодування
    @HTTP(method = "DELETE", path = "Booking", hasBody = true)
    Call<AddBooking> deleteBooking(
            // Анотації Field для позначення полів, що відповідають вхідним параметрам для PUT-запиту
            @Field("booking_id") String BookingID);
}