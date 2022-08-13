package lee.app;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class StudySession {
    public List<Person> attendees = new ArrayList<>();
    public List<Pair<Person, Person>> matching = new ArrayList<>();
}
