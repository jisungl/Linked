package lee.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Button pwdButton = findViewById(R.id.showHide);
        pwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordInput.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    pwdButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.showpass, 0,0,0);
                    passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else{
                    passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwdButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hiddenpass, 0,0,0);
                }
            }
        });
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
                Intent myIntent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(myIntent);
            } else if (viewState == ViewState.WRONG_PASSWORD) {
                Toast.makeText(this, "The Password is Incorrect", Toast.LENGTH_SHORT).show();
            } else if (viewState == ViewState.NOT_EXIST) {
                Toast.makeText(this, "That username does not exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
