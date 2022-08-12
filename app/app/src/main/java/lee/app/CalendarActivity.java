package lee.app;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar;
        TextView viewDate;
        calendar = (CalendarView)
                findViewById(R.id.calendarView2);
        viewDate = (TextView)
                findViewById(R.id.viewDate);

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
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;

                                viewDate.setText(Date);
                            }
                        });
    }

}
