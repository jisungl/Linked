package lee.app;

import com.google.gson.annotations.SerializedName;

public class Person {
    public String name;
    public String id;
    public String password;
    public String grade;
    public String accountType;

    public Person(String name, String id, String password, String grade, String accountType) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.grade = grade;
        this.accountType = accountType;
    }
}
