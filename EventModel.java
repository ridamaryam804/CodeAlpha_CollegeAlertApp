package com.example.collegealertapp;

public class EventModel {
    private String id;
    private String type; // Seminar, Exam, Fest, Notice
    private String title;
    private String description;
    private String venue;
    private String date;
    private String time;
    private String organizer;
    private boolean isToday;

    // ✅ ICON WALI LINE HATAI
    // private int iconResId;

    // EventModel.java mein constructor
    public EventModel(String type, String title, String description, String venue,
                      String date, String time, String organizer, boolean isToday) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;  // DD/MM/YYYY format
        this.time = time;
        this.organizer = organizer;
        this.isToday = isToday;


        // ✅ ICON SET KARNE WALA PURA SWITCH CASE HATAYA
        /*
        switch(type) {
            case "Seminar":
                this.iconResId = R.drawable.ic_seminar;
                break;
            case "Exam":
                this.iconResId = R.drawable.ic_exam;
                break;
            case "Fest":
                this.iconResId = R.drawable.ic_fest;
                break;
            case "Notice":
                this.iconResId = R.drawable.ic_notice;
                break;
        }
        */
    }

    // Getters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVenue() { return venue; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getOrganizer() { return organizer; }
    public boolean isToday() { return isToday; }

    // ✅ ICON GETTER BHI HATAO
    // public int getIconResId() { return iconResId; }
}