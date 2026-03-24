package com.example.collegealertapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class EventDetailActivity extends AppCompatActivity {

    private TextView detailEventType, detailEventTitle, detailDescription, detailDate, detailTime, detailVenue;
    private Button detailSaveButton;
    private EventModel event;
    private SharedPreferences sharedPreferences;
    private List<String> savedEventIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        // Initialize views
        detailEventType = findViewById(R.id.detailEventType);
        detailEventTitle = findViewById(R.id.detailEventTitle);
        detailDescription = findViewById(R.id.detailDescription);
        detailDate = findViewById(R.id.detailDate);
        detailTime = findViewById(R.id.detailTime);
        detailVenue = findViewById(R.id.detailVenue);
        detailSaveButton = findViewById(R.id.detailSaveButton);

        sharedPreferences = getSharedPreferences("CollegeAlertApp", Context.MODE_PRIVATE);
        loadSavedEvents();

        // Get event from intent
        Intent intent = getIntent();
        if (intent != null) {
            String type = intent.getStringExtra("event_type");
            String title = intent.getStringExtra("event_title");
            String description = intent.getStringExtra("event_description");
            String date = intent.getStringExtra("event_date");
            String time = intent.getStringExtra("event_time");
            String venue = intent.getStringExtra("event_venue");

            // 🔴 LOG: Intent se aayi hui description
            Log.d("DETAIL", "🔵 Description from intent: " + description);


            Log.d("DETAIL", "Type: " + type);
            Log.d("DETAIL", "Title: " + title);
            Log.d("DETAIL", "Date: " + date);
            Log.d("DETAIL", "Time: " + time);
            Log.d("DETAIL", "Venue: " + venue);

            if (description == null || description.isEmpty()) {
                description = "No description available";
                Log.d("DETAIL", "Description was null, set to default");
            }

            event = new EventModel(type, title, description, venue, date, time, "", false);

            detailEventType.setText(type != null ? type : "N/A");
            detailEventTitle.setText(title != null ? title : "N/A");
            detailDescription.setText(description);  // 🔴 YEH LINE DESCRIPTION SET KAR RAHI HAI
            detailDate.setText("Date: " + (date != null ? date : "N/A"));
            detailTime.setText("Time: " + (time != null ? time : "N/A"));
            detailVenue.setText("Venue: " + (venue != null ? venue : "N/A"));

            updateSaveButton();
        }



        detailSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSaveEvent();
            }
        });
    }

    private void loadSavedEvents() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved_events", "[]");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        savedEventIds = gson.fromJson(json, type);
        if (savedEventIds == null) {
            savedEventIds = new ArrayList<>();
        }
    }

    private void saveEventId(String eventId) {
        if (!savedEventIds.contains(eventId)) {
            savedEventIds.add(eventId);
            Gson gson = new Gson();
            String json = gson.toJson(savedEventIds);
            sharedPreferences.edit().putString("saved_events", json).apply();
        }
    }

    private void removeEventId(String eventId) {
        savedEventIds.remove(eventId);
        Gson gson = new Gson();
        String json = gson.toJson(savedEventIds);
        sharedPreferences.edit().putString("saved_events", json).apply();
    }

    private void updateSaveButton() {
        if (event == null) return;
        String eventId = event.getTitle() + "_" + event.getDate();
        if (savedEventIds.contains(eventId)) {
            detailSaveButton.setText("Unsave Event");
        } else {
            detailSaveButton.setText("Save Event");
        }
    }

    private void toggleSaveEvent() {
        if (event == null) return;
        String eventId = event.getTitle() + "_" + event.getDate();
        if (savedEventIds.contains(eventId)) {
            removeEventId(eventId);
            Toast.makeText(this, "Event removed from saved", Toast.LENGTH_SHORT).show();
        } else {
            saveEventId(eventId);
            Toast.makeText(this, "Event saved!", Toast.LENGTH_SHORT).show();
        }
        updateSaveButton();
    }
}