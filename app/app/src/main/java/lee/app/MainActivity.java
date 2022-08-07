package lee.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import lee.app.MainViewModel.ViewState;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        EditText usernameInput = findViewById(R.id.usernameInput);
        Button loginButton = findViewById(R.id.login_button);
        Button createAcc = findViewById(R.id.createAcc);
        EditText passwordInput = findViewById(R.id.passwordInput);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(myIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameCurrentInput = usernameInput.getText().toString();
                String passwordCurrentInput = passwordInput.getText().toString();
                if(userNameCurrentInput.equals("") && passwordCurrentInput.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Username and Password.", Toast.LENGTH_SHORT).show();
                } else if(userNameCurrentInput.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Username.", Toast.LENGTH_SHORT).show();
                } else if(passwordCurrentInput.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Password.", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.login(userNameCurrentInput, passwordCurrentInput);
                }
            }
        });

        viewModel.getLogin().observe(this, viewState -> {
            if (viewState == ViewState.SUCCESS) {
                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
            } else if (viewState == ViewState.WRONG_PASSWORD) {
                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
            } else if (viewState == ViewState.NOT_EXIST) {
                Toast.makeText(this, "Id doesn't exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(this, "Hi Jisung", Toast.LENGTH_SHORT).show();
    }
}
//public class Activity1 extends MainActivity {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mainsignup);
//
//        Button next = (Button) findViewById(R.id.createAcc);
//        next.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), Activity1.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });
//    }
//}