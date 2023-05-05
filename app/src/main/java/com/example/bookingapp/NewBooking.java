package com.example.bookingapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBooking extends AppCompatActivity {

    //Отримання поточної дати та часу
    Calendar dateAndTime = Calendar.getInstance();

    // Ініціалізація змінних
    TextView NumberOfPeopleValue, SelectedDate, SelectedTime;
    SeekBar NumberOfPeopleSeekBar;
    Button selectTimeButton;
    BookingInterface bookingInterface;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_booking);

        // Оголошення змінних для обирання кількості осіб
        NumberOfPeopleSeekBar = findViewById(R.id.NumberOfPeopleSeekBar);
        NumberOfPeopleValue = findViewById(R.id.NumberOfPeopleValue);
        NumberOfPeopleValue.setText(String.valueOf(NumberOfPeopleSeekBar.getProgress()));

        // Обробники натискань на слайдер обирання кількості осіб
        NumberOfPeopleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar NumberOfPeopleSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    progress = 1;
                    NumberOfPeopleSeekBar.setProgress(progress);
                }
                NumberOfPeopleValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar NumberOfPeopleSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar NumberOfPeopleSeekBar) {
            }
        });

        //Оголошення змінних для дати та часу
        SelectedDate = findViewById(R.id.SelectedDate);
        SelectedTime = findViewById(R.id.SelectedTime);

        // Встановлення початкової дати та часу
        setInitialDate();
        setInitialTime();

        // Оголошення та обробник натискання на кнопку часу
        selectTimeButton = findViewById(R.id.TimeButton);
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

    }

    // Відображення діалогового вікна для вибору дати
    public void setDate(View v) {
        new DatePickerDialog(NewBooking.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // Встановлення початкової дати
    private void setInitialDate() {
        Calendar now = Calendar.getInstance();
        // Якщо поточний час пізніше 20:00
        if (now.get(Calendar.HOUR_OF_DAY) >= 20) {
            // Встановити дату на наступний день
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        SelectedDate.setText(DateUtils.formatDateTime(this,
                now.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    // Встановлення початкового часу
    private void setInitialTime() {
        Calendar now = Calendar.getInstance();
        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        // Якщо поточний час пізніше 20:00 або раніше 8:00
        if (hourOfDay >= 20 || hourOfDay < 8 || (hourOfDay == 8 && minute < 0)) {
            // Встановити час на 8:00
            now.set(Calendar.HOUR_OF_DAY, 8);
            now.set(Calendar.MINUTE, 0);

            // Якщо поточний час пізніше 20:00, встановити дату на наступний день
            if (hourOfDay >= 20) {
                now.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            // Встановити годину бронювання на найближчі 2 години від години на момент початку бронювання
            now.set(Calendar.MINUTE, 0);
            now.add(Calendar.HOUR_OF_DAY, 2);
        }

        // Встановити час в текстове поле та у змінну dateAndTime
        SelectedTime.setText(DateUtils.formatDateTime(this,
                now.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
        dateAndTime.setTimeInMillis(now.getTimeInMillis());
    }

    // Встановлення оброблювача вибору дати
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Створення календаря з обраною датою
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);

            // Порівняння вибраної дати з поточною датою
            Calendar currentDate = Calendar.getInstance();

            // Перевіряємо, чи є обрана дата раніше поточної дати, і якщо так, то виводимо повідомлення
            if (selectedDate.before(currentDate)) {
                // Якщо вибрана дата вже пройшла, показуємо повідомлення про помилку
                Toast.makeText(NewBooking.this, "Оберіть майбутню дату", Toast.LENGTH_SHORT).show();
            } else {
                // Перевіряємо, чи є обрана дата поточною датою і час пізніше або дорівнює 20:00 і якщо так, тоді виводимо повідомлення
                if (selectedDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) && selectedDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) && selectedDate.get(Calendar.HOUR_OF_DAY) >= 20) {
                    Toast.makeText(NewBooking.this, "Оберіть майбутню дату", Toast.LENGTH_SHORT).show();
                } else {
                    // Інакше оновлюємо дату
                    dateAndTime.set(Calendar.YEAR, year);
                    dateAndTime.set(Calendar.MONTH, monthOfYear);
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    // Форматуємо дату у відповідний вигляд та встановлюємо її в SelectedDate
                    String formattedDate = DateUtils.formatDateTime(NewBooking.this,
                            dateAndTime.getTimeInMillis(),
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                    SelectedDate.setText(formattedDate);
                }
            }
        }
    };

    // Створення віджету для обирання годин та хвилин
    private void showTimePickerDialog() {

        // Отримання поточного часу
        String currentTimeString = SelectedTime.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(currentTimeString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final NumberPicker hourPicker = new NumberPicker(this);
        hourPicker.setMinValue(8);
        hourPicker.setMaxValue(20);
        hourPicker.setValue(Calendar.HOUR_OF_DAY);

        final NumberPicker minutePicker = new NumberPicker(this);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(5);
        minutePicker.setDisplayedValues(new String[]{"00", "10", "20", "30", "40", "50"});
        minutePicker.setValue(Calendar.MINUTE / 10);

        // Створення макету для відображення віджету
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        // Пустий об'єкт для вирівнювання віджету по центру
        View spacer1 = new View(this);
        spacer1.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));
        layout.addView(spacer1);

        layout.addView(hourPicker);
        layout.addView(minutePicker);

        // Ще один пустий об'єкт для вирівнювання віджету по центру
        View spacer2 = new View(this);
        spacer2.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));
        layout.addView(spacer2);

        // Встановлення значень у віджет
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minutePicker.setValue(calendar.get(Calendar.MINUTE) / 10);

        // Створення діалогового вікна
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Оберіть час:");
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Отримання значень вибраних годин та хвилин
                int hourOfDay = hourPicker.getValue();
                int minute = minutePicker.getValue() * 10;

                // Перевірка, що обраний час знаходиться в майбутньому
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);

                Calendar currentDate = Calendar.getInstance();
                if (dateAndTime.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                        dateAndTime.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                        dateAndTime.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) {
                    // Якщо обрано поточний день, то перевіряємо, що час ще не минув
                    if (selectedTime.before(Calendar.getInstance())) {
                        Toast.makeText(NewBooking.this, "Оберить майбутній час", Toast.LENGTH_SHORT).show();
                    } else {
                        // Встановлення вибраного часу
                        String timeString = String.format("%02d:%02d", hourOfDay, minute);
                        SelectedTime.setText(timeString);
                    }
                } else {
                    // Якщо обрано майбутній день, то встановлюємо обраний час
                    String timeString = String.format("%02d:%02d", hourOfDay, minute);
                    SelectedTime.setText(timeString);
                }
            }
        });
        builder.setNegativeButton("Відмінити", null);
        builder.show();
    }

    // Дії при натисканні на кнопку "Зберегти бронювання"
    public void SaveButtonClick(View view) {
        EditText nameEditText = findViewById(R.id.NameEditText);
        String name = nameEditText.getText().toString().trim();

        EditText phoneEditText = findViewById(R.id.PhoneEditText);
        String phone = phoneEditText.getText().toString().trim();

        EditText commentEditText = findViewById(R.id.CommentEditText);
        String comment = commentEditText.getText().toString().trim();

        // Перевірка, чи заповнені ім'я та номер телефону та наявність підключення до інтернету
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show();
            return;
        } if (MainActivity.isNetworkConnected(view.getContext())) {
        } else {
            Toast.makeText(NewBooking.this, "Відсутнє підключення до інтернету", Toast.LENGTH_SHORT).show();
            return;
        }

        // Створення діалогу для перевірки введених даних
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Перевірте бронювання");

        // Створення тексту для діалогу
        String message = "Ім'я: <b>" + name + "</b>" + "<br>" +
                "Номер телефону: <b>" + phone + "</b>" + "<br>" +
                "Кількість людей: <b>" + NumberOfPeopleValue.getText().toString() + "</b>" + "<br>" +
                "Дата бронювання: <b>" + SelectedDate.getText().toString() + "</b>" + "<br>" +
                "Час бронювання: <b>" + SelectedTime.getText().toString() + "</b>" + "<br>" +
                "Комментар: <b>" + comment + "</b>";

        // Пераметри форматування тексту
        TextView textView = new TextView(this);
        textView.setText(Html.fromHtml(message));
        textView.setTextSize(20);
        textView.setPadding(20, 20, 20, 20);

        // Встановлення тексту діалогу у змінну
        builder.setView(textView);

        // Встановлюємо кнопку "OK" для закриття діалогу
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Дії при натисканні на кнопку "OK"
                bookingInterface = ApiClient.getClient().create(BookingInterface.class);
                Call<AddBooking> postPutGetDelBooking = bookingInterface.postBooking(
                  nameEditText.getText().toString(),
                  phoneEditText.getText().toString(),
                  Integer.parseInt(NumberOfPeopleValue.getText().toString()),
                  SelectedDate.getText().toString(),
                  SelectedTime.getText().toString(),
                  commentEditText.getText().toString()
                );
                postPutGetDelBooking.enqueue(new Callback<AddBooking>() {
                    @Override
                    public void onResponse(Call<AddBooking> call, Response<AddBooking> response) {
                        Toast.makeText(NewBooking.this,"Бронювання збережено", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewBooking.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<AddBooking> call, Throwable t) {
                        Toast.makeText(NewBooking.this,"Помилка", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Скасувати", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // При натисканні на кнопку "Відмінити" закриваемо діалог та залишаємося на поточній сторінці
                dialog.cancel();
            }
        });

        // Створюємо та відображаємо діалог
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // При натисканні на кнопку "Відмінити бронювання" повертаємося до начальної сторінки та закриваємо поточну сторінку
    public void CancelButtonClick(View view) {
        Intent intent = new Intent(NewBooking.this, MainActivity.class);
        startActivity(intent);
    }

}