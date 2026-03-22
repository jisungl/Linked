# Linked

A tutor-student scheduling Android app built for Sunday Study Room, a local tutoring organization I founded for K-8 students. The club had more students signing up than available tutoring slots could support, so I built this app to manage weekly attendance and automate tutor-student matching.

## Problem

Sunday Study Room sessions ran weekly with a limited number of tutors and physical space. Students needed a way to request attendance for upcoming sessions and tutors needed to see who they were assigned, so this app allows me to match students to tutors each week without doing it manually over text.

## How It Works

The app supports three account types, each with a different view:

**Students** log in and see a calendar. Selecting an upcoming Sunday prompts them to request attendance for that session. They can view their assigned tutor for any date they've signed up for, or cancel their request.

**Tutors** log in to a calendar view that shows which students have been assigned to them on a given date.

**Admins** see all students who have requested attendance for a selected date and assign each student to a tutor using a dropdown. Assignments update in real time for both the student and tutor.

## Architecture

- **MVVM pattern** using a Kotlin `ViewModel` with `LiveData` and `MediatorLiveData` for reactive UI updates across all three views
- **Azure Blob Storage** backend — user accounts and study session data are serialized with Gson and stored as JSON blobs in separate containers for students, tutors, and session records
- **Coroutines** (`viewModelScope` + `Dispatchers.IO`) for all network operations to keep the UI thread free
- **Repository layer** handles all CRUD operations: account creation, login, attendance requests, tutor-student matching, and session updates
- **Role-based routing** — login determines account type and navigates to the corresponding activity (student calendar, tutor calendar, or admin panel)
- **RecyclerView** with a custom adapter in the admin view for displaying attendees and assigning tutors via inline spinners
- **SharedPreferences** for persisting login credentials locally (remember me)

## Tech Stack

- Java, Kotlin
- Android SDK (min SDK 26, target SDK 32)
- Azure Blob Storage (`azure-storage-android`)
- Kotlin Coroutines
- AndroidX Lifecycle (ViewModel, LiveData)
- Gson
- Material Design Components

## Data Model

**Person** — stores name, ID, password, grade level, account type, and a list of date-tutor matching pairs

**StudySession** — stores attendees (list of Persons) and matching results (list of student-tutor pairs) for a given date

Sessions are keyed by date string (e.g., `2024-3-17`) in Azure Blob Storage. Each attendance request or tutor assignment updates both the session blob and the individual student's person blob to keep matching state consistent across views.

## Screens

- **Splash** — checks session state and routes to login or calendar
- **Login** — username/password with show/hide toggle and remember-me checkbox
- **Sign Up** — account creation with role (teacher/student) and grade level selection
- **Student Calendar** — date picker for requesting attendance and viewing assigned tutors
- **Tutor Calendar** — date picker showing assigned students for each session
- **Admin Panel** — calendar + RecyclerView listing attendees with tutor assignment dropdowns