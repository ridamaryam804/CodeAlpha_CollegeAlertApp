package com.example.collegealertapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {
    private List<EventModel> eventList;
    private LayoutInflater inflater;
    private Context context;
    private SharedPreferences sharedPreferences;
    private List<String> savedEventIds;

    public EventAdapter(Context context, List<EventModel> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.inflater = LayoutInflater.from(context);
        this.sharedPreferences = context.getSharedPreferences("CollegeAlertApp", Context.MODE_PRIVATE);
        loadSavedEvents();
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

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public EventModel getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_item, parent, false);
        }

        EventModel event = eventList.get(position);

        LinearLayout eventCard = convertView.findViewById(R.id.eventCard);
        TextView eventType = convertView.findViewById(R.id.eventType);
        TextView newTag = convertView.findViewById(R.id.newTag);
        TextView eventTitle = convertView.findViewById(R.id.eventTitle);
        TextView eventDescription = convertView.findViewById(R.id.eventDescription);
        TextView eventDate = convertView.findViewById(R.id.eventDate);
        TextView eventTime = convertView.findViewById(R.id.eventTime);
        TextView eventVenue = convertView.findViewById(R.id.eventVenue);
        TextView saveIcon = convertView.findViewById(R.id.saveIcon);

        eventType.setText(event.getType().toUpperCase());
        eventTitle.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
        eventDate.setText(event.getDate());
        eventTime.setText(event.getTime());
        eventVenue.setText(event.getVenue());

        // NEW TAG
        newTag.setVisibility(View.VISIBLE);

        // 🔴 CARD BACKGROUND WITH BORDER
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(16); // Rounded corners
        drawable.setStroke(4, Color.parseColor("#333333")); // Border width 4px, dark color

        // Set background color based on event type
        switch(event.getType()) {
            case "Exam":
                drawable.setColor(Color.parseColor("#ffb38a"));  // Exam background
                eventCard.setBackground(drawable);
                break;
            case "Seminar":
                drawable.setColor(Color.parseColor("#FFB554"));  // 🔴 Seminar new color
                eventCard.setBackground(drawable);
                break;
            case "Notice":
                drawable.setColor(Color.parseColor("#FF725A"));  // Notice background
                eventCard.setBackground(drawable);
                break;
            case "Fest":
                drawable.setColor(Color.parseColor("#f79489"));  // Fest background
                eventCard.setBackground(drawable);
                break;
        }

        // Save icon logic
        String eventId = event.getTitle() + "_" + event.getDate();
        if (savedEventIds.contains(eventId)) {
            saveIcon.setText("⭐");
            saveIcon.setTextColor(ContextCompat.getColor(context, R.color.teal_700));
        } else {
            saveIcon.setText("☆");
            saveIcon.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        // Save icon click listener
        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = event.getTitle() + "_" + event.getDate();
                if (savedEventIds.contains(eventId)) {
                    removeEventId(eventId);
                    saveIcon.setText("☆");
                    saveIcon.setTextColor(ContextCompat.getColor(context, R.color.black));
                    Toast.makeText(context, "Removed from saved", Toast.LENGTH_SHORT).show();
                } else {
                    saveEventId(eventId);
                    saveIcon.setText("⭐");
                    saveIcon.setTextColor(ContextCompat.getColor(context, R.color.teal_700));
                    Toast.makeText(context, "Event saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    public void updateList(List<EventModel> newList) {
        this.eventList = newList;
        notifyDataSetChanged();
    }
}