package lee.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import lee.app.MainViewModel.ViewState;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainsignup);

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        EditText newUser = findViewById(R.id.newUser);
        EditText nameInput = findViewById(R.id.nameInput);
        EditText newPass = findViewById(R.id.newPass);
        newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Spinner newAccType = findViewById(R.id.accountType);
        Spinner gradeLevel = findViewById(R.id.grade);
        Button newAcc = findViewById(R.id.newAcc);
        Button pwdButton = findViewById(R.id.showhide2);
        pwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    pwdButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.showpass, 0,0,0);
                    newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else{
                    newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwdButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hiddenpass, 0,0,0);
                }
            }
        });

        newAccType.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        gradeLevel.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        viewModel.getSignUp().observe(this, viewState -> {
            if (viewState == ViewState.SUCCESS) {
                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                Intent myIntent;
                if (Session.person.accountType.equals("Teacher")) {
                    myIntent = new Intent(SignupActivity.this, TutorActivity.class);
                } else {
                    myIntent = new Intent(SignupActivity.this, CalendarActivity.class);
                }
                startActivity(myIntent);
                finish();
            } else if (viewState == ViewState.ALREADY_EXIST) {
                Toast.makeText(this, "Id already exists. Try another ID", Toast.LENGTH_SHORT).show();
            } else if (viewState == ViewState.FAILURE) {
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNewUser = newUser.getText().toString();
                String currentName = nameInput.getText().toString();
                String currentPass = newPass.getText().toString();
                String currentAccType = newAccType.getSelectedItem().toString();
                String currentGrade = gradeLevel.getSelectedItem().toString();

                if(currentNewUser.equals("") || currentName.equals("") || currentPass.equals("") ||
                currentAccType.equals("") || currentGrade.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Person person = new Person(currentName, currentNewUser, currentPass, currentGrade, currentAccType, new ArrayList<>());
                    viewModel.signUp(person);
                }
            }
        });

        Spinner accType = findViewById(R.id.accountType);
        ArrayList<String> accTypeList = new ArrayList<>();
        accTypeList.add("Teacher");
        accTypeList.add("Student");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accTypeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accType.setAdapter(arrayAdapter);
        accType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        Spinner gradeSpinner = findViewById(R.id.grade);
        ArrayList<String> gradeList = new ArrayList<>();
        gradeList.add("Kindergarten");
        gradeList.add("1");
        gradeList.add("2");
        gradeList.add("3");
        gradeList.add("4");
        gradeList.add("5");
        gradeList.add("6");
        gradeList.add("7");
        gradeList.add("8");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gradeList);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(arrayAdapter1);
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName1 = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }
}
