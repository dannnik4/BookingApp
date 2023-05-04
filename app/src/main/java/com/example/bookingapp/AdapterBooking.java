package com.example.bookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.MyViewHolder> {
    public static List<Booking> mBookingList; // Список бронювань
    private Context context;

    // Конструктор адаптеру та ініціалізація списку бронювань
    public AdapterBooking(List<Booking> mBookingList, MainActivity mainActivity) {
        this.mBookingList = mBookingList;
        this.context = context;
    }

    // Метод для створення нових ViewHolder для відображення кожного з бронювань
    @NonNull
    @Override
    public AdapterBooking.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_unit, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    // Метод для заповнення даними елементів ViewHolder
    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.MyViewHolder holder, int position) {
        holder.ViewName.setText("Ім'я: " + mBookingList.get(position).getBooking_name());
        holder.ViewPhone.setText("Номер телефону: " + mBookingList.get(position).getBooking_phone());
        holder.ViewPeople.setText("Кількість людей: " + mBookingList.get(position).getBooking_people());
        holder.ViewDate.setText("Дата бронювання: " + mBookingList.get(position).getBooking_date());
        holder.ViewTime.setText("Час бронювання: " + mBookingList.get(position).getBooking_time());
        holder.ViewComment.setText("Комментар: " + mBookingList.get(position).getBooking_comment());
    }

    // Повернення кількісті елементів у списку бронювань
    @Override
    public int getItemCount() {
        return mBookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView ViewName, ViewPhone, ViewPeople, ViewDate, ViewTime,ViewComment; // Елементи макету
        public MyViewHolder(View bookingView){ // Конструктор ViewHolder
            super(bookingView);
            // Ініціалізація елементів з макету
            ViewName = bookingView.findViewById(R.id.ViewName);
            ViewPhone = bookingView.findViewById(R.id.ViewPhone);
            ViewPeople = bookingView.findViewById(R.id.ViewPeople);
            ViewDate = bookingView.findViewById(R.id.ViewDate);
            ViewTime = bookingView.findViewById(R.id.ViewTime);
            ViewComment = bookingView.findViewById(R.id.ViewComment);
        }
    }
}
