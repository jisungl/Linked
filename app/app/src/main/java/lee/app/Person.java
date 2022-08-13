package lee.app;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Person {
    public String name;
    public String id;
    public String password;
    public String grade;
    public String accountType;
    public List<Pair<String, Person>> matching = new ArrayList<>();

    public Person(String name, String id, String password, String grade, String accountType, List<Pair<String, Person>> matching) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.grade = grade;
        this.accountType = accountType;
        this.matching = matching;
    }
}
