package com.example.zavrsniradfranzamaklar.inputs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zavrsniradfranzamaklar.MainActivity;
import com.example.zavrsniradfranzamaklar.MapsActivity;
import com.example.zavrsniradfranzamaklar.ResultsActivity;
import com.example.zavrsniradfranzamaklar.utilities.MyObservable;
import com.example.zavrsniradfranzamaklar.R;
import com.example.zavrsniradfranzamaklar.utilities.RecyclerAdapter;
import com.example.zavrsniradfranzamaklar.utilities.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InputSpecial extends Fragment {

    private TextView deathDate;
    private TextView deathPlace;
    private TextView deathCause;
    private EditText etmaps;
    private DatePicker dDate;
    private TimePicker timePicker;
    private Spinner spinner;
    private Button submit;
    private Button mapview;
    private Button results;
    private ArrayAdapter<CharSequence> adapter;

    private DeathList deathList;
    private DatabaseReference reference;
    private long maxID = 0;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_input, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deathDate = view.findViewById(R.id.deathdate);
        deathPlace = view.findViewById(R.id.deathplace);
        deathCause = view.findViewById(R.id.deathcause);
        dDate = view.findViewById(R.id.ddate);
        timePicker = view.findViewById(R.id.dtime);
        spinner = view.findViewById(R.id.causemenu);
        mapview = view.findViewById(R.id.viewmap);
        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchActivity(getActivity(), MapsActivity.class, false);
            }
        });
        etmaps = view.findViewById(R.id.etmaps);
        setupAdapter();
        MyObservable model = new ViewModelProvider(requireActivity()).get(MyObservable.class);
        model.getSelected().observe(getViewLifecycleOwner(), new Observer<DeathList>() {
            @Override
            public void onChanged(DeathList deathList) {
                InputSpecial.this.deathList = deathList;
            }

        });
        setupFirebase(view);
        setupResults(view);
    }

    private void setupResults(View view) {
        results = view.findViewById(R.id.results);
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchActivity(getActivity(), ResultsActivity.class, false);
            }
        });

    }



    public void setupAdapter() {
        adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.causemenu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setupFirebase(View view) {
        reference = FirebaseDatabase.getInstance("https://zavrsniradfranzamaklar-18dc3-default-rtdb.firebaseio.com/").getReference("Death list:");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxID = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setUpData(view);
    }

    void setUpData(View view){

        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etmaps.getText().length() == 0){
                    Toast.makeText(getContext(), "Fill all fields please", Toast.LENGTH_LONG).show();
                }else {
                    deathList.setDeathdate(getDate(String.valueOf(dDate.getDayOfMonth()),
                            String.valueOf(dDate.getMonth() + 1),
                            String.valueOf(dDate.getYear())));
                    deathList.setDeathtime(getTime(timePicker.getHour(), timePicker.getMinute()));
                    deathList.setDeathplace(etmaps.getText().toString().trim());
                    deathList.setDeathcause(getCause());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot lastsnapshot = snapshot.child(String.valueOf(maxID));
                            lastsnapshot.getRef().setValue(deathList).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Second part of data is saved", Toast.LENGTH_LONG).
                                            show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


    public String getDate(String day, String month, String year){
        return day + "/" + month + "/" + year;
    }


    public String getTime(int hour, int minute){
        if(minute < 10) {
            return String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
        }else
            return String.valueOf(hour) + ":"  + String.valueOf(minute);
    }

    public String getCause(){
        String cause;
        cause = spinner.getSelectedItem().toString();

        return cause;
    }

}
