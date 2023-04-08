package com.example.bookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Ініціалізація змінних
    Button buttonNewBooking;
    TextView textViewBookings;
    TextView textViewNoBookings;
    Button buttonClearBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Оголошення змінних
        buttonNewBooking = findViewById(R.id.buttonNewBooking);
        textViewBookings = findViewById(R.id.textViewBookings);
        textViewNoBookings = findViewById(R.id.textViewNoBookings);
        buttonClearBookings = findViewById(R.id.buttonClearBookings);

        // Отримання збереженних значень
        SharedPreferences sharedPreferences = getSharedPreferences("BOOKINGS", MODE_PRIVATE);
        // Перевірка, чі порожній файл з бронюваннями або ні
        if (sharedPreferences.getAll().isEmpty() || !sharedPreferences.contains("NAME_0")) {
            textViewNoBookings.setVisibility(View.VISIBLE);
            textViewBookings.setVisibility(View.GONE);
            buttonClearBookings.setVisibility(View.GONE);
            return;
        } else {
            textViewNoBookings.setVisibility(View.GONE);
            textViewBookings.setVisibility(View.VISIBLE);
            buttonClearBookings.setVisibility(View.VISIBLE);
        }

        // Отримання останнього збереженого індексу
        int lastIndex = sharedPreferences.getInt("LAST_INDEX", 0);

        // Створення рядка для об'єднання усіх раніше створених рядків
        StringBuilder bookingsStringBuilder = new StringBuilder();

        // Отримання значень для кожного ключа
        for (int i = 0; i <= lastIndex; i++) {
            String name = sharedPreferences.getString("NAME_" + i, "");
            String phone = sharedPreferences.getString("PHONE_" + i, "");
            int people = sharedPreferences.getInt("PEOPLE_" + i, 0);
            String date = sharedPreferences.getString("DATE_" + i, "");
            String time = sharedPreferences.getString("TIME_" + i, "");

            // Створення новії строки для поточного бронювання
            StringBuilder bookingStringBuilder = new StringBuilder();

            // Побудова рядка із отриманних значень
            bookingStringBuilder.append("Ім'я: ").append(name).append("\n");
            bookingStringBuilder.append("Номер телефону: ").append(phone).append("\n");
            bookingStringBuilder.append("Кількість людей: ").append(people).append("\n");
            bookingStringBuilder.append("Дата бронювання: ").append(date).append("\n");
            bookingStringBuilder.append("Час бронювання: ").append(time).append("\n\n");

            // Додавання поточне бронювання до загального списку бронювань
            bookingsStringBuilder.append(bookingStringBuilder.toString());
        }

        // Перевіряємо, чи є бронювання
        if (bookingsStringBuilder.length() == 0) {
            textViewNoBookings.setVisibility(View.VISIBLE);
            textViewBookings.setVisibility(View.GONE);
            buttonClearBookings.setVisibility(View.GONE);
        } else {
            textViewNoBookings.setVisibility(View.GONE);
            textViewBookings.setVisibility(View.VISIBLE);
            buttonClearBookings.setVisibility(View.VISIBLE);
        }

        // Встановлення рядка для його відображення
        textViewBookings.setText(bookingsStringBuilder.toString());

        // Додавання обробника для кнопки очищення бронювань
        buttonClearBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Видалення файлу зі список бронювань
                SharedPreferences sharedPreferences = getSharedPreferences("BOOKINGS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Оновлення відображення тексту та кнопок
                textViewNoBookings.setVisibility(View.VISIBLE);
                textViewBookings.setVisibility(View.GONE);
                buttonClearBookings.setVisibility(View.GONE);
                textViewBookings.setText("");

                // Виведення повідомлення про очищення бронювань
                Toast.makeText(MainActivity.this, "Усі бронювання видалено", Toast.LENGTH_SHORT).show();
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
}