package com.example.bookingapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBooking {

    // Оголошуення змінних для статусу, результату та повідомлення відповіді сервера
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Booking> listBooking;
    @SerializedName("message")
    String message;

    // Оголошення методів геттерів і сеттерів для кожної змінної
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Booking> getListBooking() {
        return listBooking;
    }

    public void setListBooking(List<Booking> listBooking) {
        this.listBooking = listBooking;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
