package com.example.bookingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Ініціалізація змінних
    Button buttonNewBooking, buttonUpdateBookings;
    TextView textViewNoBookings;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    BookingInterface mBookingInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Оголошення змінних
        buttonNewBooking = findViewById(R.id.buttonNewBooking);
        textViewNoBookings = findViewById(R.id.textViewNoBookings);
        buttonUpdateBookings = findViewById(R.id.buttonUpdateBookings);
        recyclerView = findViewById(R.id.ViewBookings);

        mlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setHasFixedSize(true);
        getAllBooking();
    }

    // Викликаємо функцію для отримання всіх бронювань з сервера
    private void getAllBooking() {
        mBookingInterface = ApiClient.getClient().create(BookingInterface.class); //Оброблення результатів запиту
        Call<GetBooking> bookingCall = mBookingInterface.getAllBooking("all");
        bookingCall.enqueue(new Callback<GetBooking>() {
            @Override
            public void onResponse(Call<GetBooking> call, Response<GetBooking> response) {
                // Отримання списку юронювань з відповіді сервера
                List<Booking> bookingList = response.body().getListBooking();
                // Створення адаптеру для відображення списку бронювань
                mAdapter = new AdapterBooking(bookingList, MainActivity.this);
                recyclerView.setAdapter(mAdapter);

                // Перевірка, чі є бронювання або ні
                if (bookingList == null || bookingList.size() == 0) {
                    textViewNoBookings.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    textViewNoBookings.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetBooking> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Помилка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Перевірка на наявність підключення до інтернету
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Дії при натисканні на кнопку "Нове бронювання"
    public void NewBookingClick(View view) {
        if (MainActivity.isNetworkConnected(this)) {
            Intent intent = new Intent(MainActivity.this, NewBooking.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Відсутнє підключення до інтернету", Toast.LENGTH_SHORT).show();
        }
    }


    // Дії при натисканні на кнопку "Нове бронювання"
    public void UpdateBookingClick(View view) {
        if (MainActivity.isNetworkConnected(this)) {
            getAllBooking();
            Toast.makeText(MainActivity.this, "Бронювання оновлені", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Відсутнє підключення до інтернету", Toast.LENGTH_SHORT).show();
        }
    }
}