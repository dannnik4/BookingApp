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

public class EditBooking extends AppCompatActivity {

    //Отримання поточної дати та часу
    Calendar dateAndTime = Calendar.getInstance();

    // Ініціалізація змінних
    EditText nameEditText, phoneEditText, commentEditText;
    TextView NumberOfPeopleValue, SelectedDate, SelectedTime;
    SeekBar NumberOfPeopleSeekBar;
    Button selectTimeButton;
    private Calendar selectedCalendar;
    BookingInterface bookingInterface;
    static String BookingID_edit = "";
    private String AndroidID;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_editbooking);

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

        nameEditText = findViewById(R.id.NameEditText);
        phoneEditText = findViewById(R.id.PhoneEditText);
        commentEditText = findViewById(R.id.CommentEditText);

        nameEditText.setText(getIntent().getExtras().getString("Booking_name"));
        phoneEditText.setText(getIntent().getExtras().getString("Booking_phone"));
        NumberOfPeopleValue.setText(getIntent().getExtras().getString("Booking_people"));
        SelectedDate.setText(getIntent().getExtras().getString("Booking_date"));
        SelectedTime.setText(getIntent().getExtras().getString("Booking_time"));
        commentEditText.setText(getIntent().getExtras().getString("Booking_comment"));

    }

    // Відображення діалогового вікна для вибору дати
    public void setDate(View v) {
        if (selectedCalendar == null) {
            selectedCalendar = Calendar.getInstance();
        }
        new DatePickerDialog(EditBooking.this, d,
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // Встановлення початкової дати
    private void setInitialDate() {
        Calendar now = Calendar.getInstance();
        // Якщо поточний час пізніше 19:00
        if (now.get(Calendar.HOUR_OF_DAY) >= 19) {
            // Встановити дату на наступний день
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        SelectedDate.setText(DateUtils.formatDateTime(this,
                now.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    // Встановлення оброблювача вибору дати
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Створення календаря з обраною датою
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);
            selectedCalendar = selectedDate;

            // Порівняння вибраної дати з поточною датою
            Calendar currentDate = Calendar.getInstance();

            // Перевіряємо, чи є обрана дата раніше поточної дати, і якщо так, то виводимо повідомлення
            if (selectedDate.before(currentDate)) {
                // Якщо вибрана дата вже пройшла, показуємо повідомлення про помилку
                Toast.makeText(EditBooking.this, "Оберіть майбутню дату", Toast.LENGTH_SHORT).show();
            } else {
                // Перевіряємо, чи є обрана дата поточною датою і час пізніше або дорівнює 20:00 і якщо так, тоді виводимо повідомлення
                if (selectedDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) && selectedDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) && selectedDate.get(Calendar.HOUR_OF_DAY) >= 20) {
                    Toast.makeText(EditBooking.this, "Оберіть майбутню дату", Toast.LENGTH_SHORT).show();
                } else {
                    // Інакше оновлюємо дату
                    dateAndTime.set(Calendar.YEAR, year);
                    dateAndTime.set(Calendar.MONTH, monthOfYear);
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Якщо обран поточний день, тоді в HOUR_OF_DAY записується поточна година
                    Calendar now = Calendar.getInstance();
                    if (dateAndTime.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                            dateAndTime.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                            dateAndTime.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) {
                        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
                        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    } else {
                        // Якщо обран інший день у майбутньому, тоді година ставиться на 0, щоб бронювання показувалися з 8 години
                        dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
                    }

                    // Форматуємо дату у відповідний вигляд та встановлюємо її в SelectedDate
                    String formattedDate = DateUtils.formatDateTime(EditBooking.this,
                            dateAndTime.getTimeInMillis(),
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                    SelectedDate.setText(formattedDate);

                    // Оновлення часу в рядку з часом бронювання
                    setInitialTime();
                }
            }
        }
    };

    // Встановлення початкового часу
    private void setInitialTime() {
        // Отримати поточний час
        Calendar now = Calendar.getInstance();
        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);

        // Отримати вибрану дату та час
        Calendar selectedTime = Calendar.getInstance();
        selectedTime.setTimeInMillis(dateAndTime.getTimeInMillis());

        // Якщо вибрана дата більша, ніж поточна дата
        if (dateAndTime.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                dateAndTime.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                dateAndTime.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
            if (hourOfDay >= 19 || hourOfDay < 7) {
                // Встановити годину на 8:00
                now.set(Calendar.HOUR_OF_DAY, 8);
                now.set(Calendar.MINUTE, 0);

                // Якщо поточний час пізніше 19:00, встановити дату на наступний день
                if (hourOfDay >= 19) {
                    now.add(Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                // Встановити годину бронювання на найближчі 2 години від години на момент початку бронювання
                now.set(Calendar.MINUTE, 0);
                now.add(Calendar.HOUR_OF_DAY, 2);
            }
        }
        else {
            // В інших випадках ставиться 8:00
            now.set(Calendar.HOUR_OF_DAY, 8);
            now.set(Calendar.MINUTE, 0);
        }

        // Встановити час в текстове поле та у змінну dateAndTime
        SelectedTime.setText(DateUtils.formatDateTime(this,
                now.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
        dateAndTime.setTimeInMillis(now.getTimeInMillis());
    }

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

        int startHour = calendar.get(Calendar.HOUR_OF_DAY); // Отримуємо поточну годину
        int maxValue = 20; // Максимальне значення години

        final NumberPicker hourPicker = new NumberPicker(this); // Створюємо NumberPicker для годин
        hourPicker.setMinValue(startHour); // Встановлюємо мінімальне значення години
        hourPicker.setMaxValue(maxValue); // Встановлюємо максимальне значення години
        hourPicker.setValue(startHour); // Встановлюємо початкове значення години

        final NumberPicker minutePicker = new NumberPicker(this); // Створюємо NumberPicker для хвилин
        minutePicker.setMinValue(0); // Встановлюємо мінімальне значення хвилин
        minutePicker.setMaxValue(5); // Встановлюємо максимальне значення хвилин
        minutePicker.setDisplayedValues(new String[]{"00", "10", "20", "30", "40", "50"}); // Встановлюємо значення хвилин для відображення
        minutePicker.setValue(Calendar.MINUTE / 10); // Встановлюємо початкове значення хвилин

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
                        Toast.makeText(EditBooking.this, "Оберить майбутній час", Toast.LENGTH_SHORT).show();
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

    // Дії при натисканні на кнопку "Зберегти зміни"
    public void SaveButtonClick(View view) {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String comment = commentEditText.getText().toString().trim();

        // Перевірка, чи заповнені ім'я та номер телефону та наявність підключення до інтернету
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show();
            return;
        } if (MainActivity.isNetworkConnected(view.getContext())) {
        } else {
            Toast.makeText(EditBooking.this, "Відсутнє підключення до інтернету", Toast.LENGTH_SHORT).show();
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
                "Час бронювання: <b>" + SelectedTime.getText().toString() + "</b>";

        if (!comment.isEmpty()) {
            message += "<br>Комментар: <b>" + comment + "</b>";
        }

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
                Call<AddBooking> putBooking = bookingInterface.putBooking(
                        AndroidID,
                        BookingID_edit,
                        nameEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        Integer.parseInt(NumberOfPeopleValue.getText().toString()),
                        SelectedDate.getText().toString(),
                        SelectedTime.getText().toString(),
                        commentEditText.getText().toString()
                );
                putBooking.enqueue(new Callback<AddBooking>() {
                    @Override
                    public void onResponse(Call<AddBooking> call, Response<AddBooking> response) {
                        Toast.makeText(EditBooking.this,"Бронювання збережено", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditBooking.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<AddBooking> call, Throwable t) {
                        Toast.makeText(EditBooking.this,"Помилка", Toast.LENGTH_SHORT).show();
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

    // При натисканні на кнопку "Відмінити зміни" повертаємося до начальної сторінки та закриваємо поточну сторінку
    public void CancelButtonClick(View view) {
        Intent intent = new Intent(EditBooking.this, MainActivity.class);
        startActivity(intent);
    }

}