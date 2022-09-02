package lee.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TutorActivity extends AppCompatActivity {

    CalendarView calendar;
    MainViewModel viewModel;
    TextView viewDate;
    TextView viewDay;
    TextView viewTutor;
    Calendar c;
    String selectedDate;
    CalendarView.OnDateChangeListener calendarViewListener;
    ProgressBar progressBar;
    int selectedYear;
    int selectedMonth;
    int selectedDayOfMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        getSupportActionBar().hide();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setCalendarViewListener();
        calendar = (CalendarView) findViewById(R.id.calendarView2);
        viewDate = (TextView) findViewById(R.id.textDate);
        viewDay = (TextView) findViewById(R.id.textDay);
        viewTutor = (TextView) findViewById(R.id.textTutor);
        progressBar = findViewById(R.id.progressBar);
        c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        viewDate.setText("" + day);

        viewModel.getUpdateAttendee().observe(this, new Observer<MainViewModel.ViewState>() {
            @Override
            public void onChanged(MainViewModel.ViewState viewState) {
                if (viewState != MainViewModel.ViewState.SUCCESS) {
                    viewTutor.setText("You don't have students assigned yet");
                    progressBar.setVisibility(View.GONE);
                    calendar.setEnabled(true);
                }
            }
        });

        viewModel.getAttendees().observe(this, new Observer<StudySession>() {
            @Override
            public void onChanged(StudySession studySession) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < studySession.attendees.size(); i++) {
                    Person student = studySession.attendees.get(i);
                    for (int j = 0; j < student.matching.size(); j++) {
                        Pair<String, Person> pair = student.matching.get(j);
                        if (pair.first.equals(selectedDate)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append(", ");
                            }
                            stringBuilder.append(student.name);
                        }
                    }
                }
                if (stringBuilder.length() == 0) {
                    viewTutor.setText("You don't have students assigned yet");
                } else {
                    viewTutor.setText("Assigned Students: " + stringBuilder.toString());
                }
                progressBar.setVisibility(View.GONE);
                calendar.setEnabled(true);
            }
        });

        updateUI();
    }

    private void updateUI() {
        int day = c.get(Calendar.DAY_OF_MONTH);
//        viewDate.setText("" + day);
//        viewDay.setText(LocalDate.now().getDayOfWeek().name());
        calendar.setOnDateChangeListener(calendarViewListener);
    }

    private void setCalendarViewListener() {
        calendarViewListener = new CalendarView
                .OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(
                    @NonNull CalendarView view,
                    int year,
                    int month,
                    int dayOfMonth)
            {
                selectedYear = year;
                selectedMonth = month;
                selectedDayOfMonth = dayOfMonth;

                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                viewModel.getAttendee(selectedDate);
                progressBar.setVisibility(View.VISIBLE);
                calendar.setEnabled(false);
            }
        };
    }
}
