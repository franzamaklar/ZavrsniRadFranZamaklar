package com.example.zavrsniradfranzamaklar.inputs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.zavrsniradfranzamaklar.MainActivity;
import com.example.zavrsniradfranzamaklar.utilities.MyObservable;
import com.example.zavrsniradfranzamaklar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InputGeneral extends Fragment implements View.OnClickListener {
    private TextView name;
    private TextView surname;
    private TextView gender;
    private TextView birthdate;
    private EditText etName;
    private EditText etSurname;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Button button;
    private DatePicker datePicker;
    private MyObservable model;
    private DeathList deathList;
    private ArrayList<DeathList> deathLists;
    private Dialog myDialog;


    private DatabaseReference reference =
            FirebaseDatabase.
            getInstance("https://zavrsniradfranzamaklar-18dc3-default-rtdb.firebaseio.com/").
            getReference("Death list:");


    private long maxID = 0;
    private int flag = 0;
    private TextView tvDescription;
    private TextView tvCredit;
    private CardView cardViewTitle;
    private CardView cardViewDescription;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.general_input, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        gender = view.findViewById(R.id.gender);
        birthdate = view.findViewById(R.id.birthdate);
        etName = view.findViewById(R.id.etname);
        etSurname = view.findViewById(R.id.etnsurame);
        radioGroup = view.findViewById(R.id.group);
        radioButtonMale = view.findViewById(R.id.male);
        radioButtonFemale = view.findViewById(R.id.female);
        datePicker = view.findViewById(R.id.date);

        deathList = new DeathList();
        deathLists = new ArrayList<>();
        setUpData(view);
        model = new ViewModelProvider(requireActivity()).get(MyObservable.class);
        model.select(deathList);
        myDialog = new Dialog(view.getContext());
        setupPopUp(view);
    }

    public void setupPopUp(View view) {
        tvDescription = view.findViewById(R.id.tvDescription);
        cardViewTitle = view.findViewById(R.id.cvTitle);
        cardViewDescription = view.findViewById(R.id.cvDescription);
        tvCredit = view.findViewById(R.id.tvCredit);
        myDialog.setContentView(R.layout.popup_entry);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton)v).isChecked();
        switch(v.getId()) {
            case R.id.male:
                if (checked)
                    break;
            case R.id.female:
                if (checked)
                    break;
        }

    }

    public void setUpData(View view){

        button = view.findViewById(R.id.saveStates);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxID = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().length() == 0 || etSurname.getText().length() == 0){
                    Toast.makeText(getContext(), "Fill all fields please", Toast.LENGTH_LONG).show();
                }else {
                    if(flag == 1) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    DeathList deathList = dataSnapshot.getValue(DeathList.class);
                                    if (deathList.getDeathcause() == null) {
                                        Toast.makeText(getContext(), "You have not filled second part of data in object", Toast.LENGTH_LONG).show();
                                        break;
                                    } else
                                        flag = 0;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else if(flag == 0) {
                        deathList.setName(etName.getText().toString().trim());
                        deathList.setSurname(etSurname.getText().toString().trim());
                        deathList.setGender(getGender());
                        deathList.setBirthdate(getDate(String.valueOf(datePicker.getDayOfMonth()),
                                String.valueOf(datePicker.getMonth() + 1),
                                String.valueOf(datePicker.getYear())));
                        reference.child(String.valueOf(maxID + 1)).setValue(deathList).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "First part of data is saved", Toast.LENGTH_LONG).
                                                show();

                                    }
                                });
                        flag = 1;
                        }
                    }
                }
        });
    }

    public String getDate(String day, String month, String year){
        return day + "/" + month + "/" + year;
    }

    public String getGender(){
        String gender;
        int id = radioGroup.getCheckedRadioButtonId();
        if(id == radioButtonFemale.getId()){
            gender = "Žensko";
        }else
            gender = "Muško";
        return gender;
    }
}
