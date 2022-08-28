package lee.app;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudySessionAdapter extends RecyclerView.Adapter<StudySessionAdapter.ViewHolder> {

    StudySession data;
    Context context;
    List<Person> tutors;
    List<String> tutorsName;
    MainViewModel viewModel;
    String date;
    ArrayAdapter<String> arrayAdapter;
    ProgressBar progressBar;

    public StudySessionAdapter(Context context, StudySession data, MainViewModel viewModel, String date, ProgressBar progressBar) {
        this.data = data;
        this.context = context;
        this.tutors = viewModel.getTutors().getValue();
        this.viewModel = viewModel;
        this.date = date;
        this.progressBar = progressBar;
        tutorsName = new ArrayList<>();
        tutorsName.add("");
        for (int i = 0; i < tutors.size(); i++) {
            tutorsName.add(tutors.get(i).name);
        }
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tutorsName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i <data.attendees.size(); i++) {
            Person person = data.attendees.get(i);
            Log.e("minmin", "Init: " + person.name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_study_session, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("minmin", "onBindViewHolder: " + position + ", name: " + data.attendees.get(position).name);

        holder.student.setText(data.attendees.get(position).name);

        holder.tutors.setAdapter(arrayAdapter);

        Person person = data.attendees.get(position);
        for (int i = 0; i < person.matching.size(); i++) {
            Pair<String, Person> pair = person.matching.get(i);
            if (pair.first.equals(date) && pair.second != null) {
                for (int j = 0; j < tutorsName.size(); j++) {
                    if (pair.second.name.equals(tutorsName.get(j))) {
                        holder.tutors.setSelection(j);
                    }
                }
            }
        }

//        viewHolder.tutors.setSelection(Adapter.NO_SELECTION, true);
        holder.tutors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int tutorPosition, long id) {
                Person person = data.attendees.get(holder.getAdapterPosition());
                for (int i = 0; i < person.matching.size(); i++) {
                    Pair<String, Person> pair = person.matching.get(i);
                    if (pair.first.equals(date) && pair.second != null) {
                        for (int j = 0; j < tutorsName.size(); j++) {
                            if (pair.second.name.equals(tutorsName.get(tutorPosition))) {
                                return;
                            }
                        }
                    }
                }

                Log.e("minmin", "onItemSelected: " + tutorPosition);
                Person selectedStudent = data.attendees.get(holder.getAdapterPosition());

                Person selectedTutor = null;
                if (tutorPosition > 0) {
                    selectedTutor = tutors.get(tutorPosition - 1);
                }

                viewModel.updateMatchingForStudent(selectedStudent, selectedTutor, date, data);
                progressBar.setVisibility(VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.attendees.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView student;
        public Spinner tutors;

        public ViewHolder(View itemView) {
            super(itemView);
            tutors = (Spinner) itemView.findViewById(R.id.tutorList);
            student = (TextView) itemView.findViewById(R.id.students);
        }
    }
}