package lee.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudySessionAdapter extends RecyclerView.Adapter<StudySessionAdapter.ViewHolder> {

    StudySession data;
    Context context;

    public StudySessionAdapter(Context context, StudySession data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_study_session, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        data.
//        holder.nameTextView.setText(data.get(position).getName());
//        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Clicked on"+data.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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