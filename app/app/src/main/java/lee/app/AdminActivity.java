package lee.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
    TextView viewDay;
    TextView viewTutor;
    Calendar c;
    Button cancelButton;
    TextView studentName;
    String selectedDate;
    CalendarView.OnDateChangeListener calendarViewListener;
    int selectedYear;
    int selectedMonth;
    int selectedDayOfMonth;
    Spinner tutors;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setCalendarViewListener();
        calendar = (CalendarView) findViewById(R.id.calendarView2);
        viewDate = (TextView) findViewById(R.id.textDate);
        viewDay = (TextView) findViewById(R.id.textDay);
        viewTutor = (TextView) findViewById(R.id.textTutor);
        c = Calendar.getInstance();
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.GONE);
        studentName = (TextView) findViewById(R.id.studentName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        viewModel.getAttendees().observe(this, studySession -> {
            setupAdapter(studySession);
        });

        viewModel.loadTutors();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDate == null) return;

                viewModel.removeAttendee(selectedDate);
            }
        });

        updateUI();
    }

    private void setupAdapter(StudySession studySession) {
        StudySessionAdapter adapter= new StudySessionAdapter(this, studySession);
        recyclerView.setAdapter(adapter);
    }

    private void updateUI() {
        studentName.setVisibility(View.GONE);
        tutors.setVisibility(View.GONE);
        int day = c.get(Calendar.DAY_OF_MONTH);
        viewDate.setText("" + day);
        viewDay.setText(LocalDate.now().getDayOfWeek().name());
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
//
                LocalDate date = LocalDate.of(year, month+1, dayOfMonth);
                String monthNameTemp = date.getMonth().toString();
                String monthName = monthNameTemp.substring(0,1) +
                        monthNameTemp.substring(1).toLowerCase();
                List<Pair<String, Person>> matchingList = Session.person.matching;
                String currentDate = "";
                for (int i = 0; i < matchingList.size(); i++) {
                    Pair<String, Person> matching = matchingList.get(i);
                    currentDate = matching.first;
                    Person tutor = matching.second;
                    if (currentDate.equals(selectedDate)) {
                        viewTutor.setText("Your assigned tutor for this day is " + tutor);
                        cancelButton.setVisibility(View.VISIBLE);
                        break;
                    } else if(date.getDayOfWeek().toString() == "SUNDAY") {
                        viewTutor.setText("You did not request");
                        cancelButton.setVisibility(View.GONE);
                    } else {
                        viewTutor.setText("");
                        cancelButton.setVisibility(View.GONE);
                    }
                }

                viewDate.setText("" + dayOfMonth);
                viewDay.setText(date.getDayOfWeek().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                String dialogMsg = "";
                if(currentDate.equals(selectedDate)) {
                    return;
                } else if(date.getDayOfWeek().toString() != "SUNDAY") {
                    dialogMsg += "There is no class on this day";

                } else if(dayOfMonth == 11 || dayOfMonth== 12 || dayOfMonth == 13) {
                    dialogMsg += "Do you plan on attending on the " + dayOfMonth + "th of " +
                            monthName + "?";
                } else if (dayOfMonth % 10 == 1) {
                    dialogMsg += "Do you plan on attending on the " + dayOfMonth + "st of " +
                            monthName + "?";
                } else if (dayOfMonth % 10 == 2) {
                    dialogMsg += "Do you plan on attending on the " + dayOfMonth + "nd of " +
                            monthName + "?";
                } else if (dayOfMonth % 10 == 3) {
                    dialogMsg += "Do you plan on attending on the " + dayOfMonth + "rd of " +
                            monthName + "?";
                } else {
                    dialogMsg = "Do you plan on attending on the " + dayOfMonth + "th of " +
                            monthName + "?";
                }

                if(date.getDayOfWeek().toString() == "SUNDAY") {
                    builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            viewModel.updateAttendee(selectedDate);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    // Create the AlertDialog object and return
                    builder.create().show();
                } else {
                    builder.setMessage(dialogMsg).setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    // Create the AlertDialog object and return
                    builder.create().show();
                }

            }
        };
    }
}