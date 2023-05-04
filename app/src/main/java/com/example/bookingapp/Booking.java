package com.example.bookingapp;

import com.google.gson.annotations.SerializedName;

public class Booking {

    // Оголошення полів об'єктів бронювання
    @SerializedName("booking_id")
    private String booking_id;

    @SerializedName("booking_name")
    private String booking_name;

    @SerializedName("booking_phone")
    private String booking_phone;

    @SerializedName("booking_people")
    private String booking_people;

    @SerializedName("booking_date")
    private String booking_date;

    @SerializedName("booking_time")
    private String booking_time;

    @SerializedName("booking_comment")
    private String booking_comment;

    // Оголошення методів геттерів і сеттерів для кожного об'єкту бронювання
    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_name() {
        return booking_name;
    }

    public void setBooking_name(String booking_name) {
        this.booking_name = booking_name;
    }

    public String getBooking_phone() {
        return booking_phone;
    }

    public void setBooking_phone(String booking_phone) {
        this.booking_phone = booking_phone;
    }

    public String getBooking_people() {
        return booking_people;
    }

    public void setBooking_people(String booking_people) {
        this.booking_people = booking_people;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_comment() {
        return booking_comment;
    }

    public void setBooking_comment(String booking_comment) {
        this.booking_comment = booking_comment;
    }
}
