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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    CalendarView calendar;
    MainViewModel viewModel;
    TextView viewDate;
    ProgressBar progressBar;
    Calendar c;
    CalendarView.OnDateChangeListener calendarViewListener;
    int selectedYear;
    int selectedMonth;
    int selectedDayOfMonth;
    Spinner tutors;
    RecyclerView recyclerView;
    String selectedDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        progressBar = findViewById(R.id.progressBar);
        calendar = (CalendarView) findViewById(R.id.calendarView2);
        viewDate = (TextView) findViewById(R.id.textDate1);
        c = Calendar.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        viewModel.loadTutors();

        setCalendarViewListener();
        calendar.setOnDateChangeListener(calendarViewListener);

        int day = c.get(Calendar.DAY_OF_MONTH);
        viewDate.setText("" + day);

        viewModel.getAttendeesWithTutors().observe(AdminActivity.this, studySession -> {
            setupAdapter(studySession);
        });

        viewModel.getUpdateAttendee().observe(AdminActivity.this, viewState -> {
            if (viewState == MainViewModel.ViewState.NOT_EXIST) {
                Toast.makeText(AdminActivity.this, "No one signed up yet", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(null);
            }
            progressBar.setVisibility(View.GONE);
            calendar.setEnabled(true);
        });
    }

    private void setupAdapter(StudySession studySession) {
        StudySessionAdapter adapter= new StudySessionAdapter(this, studySession, viewModel, selectedDate, progressBar);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        calendar.setEnabled(true);
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
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                viewDate.setText("" + dayOfMonth);
                viewModel.getAttendee(selectedDate);
                progressBar.setVisibility(View.VISIBLE);
                calendar.setEnabled(false);
            }
        };
    }
}