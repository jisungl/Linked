package lee.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();

//        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//
//        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView2);
//        TextView viewDate = (TextView) findViewById(R.id.textDate);
//        TextView viewDay = (TextView) findViewById(R.id.textDay);
//        Calendar c = Calendar.getInstance();
//        TextView studentName = (TextView) findViewById(R.id.studentName);
//        Spinner tutors = (Spinner) findViewById(R.id.tutors);
//        studentName.setVisibility(View.VISIBLE);
//        tutors.setVisibility(View.VISIBLE);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        viewDate.setText("" + day);
//        viewDay.setText(LocalDate.now().getDayOfWeek().name());
    }
}
