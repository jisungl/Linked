package lee.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView2);
        TextView viewDate = (TextView) findViewById(R.id.viewDate);

        List<Pair<String, Person>> matchingList = Session.person.matching;
        for (int i = 0; i < matchingList.size(); i++) {
            Pair<String, Person> matching = matchingList.get(i);
            String date = matching.first;
            Person tutor = matching.second;
            Toast.makeText(this, "Your requested " + date + ". Assigned tutor is : " + tutor, Toast.LENGTH_SHORT).show();
        }

        calendar.setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {
                                String Date = year + "-" + (month + 1) + "-" + dayOfMonth;
//
                                LocalDate date = LocalDate.of(year, month+1, dayOfMonth);
                                String monthNameTemp = date.getMonth().toString();
                                String monthName = monthNameTemp.substring(0,1) +
                                                    monthNameTemp.substring(1).toLowerCase();

                                viewDate.setText(Date);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                                String dialogMsg = "";
                                if(date.getDayOfWeek().toString() != "SUNDAY") {
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
                                            viewModel.updateAttendee(Date);
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
                        });

        viewModel.getUpdateAttendee().observe(this, viewState -> {
            if (viewState == MainViewModel.ViewState.SUCCESS) {
                Toast.makeText(this, "Your request is accepted", Toast.LENGTH_SHORT).show();
            } else if (viewState == MainViewModel.ViewState.ALREADY_EXIST) {
                Toast.makeText(this, "You already requested for this date", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
