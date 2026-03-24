# CodeAlpha_CollegeAlertApp
College Alert App An Android app that sends campus event alerts to students. Students can see Seminars, Exams, Fests, and Notices instantly.

**App Features**
| Feature | What it does |
|---------|--------------|
| **Login/Signup**| Create account or login with email/password |
| **Home Screen** | See all events. Filter by Seminar, Exam, Fest, Notice |
| **Calendar** | See events on any date. Shows upcoming events for Today, Tomorrow, Day After |
| **Save Events** | Click star icon to save events. View them in Saved tab |
| **Profile** | Edit your name, department, password |
| **Notifications** | Get push alerts when new events are added |
| **Admin Panel** | Add events from any browser (HTML file included)

**What Was Used -**Java – App code
-Android Studio – To build the app
-Firebase – For login, database, and notifications
-XML – For app design

**Files You'll See**
| Folder | Contains |
|--------|----------|
| app/src/main/java/ | All Java code (MainActivity, Login, SignUp, etc.) |
| app/src/main/res/layout/ | All screen designs (XML files) |
| app/src/main/res/drawable/ | Images and button shapes |
| app/src/main/res/values/ | Colors, text, themes |
| app/ | google-services.json (Firebase setup) |
| root folder | admin\_panel.html (to add events) |

**How to Run the App (5 Minutes)**
Step 1: Download the Code

**Option 1 – Git:**
git clone https://github.com/ridamaryam804/CodeAlpha\_CollegeAlertApp.git
**Option 2 – ZIP:**
Go to GitHub → Click Code → Download ZIP → Extract
**Step 2: Open in Android Studio**
Open Android Studio
Click Open
Select the downloaded folder
Wait for the blue loading bar to finish (Gradle sync)
**Step 3: Run the App**
Connect your phone (enable USB debugging) OR start emulator
Click the green  button
App will install on your phone

**How to Add Events (Using Admin Panel)**
Events are added through the Admin Panel, not inside the app.
Open admin\_panel.html file in Chrome or any browser
Fill the form:
Type – Select Seminar / Exam / Fest / Notice
Title – Name of event
Description – Event details
Date – Pick from calendar
Time – Pick time
Venue – Where it happens
Click Add Event
Open the app – The event will appear instantly on:
Home Screen (under all events)
Alerts Screen (complete list)
Upcoming Events section (if date is Today, Tomorrow)
One click in Admin Panel = Event visible to all students instantly.

**Firebase (Already Setup)**
Firebase is already connected. You don't need to do anything.
All events save automatically
Users can sign up/login
Notifications work
If you want to use your own Firebase project:
Go to Firebase Console
Create new project
Add Android app with package name: com.example.collegealertapp
Download google-services.json and put it in app/ folder
Open admin\_panel.html and replace the Firebase config

**What You'll See in App**
Screen	What it looks like
First Screen=	"CampusAlert" with "Get Started" button
Login=	Email, password, "Login" button
Signup=	Name, Email, Department, Password
Home=	Event cards with type (Seminar/Exam/Fest/Notice)
Calendar=	Month view + upcoming events (Today, Tomorrow)
Saved=	Events you saved with star icon
Profile=	Your name, email, department. Edit option available

**Common Issues \& Fixes**
**App won't build?**
Make sure google-services.json is in app/ folder
Click Build → Clean Project then Build → Rebuild Project

**Events not showing?**
Check internet connection
Firebase rules are set to read/write (already done)
Make sure you added events from Admin Panel first

**Admin panel not adding events?**
Open browser console (F12) and check for errors
Make sure Firebase config in admin\_panel.html matches your project

**App crashes on open?**
Uninstall old app and reinstall
Clear app data from phone settings
