package lee.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for the content view.
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        int delay;
        Intent i;
        if(Session.person == null) {
            i = new Intent(SplashActivity.this, MainActivity.class);
            delay = 1000;
        } else {
            i = new Intent(SplashActivity.this, CalendarActivity.class);
            delay = 0;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                startActivity(i);
                // close this activity
                finish();
            }
        }, delay);
    }
}
