package com.example.bookingapp;

import com.google.gson.annotations.SerializedName;

public class AddBooking {

    // Оголошення змінних для статусу, результату та повідомлення відповіді сервера
    @SerializedName("status")
    String status;
    @SerializedName("result")
    Booking Booking;
    @SerializedName("message")
    String message;

    // Оголошення методів геттерів і сеттерів для кожної змінної
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public com.example.bookingapp.Booking getBooking() {
        return Booking;
    }

    public void setBooking(com.example.bookingapp.Booking booking) {
        Booking = booking;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
