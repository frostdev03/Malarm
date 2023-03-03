package com.frostdev.malarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText mAlarmTimeEditText;
    private TextView mMathQuestionTextView;
    private EditText mMathAnswerEditText;
    private int mAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel for notifications
            NotificationChannel channel = new NotificationChannel("alarm", "Alarm", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Alarm notification");

            // Register channel with system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        mAlarmTimeEditText = findViewById(R.id.alarm_time_edittext);
        mMathQuestionTextView = findViewById(R.id.math_question_textview);
        mMathAnswerEditText = findViewById(R.id.math_answer_edittext);

        // Get user's age from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        mAge = sharedPreferences.getInt("age", 0);

        // Set an OnClickListener on the "Add Alarm" button
        Button addAlarmButton = findViewById(R.id.add_alarm_button);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alarmTime = mAlarmTimeEditText.getText().toString();

                // Set up the alarm
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                Calendar calendar = Calendar.getInstance();
                String[] timeParts = alarmTime.split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
                calendar.set(Calendar.SECOND, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        // Set an OnClickListener on the "Answer" button
        Button answerButton = findViewById(R.id.answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mathAnswer = mMathAnswerEditText.getText().toString();

                // Check if the answer is correct
                int correctAnswer = MathQuestion.getCorrectAnswer(mMathQuestionTextView.getText().toString());
                if (mathAnswer.equals(Integer.toString(correctAnswer))) {
                    // Cancel the alarm
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(MainActivity.this, "Alarm turned off", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Check if the user's age is less than 10 and generate a math question
        if (mAge < 10) {
            String mathQuestion = MathQuestion.generateQuestion(1);
            mMathQuestionTextView.setText(mathQuestion);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save user's age to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("age", mAge);
        editor.apply();
    }



}