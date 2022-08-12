package lee.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView2);
        TextView viewDate = (TextView) findViewById(R.id.viewDate);

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
                                String Date
                                        = dayOfMonth + "/"
                                        + (month + 1) + "/" + year;
//                                String day;
//                                int monthName = month + 1;
//                                if(monthName == 1) {
//                                    day = "January";
//                                } else if (monthName == 2) {
//                                    day = "February";
//                                } else if (monthName == 3) {
//                                    day =
//                                }

                                LocalDate date = LocalDate.of(year, month, dayOfMonth);
                                String monthName = date.getMonth().toString();

                                viewDate.setText(Date);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                                builder.setMessage("Do you plan on coming on the " + dayOfMonth + "th of " + monthName)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // START THE GAME!
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User cancelled the dialog
                                            }
                                        });
                                // Create the AlertDialog object and return
                                builder.create().show();
                            }
                        });

    }

}
