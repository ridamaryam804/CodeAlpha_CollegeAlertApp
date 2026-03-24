package com.example.collegealertapp;

import android.content.Context;
import android.widget.LinearLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.res.ColorStateList;
import android.util.Log;

// Firebase imports
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.UserProfileChangeRequest;

// Java utilities
import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private View navHome, navAlerts, navCalendar, navSaved, navProfile;
    private TextView iconHome, iconAlerts, iconCalendar, iconSaved, iconProfile;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initializeViews();
            setupViewPager();
            setupBottomNavigation();
            updateNavIcons(0);

            // Apply enter animation to main layout
            applyEnterAnimation();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void applyEnterAnimation() {
        View mainLayout = findViewById(R.id.main_layout);
        if (mainLayout == null) return;

        // Fade in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(800);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        mainLayout.startAnimation(fadeIn);
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        navHome = findViewById(R.id.navHome);
        navAlerts = findViewById(R.id.navAlerts);
        navCalendar = findViewById(R.id.navCalendar);
        navSaved = findViewById(R.id.navSaved);
        navProfile = findViewById(R.id.navProfile);

        iconHome = findViewById(R.id.iconHome);
        iconAlerts = findViewById(R.id.iconAlerts);
        iconCalendar = findViewById(R.id.iconCalendar);
        iconSaved = findViewById(R.id.iconSaved);
        iconProfile = findViewById(R.id.iconProfile);
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateNavIcons(position);
            }
        });
    }

    private void setupBottomNavigation() {
        navHome.setOnClickListener(v -> {
            applyClickAnimation(v);
            viewPager.setCurrentItem(0, false);
            updateNavIcons(0);
        });

        navAlerts.setOnClickListener(v -> {
            applyClickAnimation(v);
            viewPager.setCurrentItem(1, false);
            updateNavIcons(1);
        });

        navCalendar.setOnClickListener(v -> {
            applyClickAnimation(v);
            viewPager.setCurrentItem(2, false);
            updateNavIcons(2);
        });

        navSaved.setOnClickListener(v -> {
            applyClickAnimation(v);
            viewPager.setCurrentItem(3, false);
            updateNavIcons(3);
        });

        navProfile.setOnClickListener(v -> {
            applyClickAnimation(v);
            viewPager.setCurrentItem(4, false);
            updateNavIcons(4);
        });
    }

    private void applyClickAnimation(View view) {
        // Scale animation on click
        ScaleAnimation scale = new ScaleAnimation(
                1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(100);
        scale.setRepeatMode(Animation.REVERSE);
        scale.setRepeatCount(1);

        view.startAnimation(scale);
    }

    private void updateNavIcons(int position) {
        iconHome.setTextColor(getColor(R.color.black));
        iconAlerts.setTextColor(getColor(R.color.black));
        iconCalendar.setTextColor(getColor(R.color.black));
        iconSaved.setTextColor(getColor(R.color.black));
        iconProfile.setTextColor(getColor(R.color.black));

        switch (position) {
            case 0:
                iconHome.setTextColor(getColor(R.color.teal_700));
                break;
            case 1:
                iconAlerts.setTextColor(getColor(R.color.teal_700));
                break;
            case 2:
                iconCalendar.setTextColor(getColor(R.color.teal_700));
                break;
            case 3:
                iconSaved.setTextColor(getColor(R.color.teal_700));
                break;
            case 4:
                iconProfile.setTextColor(getColor(R.color.teal_700));
                break;
        }
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new AlertsFragment();
                case 2:
                    return new CalendarFragment();
                case 3:
                    return new SavedFragment();
                case 4:
                    return new ProfileFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    // ============= HOME FRAGMENT =============
    public static class HomeFragment extends Fragment {
        private List<EventModel> allEvents;
        private EventAdapter adapter;
        private TextView chipAll, chipSeminars, chipExams, chipFests, chipNotices;
        private ListView eventListView;
        private TextView todayCount;
        private TextView campusAlertTitle, stayInformedText;
        private Context context;
        private DatabaseReference eventsRef;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            try {
                eventListView = view.findViewById(R.id.eventListView);
                todayCount = view.findViewById(R.id.todayCount);
                EditText searchEditText = view.findViewById(R.id.searchEditText);

                // Find animation views
                campusAlertTitle = view.findViewById(R.id.campusAlertTitle);
                stayInformedText = view.findViewById(R.id.stayInformedText);

                chipAll = view.findViewById(R.id.chipAll);
                chipSeminars = view.findViewById(R.id.chipSeminars);
                chipExams = view.findViewById(R.id.chipExams);
                chipFests = view.findViewById(R.id.chipFests);
                chipNotices = view.findViewById(R.id.chipNotices);

                allEvents = new ArrayList<>();

                if (eventListView != null && context != null) {
                    adapter = new EventAdapter(context, allEvents);
                    eventListView.setAdapter(adapter);

                    // 🔴 UPDATED: Event click listener - opens detail activity
                    eventListView.setOnItemClickListener((parent, view1, position, id) -> {
                        EventModel clickedEvent = adapter.getItem(position);

                        // Open detail activity
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        intent.putExtra("event_type", clickedEvent.getType());
                        intent.putExtra("event_title", clickedEvent.getTitle());
                        intent.putExtra("event_description", clickedEvent.getDescription());
                        intent.putExtra("event_date", clickedEvent.getDate());
                        intent.putExtra("event_time", clickedEvent.getTime());
                        intent.putExtra("event_venue", clickedEvent.getVenue());
                        startActivity(intent);

                        // Apply click animation to list item
                        view1.startAnimation(createListItemAnimation());
                    });
                }

                // 🔥 Firebase se data load karo
                loadEventsFromFirebase();

                setChipClickListeners();

                if (searchEditText != null) {
                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            filterEvents(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }

                // Apply title animations
                applyTitleAnimations();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        private void applyTitleAnimations() {
            // Scale and fade animation for CampusAlert title
            if (campusAlertTitle != null) {
                campusAlertTitle.setAlpha(0f);
                campusAlertTitle.setScaleX(0.5f);
                campusAlertTitle.setScaleY(0.5f);

                campusAlertTitle.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(800)
                        .setInterpolator(new DecelerateInterpolator())
                        .setStartDelay(200)
                        .start();
            }

            // Slide up animation for Stay Informed text
            if (stayInformedText != null) {
                stayInformedText.setAlpha(0f);
                stayInformedText.setTranslationY(30f);

                stayInformedText.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(600)
                        .setInterpolator(new DecelerateInterpolator())
                        .setStartDelay(500)
                        .start();
            }

            // Animate chips with staggered delay
            if (chipAll != null) {
                animateChip(chipAll, 700);
            }
            if (chipSeminars != null) {
                animateChip(chipSeminars, 800);
            }
            if (chipExams != null) {
                animateChip(chipExams, 900);
            }
            if (chipFests != null) {
                animateChip(chipFests, 1000);
            }
            if (chipNotices != null) {
                animateChip(chipNotices, 1100);
            }
        }

        private void animateChip(View chip, long delay) {
            chip.setAlpha(0f);
            chip.setScaleX(0.7f);
            chip.setScaleY(0.7f);

            chip.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .setStartDelay(delay)
                    .start();
        }

        private Animation createListItemAnimation() {
            AnimationSet set = new AnimationSet(true);

            // Scale down a bit
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.95f, 1.0f, 0.95f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(100);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);

            set.addAnimation(scale);
            return set;
        }

        private void loadEventsFromFirebase() {
            eventsRef = FirebaseDatabase.getInstance().getReference("events");

            eventsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allEvents.clear();

                    // 🔴 LOG: Total events count
                    Log.d("FIREBASE", "Total events in Firebase: " + snapshot.getChildrenCount());

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String type = eventSnapshot.child("type").getValue(String.class);
                        String title = eventSnapshot.child("title").getValue(String.class);
                        String description = eventSnapshot.child("description").getValue(String.class);
                        String date = eventSnapshot.child("date").getValue(String.class);
                        String time = eventSnapshot.child("time").getValue(String.class);
                        String venue = eventSnapshot.child("venue").getValue(String.class);

                        // 🔴 LOG: Har event ki description
                        Log.d("FIREBASE", "Event Title: " + title);
                        Log.d("FIREBASE", "Description from Firebase: " + description);

                        if (type != null && title != null) {
                            EventModel event = new EventModel(
                                    type, title, description, venue,
                                    date, time, "", false
                            );
                            allEvents.add(event);
                        }
                    }

                    if (adapter != null) {
                        adapter.updateList(new ArrayList<>(allEvents));

                        // Animate list items fade in
                        if (eventListView != null) {
                            eventListView.setAlpha(0f);
                            eventListView.animate()
                                    .alpha(1f)
                                    .setDuration(600)
                                    .setStartDelay(1200)
                                    .start();
                        }
                    }

                    if (todayCount != null) {
                        todayCount.setText(String.valueOf(allEvents.size()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void setChipClickListeners() {
            if (context == null) return;

            if (chipAll != null) {
                chipAll.setOnClickListener(v -> {
                    applyChipClickAnimation(v);
                    updateChipStyles("All");
                    filterByType("All");
                });
            }
            if (chipSeminars != null) {
                chipSeminars.setOnClickListener(v -> {
                    applyChipClickAnimation(v);
                    updateChipStyles("Seminar");
                    filterByType("Seminar");
                });
            }
            if (chipExams != null) {
                chipExams.setOnClickListener(v -> {
                    applyChipClickAnimation(v);
                    updateChipStyles("Exam");
                    filterByType("Exam");
                });
            }
            if (chipFests != null) {
                chipFests.setOnClickListener(v -> {
                    applyChipClickAnimation(v);
                    updateChipStyles("Fest");
                    filterByType("Fest");
                });
            }
            if (chipNotices != null) {
                chipNotices.setOnClickListener(v -> {
                    applyChipClickAnimation(v);
                    updateChipStyles("Notice");
                    filterByType("Notice");
                });
            }
        }

        private void applyChipClickAnimation(View chip) {
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(150);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);

            chip.startAnimation(scale);
        }

        private void updateChipStyles(String selected) {
            if (context == null) return;

            // Reset all chips
            if (chipAll != null) {
                chipAll.setBackgroundResource(R.drawable.chip_unselected);
                chipAll.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            if (chipSeminars != null) {
                chipSeminars.setBackgroundResource(R.drawable.chip_unselected);
                chipSeminars.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            if (chipExams != null) {
                chipExams.setBackgroundResource(R.drawable.chip_unselected);
                chipExams.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            if (chipFests != null) {
                chipFests.setBackgroundResource(R.drawable.chip_unselected);
                chipFests.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            if (chipNotices != null) {
                chipNotices.setBackgroundResource(R.drawable.chip_unselected);
                chipNotices.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

            // Highlight selected
            switch (selected) {
                case "All":
                    if (chipAll != null) {
                        chipAll.setBackgroundResource(R.drawable.chip_selected);
                        chipAll.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }
                    break;
                case "Seminar":
                    if (chipSeminars != null) {
                        chipSeminars.setBackgroundResource(R.drawable.chip_selected);
                        chipSeminars.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }
                    break;
                case "Exam":
                    if (chipExams != null) {
                        chipExams.setBackgroundResource(R.drawable.chip_selected);
                        chipExams.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }
                    break;
                case "Fest":
                    if (chipFests != null) {
                        chipFests.setBackgroundResource(R.drawable.chip_selected);
                        chipFests.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }
                    break;
                case "Notice":
                    if (chipNotices != null) {
                        chipNotices.setBackgroundResource(R.drawable.chip_selected);
                        chipNotices.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }
                    break;
            }
        }

        private void filterByType(String type) {
            List<EventModel> filteredList = new ArrayList<>();

            if (type.equals("All")) {
                filteredList.addAll(allEvents);
            } else {
                for (EventModel event : allEvents) {
                    if (event.getType().equals(type)) {
                        filteredList.add(event);
                    }
                }
            }

            if (adapter != null) {
                adapter.updateList(filteredList);
            }
        }

        private void filterEvents(String query) {
            List<EventModel> filteredList = new ArrayList<>();

            if (query.isEmpty()) {
                filteredList.addAll(allEvents);
            } else {
                for (EventModel event : allEvents) {
                    if (event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(event);
                    }
                }
            }

            if (adapter != null) {
                adapter.updateList(filteredList);
            }
        }
    }

    // ============= ALERTS FRAGMENT (with Underline) =============
    public static class AlertsFragment extends Fragment {
        private Context context;
        private TextView allAlertsTitle;
        private View underlineView;
        private ListView alertsListView;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_alerts, container, false);

            try {
                alertsListView = view.findViewById(R.id.alertsListView);
                allAlertsTitle = view.findViewById(R.id.allAlertsTitle);
                underlineView = view.findViewById(R.id.underlineView);

                // Firebase se data load karo
                loadAlertsFromFirebase();

                if (alertsListView != null && context != null) {
                    alertsListView.setOnItemClickListener((parent, view1, position, id) -> {
                        // Click animation
                        applyClickAnimation(view1);
                    });
                }

                // Apply animations
                applyTitleAnimation();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return view;
        }

        private void loadAlertsFromFirebase() {
            // Firebase se saare events load karo
            DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

            eventsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<EventModel> alertsList = new ArrayList<>();

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String type = eventSnapshot.child("type").getValue(String.class);
                        String title = eventSnapshot.child("title").getValue(String.class);
                        String description = eventSnapshot.child("description").getValue(String.class);
                        String date = eventSnapshot.child("date").getValue(String.class);
                        String time = eventSnapshot.child("time").getValue(String.class);
                        String venue = eventSnapshot.child("venue").getValue(String.class);

                        if (type != null && title != null) {
                            EventModel event = new EventModel(
                                    type, title, description, venue,
                                    date, time, "", false
                            );
                            alertsList.add(event);
                        }
                    }

                    if (alertsListView != null && context != null) {
                        EventAdapter adapter = new EventAdapter(context, alertsList);
                        alertsListView.setAdapter(adapter);

                        // Animate list fade in
                        applyListViewAnimation();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to load alerts", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void applyTitleAnimation() {
            if (allAlertsTitle == null) return;

            // Title animation
            allAlertsTitle.setAlpha(0f);
            allAlertsTitle.setScaleX(0.7f);
            allAlertsTitle.setScaleY(0.7f);

            allAlertsTitle.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();

            // Underline animation
            if (underlineView != null) {
                underlineView.setAlpha(0f);
                underlineView.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setStartDelay(300)
                        .start();
            }
        }

        private void applyListViewAnimation() {
            if (alertsListView == null) return;

            alertsListView.setAlpha(0f);
            alertsListView.animate()
                    .alpha(1f)
                    .setDuration(800)
                    .setStartDelay(200)
                    .start();
        }

        private void applyClickAnimation(View view) {
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.95f, 1.0f, 0.95f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(100);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);

            view.startAnimation(scale);
        }
    }

    // ============= CALENDAR FRAGMENT (with date events) =============
    public static class CalendarFragment extends Fragment {
        private Context context;
        private LinearLayout calendarGrid;
        private TextView currentMonthYear;
        private ListView calendarEventsListView;
        private TextView noEventsMessage;
        private TextView selectedDateTitle;
        private LinearLayout upcomingEventsLayout;
        private TextView upcomingTitle;
        private int currentYear = 2026;
        private int currentMonth = 2;
        private TextView selectedDateView = null;
        private String selectedDate = "";
        private DatabaseReference eventsRef;

        private final String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.calendar_screen, container, false);
            try {
                calendarGrid = view.findViewById(R.id.calendarGrid);
                currentMonthYear = view.findViewById(R.id.currentMonthYear);
                calendarEventsListView = view.findViewById(R.id.calendarEventsListView);
                noEventsMessage = view.findViewById(R.id.noEventsMessage);
                selectedDateTitle = view.findViewById(R.id.selectedDateTitle);
                upcomingEventsLayout = view.findViewById(R.id.upcomingEventsLayout);
                upcomingTitle = view.findViewById(R.id.upcomingTitle);

                eventsRef = FirebaseDatabase.getInstance().getReference("events");

                TextView prevMonthBtn = view.findViewById(R.id.prevMonthBtn);
                TextView nextMonthBtn = view.findViewById(R.id.nextMonthBtn);

                updateCalendar();
                loadUpcomingEvents();

                prevMonthBtn.setOnClickListener(v -> {
                    applyClickAnimation(v);
                    currentMonth--;
                    if (currentMonth < 0) {
                        currentMonth = 11;
                        currentYear--;
                    }
                    updateCalendar();
                });

                nextMonthBtn.setOnClickListener(v -> {
                    applyClickAnimation(v);
                    currentMonth++;
                    if (currentMonth > 11) {
                        currentMonth = 0;
                        currentYear++;
                    }
                    updateCalendar();
                });

                view.setAlpha(0f);
                view.animate()
                        .alpha(1f)
                        .setDuration(600)
                        .start();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        // ==================== UPCOMING EVENTS ====================

        private void loadUpcomingEvents() {
            java.util.Calendar todayCal = java.util.Calendar.getInstance();

            String todayDate = formatDateFirebase(todayCal);
            java.util.Calendar tomorrowCal = (java.util.Calendar) todayCal.clone();
            tomorrowCal.add(java.util.Calendar.DAY_OF_MONTH, 1);
            String tomorrowDate = formatDateFirebase(tomorrowCal);
            java.util.Calendar dayAfterCal = (java.util.Calendar) todayCal.clone();
            dayAfterCal.add(java.util.Calendar.DAY_OF_MONTH, 2);
            String dayAfterDate = formatDateFirebase(dayAfterCal);

            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (upcomingEventsLayout != null) {
                        upcomingEventsLayout.removeAllViews();
                    }

                    addEventsForDateFirebase(snapshot, todayDate, "Today");
                    addEventsForDateFirebase(snapshot, tomorrowDate, "Tomorrow");
                    addEventsForDateFirebase(snapshot, dayAfterDate, "Day After");

                    if (upcomingEventsLayout != null && upcomingEventsLayout.getChildCount() == 0) {
                        TextView noEventsText = new TextView(context);
                        noEventsText.setText("No upcoming events");
                        noEventsText.setTextSize(14);
                        noEventsText.setTextColor(Color.parseColor("#666666"));
                        noEventsText.setPadding(16, 32, 16, 16);
                        noEventsText.setGravity(android.view.Gravity.CENTER);
                        upcomingEventsLayout.addView(noEventsText);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private String formatDateFirebase(java.util.Calendar cal) {
            int year = cal.get(java.util.Calendar.YEAR);
            int month = cal.get(java.util.Calendar.MONTH) + 1;
            int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
            return String.format("%04d/%02d/%02d", year, month, day);
        }

        private String convertToDisplayDate(String firebaseDate) {
            String[] parts = firebaseDate.split("/");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
            return firebaseDate;
        }

        private void addEventsForDateFirebase(DataSnapshot snapshot, String firebaseDate, String dateLabel) {
            List<EventModel> eventsForDate = new ArrayList<>();

            for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                String eventDate = eventSnapshot.child("date").getValue(String.class);
                if (eventDate != null && eventDate.equals(firebaseDate)) {
                    String type = eventSnapshot.child("type").getValue(String.class);
                    String title = eventSnapshot.child("title").getValue(String.class);
                    String description = eventSnapshot.child("description").getValue(String.class);
                    String time = eventSnapshot.child("time").getValue(String.class);
                    String venue = eventSnapshot.child("venue").getValue(String.class);
                    if (description == null) description = "No description";
                    String displayDate = convertToDisplayDate(firebaseDate);
                    eventsForDate.add(new EventModel(type, title, description, venue, displayDate, time, "", false));
                }
            }

            if (!eventsForDate.isEmpty() && upcomingEventsLayout != null) {
                TextView dateHeader = new TextView(context);
                dateHeader.setText(dateLabel + " - " + convertToDisplayDate(firebaseDate));
                dateHeader.setTextSize(16);
                dateHeader.setTextColor(Color.parseColor("#C76327"));
                dateHeader.setTypeface(null, android.graphics.Typeface.BOLD);
                dateHeader.setPadding(0, 16, 0, 8);
                upcomingEventsLayout.addView(dateHeader);
                for (EventModel event : eventsForDate) {
                    View eventView = createUpcomingEventView(event);
                    upcomingEventsLayout.addView(eventView);
                }
            }
        }

        private View createUpcomingEventView(EventModel event) {
            LinearLayout card = new LinearLayout(context);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundColor(Color.WHITE);
            card.setElevation(4);
            card.setPadding(16, 16, 16, 16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 8, 0, 8);
            card.setLayoutParams(params);

            LinearLayout topRow = new LinearLayout(context);
            topRow.setOrientation(LinearLayout.HORIZONTAL);
            topRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView typeText = new TextView(context);
            typeText.setText(event.getType().toUpperCase());
            typeText.setPadding(8, 4, 8, 4);
            typeText.setTextSize(12);
            typeText.setTypeface(null, android.graphics.Typeface.BOLD);
            switch(event.getType()) {
                case "Exam":
                    typeText.setBackgroundColor(Color.parseColor("#ffb38a"));
                    break;
                case "Seminar":
                    typeText.setBackgroundColor(Color.parseColor("#FFB554"));
                    break;
                case "Notice":
                    typeText.setBackgroundColor(Color.parseColor("#FF725A"));
                    break;
                case "Fest":
                    typeText.setBackgroundColor(Color.parseColor("#f79489"));
                    break;
            }
            typeText.setTextColor(Color.parseColor("#333333"));

            TextView newTag = new TextView(context);
            newTag.setText("NEW");
            newTag.setTextColor(Color.parseColor("#4CAF50"));
            newTag.setTextSize(10);
            newTag.setTypeface(null, android.graphics.Typeface.BOLD);
            newTag.setBackgroundColor(Color.parseColor("#E8F5E9"));
            newTag.setPadding(6, 2, 6, 2);
            newTag.setVisibility(View.VISIBLE);

            topRow.addView(typeText);
            topRow.addView(newTag);
            card.addView(topRow);

            TextView titleText = new TextView(context);
            titleText.setText(event.getTitle());
            titleText.setTextSize(16);
            titleText.setTypeface(null, android.graphics.Typeface.BOLD);
            titleText.setTextColor(Color.parseColor("#333333"));
            titleText.setPadding(0, 8, 0, 4);
            card.addView(titleText);

            TextView descText = new TextView(context);
            descText.setText(event.getDescription());
            descText.setTextSize(14);
            descText.setTextColor(Color.parseColor("#666666"));
            descText.setPadding(0, 0, 0, 8);
            card.addView(descText);

            LinearLayout dateTimeRow = new LinearLayout(context);
            dateTimeRow.setOrientation(LinearLayout.HORIZONTAL);
            dateTimeRow.setPadding(0, 4, 0, 4);
            TextView dateText = new TextView(context);
            dateText.setText("📅 " + event.getDate());
            dateText.setTextSize(12);
            dateText.setTextColor(Color.parseColor("#666666"));
            dateText.setPadding(0, 0, 16, 0);
            TextView timeText = new TextView(context);
            timeText.setText("⏰ " + event.getTime());
            timeText.setTextSize(12);
            timeText.setTextColor(Color.parseColor("#666666"));
            dateTimeRow.addView(dateText);
            dateTimeRow.addView(timeText);
            card.addView(dateTimeRow);

            TextView venueText = new TextView(context);
            venueText.setText("📍 " + event.getVenue());
            venueText.setTextSize(12);
            venueText.setTextColor(Color.parseColor("#666666"));
            card.addView(venueText);
            return card;
        }

        // ==================== CALENDAR GRID ====================

        private void applyClickAnimation(View view) {
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.9f, 1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(150);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);
            view.startAnimation(scale);
        }

        private void updateCalendar() {
            if (context == null || calendarGrid == null) return;
            currentMonthYear.setText(monthNames[currentMonth] + " " + currentYear);
            calendarGrid.removeAllViews();

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(currentYear, currentMonth, 1);
            int firstDayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
            int startDay;
            switch (firstDayOfWeek) {
                case java.util.Calendar.MONDAY: startDay = 0; break;
                case java.util.Calendar.TUESDAY: startDay = 1; break;
                case java.util.Calendar.WEDNESDAY: startDay = 2; break;
                case java.util.Calendar.THURSDAY: startDay = 3; break;
                case java.util.Calendar.FRIDAY: startDay = 4; break;
                case java.util.Calendar.SATURDAY: startDay = 5; break;
                case java.util.Calendar.SUNDAY: startDay = 6; break;
                default: startDay = 0;
            }
            int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            int dayCounter = 1;

            for (int row = 0; row < 6; row++) {
                LinearLayout rowLayout = new LinearLayout(context);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45)));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setWeightSum(7);
                for (int col = 0; col < 7; col++) {
                    int cellPosition = row * 7 + col;
                    TextView cell = new TextView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                    cell.setLayoutParams(params);
                    cell.setGravity(android.view.Gravity.CENTER);
                    cell.setTextSize(14);
                    if (cellPosition >= startDay && dayCounter <= daysInMonth) {
                        final int currentDay = dayCounter;
                        final String dateStr = String.format("%02d/%02d/%d",
                                currentDay, currentMonth + 1, currentYear);
                        cell.setText(String.valueOf(currentDay));
                        cell.setTextColor(ContextCompat.getColor(context, R.color.black));
                        java.util.Calendar today = java.util.Calendar.getInstance();
                        if (currentDay == today.get(java.util.Calendar.DAY_OF_MONTH) &&
                                currentMonth == today.get(java.util.Calendar.MONTH) &&
                                currentYear == today.get(java.util.Calendar.YEAR)) {
                            cell.setTextColor(ContextCompat.getColor(context, R.color.teal_700));
                            cell.setTypeface(null, android.graphics.Typeface.BOLD);
                        }
                        final TextView clickedCell = cell;
                        cell.setOnClickListener(v -> {
                            applyClickAnimation(v);
                            if (selectedDateView != null) {
                                selectedDateView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                                selectedDateView.setTextColor(ContextCompat.getColor(context, R.color.black));
                            }
                            clickedCell.setBackgroundColor(ContextCompat.getColor(context, R.color.calendar_selected));
                            clickedCell.setTextColor(ContextCompat.getColor(context, R.color.white));
                            selectedDateView = clickedCell;
                            selectedDate = dateStr;
                            showEventsForDate(dateStr);
                        });
                        dayCounter++;
                    } else {
                        cell.setText("");
                    }
                    rowLayout.addView(cell);
                }
                calendarGrid.addView(rowLayout);
                if (dayCounter > daysInMonth && row < 5) {
                    for (int extraRow = row + 1; extraRow < 6; extraRow++) {
                        LinearLayout emptyRow = new LinearLayout(context);
                        emptyRow.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45)));
                        emptyRow.setOrientation(LinearLayout.HORIZONTAL);
                        emptyRow.setWeightSum(7);
                        for (int c = 0; c < 7; c++) {
                            TextView emptyCell = new TextView(context);
                            LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(
                                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                            emptyCell.setLayoutParams(emptyParams);
                            emptyRow.addView(emptyCell);
                        }
                        calendarGrid.addView(emptyRow);
                    }
                    break;
                }
            }
        }

        // ==================== DATE CLICK EVENTS ====================

        private void showEventsForDate(String date) {
            List<EventModel> eventsForDate = new ArrayList<>();

            if (selectedDateTitle != null) {
                selectedDateTitle.setText("Events on " + date);
            }

            // Convert app date (DD/MM/YYYY) to Firebase date (YYYY/MM/DD)
            String[] dateParts = date.split("/");
            String firebaseFormatDate = date;

            if (dateParts.length == 3) {
                String day = dateParts[0];
                String month = dateParts[1];
                String year = dateParts[2];
                firebaseFormatDate = year + "/" + month + "/" + day;
            }

            // 🔴 TEST: Toast dikhao ke kaunsi date search kar rahe ho
            Toast.makeText(context, "Searching for: " + firebaseFormatDate, Toast.LENGTH_SHORT).show();

            final String searchDate = firebaseFormatDate;

            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    eventsForDate.clear();

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String eventDate = eventSnapshot.child("date").getValue(String.class);

                        if (eventDate != null && eventDate.equals(searchDate)) {
                            String type = eventSnapshot.child("type").getValue(String.class);
                            String title = eventSnapshot.child("title").getValue(String.class);
                            String description = eventSnapshot.child("description").getValue(String.class);
                            String time = eventSnapshot.child("time").getValue(String.class);
                            String venue = eventSnapshot.child("venue").getValue(String.class);

                            if (description == null || description.isEmpty()) {
                                description = "No description available";
                            }

                            EventModel event = new EventModel(
                                    type, title, description, venue,
                                    date, time, "", false
                            );
                            eventsForDate.add(event);
                        }
                    }

                    if (calendarEventsListView != null && context != null) {
                        if (eventsForDate.isEmpty()) {
                            calendarEventsListView.setVisibility(View.GONE);
                            if (noEventsMessage != null) {
                                noEventsMessage.setVisibility(View.VISIBLE);
                                noEventsMessage.setText("No events on this date");
                            }
                            Toast.makeText(context, "No events found for: " + searchDate, Toast.LENGTH_SHORT).show();
                        } else {
                            calendarEventsListView.setVisibility(View.VISIBLE);
                            if (noEventsMessage != null) {
                                noEventsMessage.setVisibility(View.GONE);
                            }

                            EventAdapter adapter = new EventAdapter(context, eventsForDate);
                            calendarEventsListView.setAdapter(adapter);

                            calendarEventsListView.setOnItemClickListener((parent, view1, position, id) -> {
                                EventModel clickedEvent = adapter.getItem(position);

                                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                                intent.putExtra("event_type", clickedEvent.getType());
                                intent.putExtra("event_title", clickedEvent.getTitle());
                                intent.putExtra("event_description", clickedEvent.getDescription());
                                intent.putExtra("event_date", clickedEvent.getDate());
                                intent.putExtra("event_time", clickedEvent.getTime());
                                intent.putExtra("event_venue", clickedEvent.getVenue());
                                startActivity(intent);
                            });

                            Toast.makeText(context, "Found " + eventsForDate.size() + " events", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private int dpToPx(int dp) {
            return (int) (dp * context.getResources().getDisplayMetrics().density);
        }
    }
    // ============= SAVED FRAGMENT (with saved events) =============
    public static class SavedFragment extends Fragment {
        private Context context;
        private TextView savedEventsTitle;
        private View savedUnderlineView;
        private ListView savedListView;
        private EventAdapter adapter;
        private List<EventModel> savedEventsList;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.saved_screen, container, false);

            try {
                savedListView = view.findViewById(R.id.savedListView);
                savedEventsTitle = view.findViewById(R.id.savedEventsTitle);
                savedUnderlineView = view.findViewById(R.id.savedUnderlineView);
                savedEventsList = new ArrayList<>();

                if (savedListView != null && context != null) {
                    adapter = new EventAdapter(context, savedEventsList);
                    savedListView.setAdapter(adapter);

                    // Load saved events
                    loadSavedEvents();

                    // Click listener for saved events
                    savedListView.setOnItemClickListener((parent, view1, position, id) -> {
                        EventModel clickedEvent = adapter.getItem(position);

                        // Open detail activity
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        intent.putExtra("event_type", clickedEvent.getType());
                        intent.putExtra("event_title", clickedEvent.getTitle());
                        intent.putExtra("event_description", clickedEvent.getDescription());
                        intent.putExtra("event_date", clickedEvent.getDate());
                        intent.putExtra("event_time", clickedEvent.getTime());
                        intent.putExtra("event_venue", clickedEvent.getVenue());
                        startActivity(intent);

                        applyClickAnimation(view1);
                    });
                }

                // Apply animations
                applyTitleAnimation();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return view;
        }

        private void loadSavedEvents() {
            // Get saved event IDs from SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("CollegeAlertApp", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("saved_events", "[]");
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            final List<String> savedEventIds = gson.fromJson(json, type);

            final List<String> finalSavedEventIds;
            if (savedEventIds == null) {
                finalSavedEventIds = new ArrayList<>();
            } else {
                finalSavedEventIds = savedEventIds;
            }

            // Get all events from Firebase and filter saved ones
            DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    savedEventsList.clear();

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String type = eventSnapshot.child("type").getValue(String.class);
                        String title = eventSnapshot.child("title").getValue(String.class);
                        String description = eventSnapshot.child("description").getValue(String.class);
                        String date = eventSnapshot.child("date").getValue(String.class);
                        String time = eventSnapshot.child("time").getValue(String.class);
                        String venue = eventSnapshot.child("venue").getValue(String.class);

                        String eventId = title + "_" + date;

                        if (finalSavedEventIds.contains(eventId)) {
                            EventModel event = new EventModel(
                                    type, title, description, venue,
                                    date, time, "", false
                            );
                            savedEventsList.add(event);
                        }
                    }

                    if (adapter != null) {
                        adapter.updateList(savedEventsList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to load saved events", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void applyTitleAnimation() {
            if (savedEventsTitle == null) return;

            savedEventsTitle.setAlpha(0f);
            savedEventsTitle.setScaleX(0.7f);
            savedEventsTitle.setScaleY(0.7f);

            savedEventsTitle.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();

            if (savedUnderlineView != null) {
                savedUnderlineView.setAlpha(0f);
                savedUnderlineView.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setStartDelay(300)
                        .start();
            }

            if (savedListView != null) {
                savedListView.setAlpha(0f);
                savedListView.animate()
                        .alpha(1f)
                        .setDuration(800)
                        .setStartDelay(200)
                        .start();
            }
        }

        private void applyClickAnimation(View view) {
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.95f, 1.0f, 0.95f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(100);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);

            view.startAnimation(scale);
        }
    }
    public static class ProfileFragment extends Fragment {
        private Context context;
        private TextView profileName, profileEmail, profileDepartment, profilePassword, togglePassword, profileImage;
        private Button editProfileButton, logoutButton;
        private boolean isPasswordVisible = false;
        private String actualPassword = "";
        private static final int PICK_IMAGE_REQUEST = 1;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);

            try {
                profileImage = view.findViewById(R.id.profileImage);
                profileName = view.findViewById(R.id.profileName);
                profileEmail = view.findViewById(R.id.profileEmail);
                profileDepartment = view.findViewById(R.id.profileDepartment);
                profilePassword = view.findViewById(R.id.profilePassword);
                togglePassword = view.findViewById(R.id.togglePassword);
                editProfileButton = view.findViewById(R.id.editProfileButton);
                logoutButton = view.findViewById(R.id.logoutButton);

                loadUserData();

                profileImage.setOnClickListener(v -> {
                    applyClickAnimation(v);
                    openImagePicker();
                });

                togglePassword.setOnClickListener(v -> {
                    applyClickAnimation(v);
                    if (isPasswordVisible) {
                        profilePassword.setText("••••••••");
                        isPasswordVisible = false;
                    } else {
                        profilePassword.setText(actualPassword);
                        isPasswordVisible = true;
                    }
                });

                editProfileButton.setOnClickListener(v -> {
                    applyButtonAnimation(v);
                    openEditProfileDialog();
                });

                logoutButton.setOnClickListener(v -> {
                    applyButtonAnimation(v);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), FirstScreenActivity.class));
                    if (getActivity() != null) getActivity().finish();
                });

                applyEnterAnimations(view);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        private void applyEnterAnimations(View view) {
            view.setAlpha(0f);
            view.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();

            if (profileImage != null) {
                profileImage.setAlpha(0f);
                profileImage.setScaleX(0.5f);
                profileImage.setScaleY(0.5f);
                profileImage.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(500)
                        .setStartDelay(100)
                        .start();
            }

            if (profileName != null) {
                profileName.setAlpha(0f);
                profileName.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setStartDelay(300)
                        .start();
            }
        }

        private void applyButtonAnimation(View button) {
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.95f, 1.0f, 0.95f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(150);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);
            button.startAnimation(scale);
        }

        private void applyClickAnimation(View view) {
            ScaleAnimation scale = new ScaleAnimation(
                    1.0f, 0.95f, 1.0f, 0.95f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(100);
            scale.setRepeatMode(Animation.REVERSE);
            scale.setRepeatCount(1);
            view.startAnimation(scale);
        }

        private void openImagePicker() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Toast.makeText(context, "Profile picture selected!", Toast.LENGTH_SHORT).show();
                profileImage.setText("📸");
            }
        }

        // 🔴 FIXED: loadUserData - no duplicate labels
        private void loadUserData() {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                String email = currentUser.getEmail();

                // 🔴 FIX: Database se name load karo, Auth se nahi
                DatabaseReference userRef = FirebaseDatabase.getInstance()
                        .getReference("Users").child(currentUser.getUid());

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // 🔴 Name database se le rahe hain
                            String name = snapshot.child("name").getValue(String.class);
                            String department = snapshot.child("department").getValue(String.class);
                            String password = snapshot.child("password").getValue(String.class);

                            // 🔴 Set name without label
                            if (name != null && !name.isEmpty()) {
                                profileName.setText(name);
                            } else {
                                profileName.setText("User Name");
                            }

                            if (email != null) {
                                profileEmail.setText(email);
                            }

                            // 🔴 Set department without label
                            if (department != null && !department.isEmpty()) {
                                profileDepartment.setText(department);
                            } else {
                                profileDepartment.setText("Not specified");
                            }

                            if (password != null) {
                                actualPassword = password;
                                profilePassword.setText("••••••••");
                            }
                        } else {
                            // Fallback if no data in database
                            if (email != null) {
                                profileEmail.setText(email);
                            }
                            profileName.setText("User Name");
                            profileDepartment.setText("Not specified");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private void openEditProfileDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
            builder.setView(dialogView);

            EditText editName = dialogView.findViewById(R.id.editName);
            EditText editDepartment = dialogView.findViewById(R.id.editDepartment);
            EditText editPassword = dialogView.findViewById(R.id.editPassword);
            Button saveButton = dialogView.findViewById(R.id.saveButton);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);

            // Get current values without labels
            String currentName = profileName.getText().toString();
            String currentDept = profileDepartment.getText().toString();

            editName.setText(currentName);
            editDepartment.setText(currentDept);

            saveButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C76327")));
            cancelButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));

            AlertDialog dialog = builder.create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            }

            saveButton.setOnClickListener(v -> {
                applyClickAnimation(v);
                String newName = editName.getText().toString().trim();
                String newDept = editDepartment.getText().toString().trim();
                String newPass = editPassword.getText().toString().trim();

                if (!newName.isEmpty() && !newDept.isEmpty()) {
                    updateUserData(newName, newDept, newPass);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            });

            cancelButton.setOnClickListener(v -> {
                applyClickAnimation(v);
                dialog.dismiss();
            });

            dialog.show();
        }

        // 🔴 FIXED: updateUserData - no duplicate labels
        private void updateUserData(String name, String department, String password) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) return;

            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users").child(user.getUid());

            Map<String, Object> updates = new HashMap<>();
            updates.put("name", name);
            updates.put("department", department);

            if (!password.isEmpty()) {
                updates.put("password", password);
                actualPassword = password;
            }

            userRef.updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update display without labels
                    profileName.setText(name);
                    profileDepartment.setText(department);
                    if (!password.isEmpty()) {
                        profilePassword.setText("••••••••");
                    }

                    // Update Firebase Auth display name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    user.updateProfile(profileUpdates);

                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }}