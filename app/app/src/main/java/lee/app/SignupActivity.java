package lee.app;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainsignup);

        EditText newUser = findViewById(R.id.newUser);
        EditText nameInput = findViewById(R.id.nameInput);
        EditText newPass = findViewById(R.id.newPass);
        Spinner newAccType = findViewById(R.id.accountType);
        Spinner gradeLevel = findViewById(R.id.grade);
        Button newAcc = findViewById(R.id.newAcc);

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
