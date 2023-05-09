package com.example.bookingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.MyViewHolder> {
    public static List<Booking> mBookingList; // Список бронювань
    private Context ctx;

    // Конструктор адаптеру та ініціалізація списку бронювань
    public AdapterBooking(List<Booking> mBookingList, Context context) {
        this.mBookingList = mBookingList;
        this.ctx = context;
    }

    // Метод для створення нових ViewHolder для відображення кожного з бронювань
    @NonNull
    @Override
    public AdapterBooking.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_unit, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    // Метод для заполнения данными элементов ViewHolder
    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.MyViewHolder holder, int position) {
        holder.ViewName.setText("Ім'я: " + mBookingList.get(position).getBooking_name());
        holder.ViewPhone.setText("Номер телефону: " + mBookingList.get(position).getBooking_phone());
        holder.ViewPeople.setText("Кількість осіб: " + mBookingList.get(position).getBooking_people());
        holder.ViewDate.setText("Дата бронювання: " + mBookingList.get(position).getBooking_date());
        holder.ViewTime.setText("Час бронювання: " + mBookingList.get(position).getBooking_time());
        holder.ViewComment.setText("Комментар: " + mBookingList.get(position).getBooking_comment());

        String comment = mBookingList.get(position).getBooking_comment();
        if (comment.isEmpty()) {
            holder.ViewComment.setVisibility(View.GONE); // Не відображати ViewComment, якщо коментар порожній
        } else {
            holder.ViewComment.setVisibility(View.VISIBLE); // Відображати ViewComment, якщо коментар не порожній
            holder.ViewComment.setText("Комментар: " + mBookingList.get(position).getBooking_comment());
        }

        Button EditButton = holder.itemView.findViewById(R.id.EditButton);
        Button DeleteButton = holder.itemView.findViewById(R.id.DeleteButton);
        String booking_id = mBookingList.get(position).getBooking_id();

        // Установка обработчика нажатия кнопки EditBooking
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditButtonClick(view, holder.getAdapterPosition(), booking_id, mBookingList.get(position).getBooking_name(), mBookingList.get(position).getBooking_phone(), mBookingList.get(position).getBooking_people(), mBookingList.get(position).getBooking_date(), mBookingList.get(position).getBooking_time(), mBookingList.get(position).getBooking_comment());
            }
        });

        // Установка обработчика нажатия кнопки DeleteBooking
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteButtonClick(view, holder.getAdapterPosition(), booking_id);
            }
        });
    }

    // Метод для обработки нажатия кнопки EditBooking
    private void onEditButtonClick(View view, int position, String booking_id, String Booking_name, String Booking_phone, String Booking_people, String Booking_date, String Booking_time, String Booking_comment) {
        Intent intent_edit = new Intent (ctx,EditBooking.class);
        EditBooking.BookingID_edit = booking_id;
        intent_edit.putExtra("Booking_name",Booking_name);
        intent_edit.putExtra("Booking_phone",Booking_phone);
        intent_edit.putExtra("Booking_people",Booking_people);
        intent_edit.putExtra("Booking_date",Booking_date);
        intent_edit.putExtra("Booking_time",Booking_time);
        intent_edit.putExtra("Booking_comment",Booking_comment);
        ctx.startActivity(intent_edit);
    }

    // Метод для обработки нажатия кнопки DeleteBooking
    private void onDeleteButtonClick(View view, int position, String booking_id) {
        BookingInterface bookingInterface = ApiClient.getClient().create(BookingInterface.class);
        Call<AddBooking> delBooking = bookingInterface.deleteBooking(
                booking_id
        );
        delBooking.enqueue(new Callback<AddBooking>() {
            @Override
            public void onResponse(Call<AddBooking> call, Response<AddBooking> response) {
                Toast.makeText(ctx,"Бронювання видалено", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddBooking> call, Throwable t) {
                Toast.makeText(ctx,"Помилка", Toast.LENGTH_SHORT).show();
            }
        });
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
