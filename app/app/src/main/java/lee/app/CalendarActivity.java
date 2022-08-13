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
//
                                LocalDate date = LocalDate.of(year, month+1, dayOfMonth);
                                String monthNameTemp = date.getMonth().toString();
                                String monthName = monthNameTemp.substring(0,1) +
                                                    monthNameTemp.substring(1).toLowerCase();

                                viewDate.setText(Date);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                                String dialogMsg = "";

                                if(dayOfMonth == 11 || dayOfMonth== 12 || dayOfMonth == 13) {
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
                                builder.setMessage(dialogMsg)
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
