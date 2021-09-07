package com.example.zavrsniradfranzamaklar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zavrsniradfranzamaklar.inputs.DeathList;
import com.example.zavrsniradfranzamaklar.utilities.RecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<DeathList> deathLists;
    private DatabaseReference reference = FirebaseDatabase.getInstance("https://zavrsniradfranzamaklar-18dc3-default-rtdb.firebaseio.com/").getReference("Death list:");
    private Button returnButton;
    private int maleValue = 0;
    private int femaleValue = 0;
    private Spinner primarySpinner;
    private ArrayAdapter<CharSequence> primarySpinnerAdapter;
    private Spinner secondarySpinner;
    private ArrayAdapter<CharSequence> secondarySpinnerAdapter;

    private boolean flag = false;
    private BarChart mBarChart;

    private int cancerValue = 0;
    private int hstrokeValue = 0;
    private int bstrokeValue = 0;
    private int suicideValue = 0;
    private int []months = new int[12];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setUpAdapter();
        setUpRecycler();
    }

    public void setUpRecycler(){

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deathLists = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(this, deathLists);
        recyclerView.setAdapter(recyclerAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DeathList deathList = dataSnapshot.getValue(DeathList.class);
                    deathLists.add(deathList);
                }
                setUpBars(deathLists);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        returnButton = findViewById(R.id.btnBack);
        returnButton.setOnClickListener(v -> endIt());
    }

    private void endIt(){
        finish();
    }

    void setUpBars(ArrayList<DeathList> deathLists){
        mBarChart = findViewById(R.id.barchart);
        primarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (deathLists.size() > 0) {
                    if (parent.getItemAtPosition(position).equals("Statistika po spolu")) {
                        mBarChart.clearChart();
                        if (!flag) {
                            for (int i = 0; i < deathLists.size(); i++) {
                                if(deathLists.get(i).getGender() == null){
                                    Toast.makeText(getApplicationContext(), "Data missing at " + i + " data field", Toast.LENGTH_LONG).show();
                                }else {
                                    if (deathLists.get(i).getGender().equals("Muško")) {
                                        femaleValue++;
                                    } else if (deathLists.get(i).getGender().equals("Žensko")) {
                                        maleValue++;
                                    }
                                }
                            }
                            mBarChart.addBar(new BarModel("Ženski spol", femaleValue, 0xFF873F56));
                            mBarChart.addBar(new BarModel("Muški spol", maleValue, 0xFF56B7F1));
                            mBarChart.startAnimation();
                            flag = true;
                        } else {
                            mBarChart.clearChart();
                            for (int i = 0; i < deathLists.size(); i++) {
                                if (deathLists.get(i).getGender() == null) {
                                    Toast.makeText(getApplicationContext(), "Data missing at " + i + " data field", Toast.LENGTH_LONG).show();
                                } else {
                                     if (deathLists.get(i).getGender().equals("Žensko")) {
                                         femaleValue++;
                                     }
                                     if (deathLists.get(i).getGender().equals("Muško")) {
                                        maleValue++;
                                    }
                                }
                            }
                            mBarChart.addBar(new BarModel("Ženski spol", femaleValue, 0xFF873F56));
                            mBarChart.addBar(new BarModel("Muški spol", maleValue, 0xFF56B7F1));
                            mBarChart.startAnimation();
                        }
                        maleValue = 0;
                        femaleValue = 0;
                    } else if (parent.getItemAtPosition(position).equals("Statistika po uzroku smrti")) {
                        if (!flag) {
                            for (int i = 0; i < deathLists.size(); i++) {
                                if(deathLists.get(i).getDeathcause() == null){
                                    Toast.makeText(getApplicationContext(), "Data missing at " + i + " data field", Toast.LENGTH_LONG).show();
                                }else {
                                    if (deathLists.get(i).getDeathcause().equals("Karcinom")) {
                                        cancerValue++;
                                    }
                                    if (deathLists.get(i).getDeathcause().equals("Oslabljeno srce")) {
                                        hstrokeValue++;
                                    }
                                    if (deathLists.get(i).getDeathcause().equals("Moždani udar")) {
                                        bstrokeValue++;
                                    }
                                    if (deathLists.get(i).getDeathcause().equals("Samoubojstvo")) {
                                        suicideValue++;
                                    }
                                }
                            }
                            mBarChart.addBar(new BarModel("Karcinom", cancerValue, 0xFF873F56));
                            mBarChart.addBar(new BarModel("Oslablj. srce", hstrokeValue, 0xFA56B7F1));
                            mBarChart.addBar(new BarModel("Moždani udar", bstrokeValue, 0xFF873FBD));
                            mBarChart.addBar(new BarModel("Suicid", suicideValue, 0xFF56B7A4));
                            mBarChart.startAnimation();
                            flag = true;
                        } else {
                            mBarChart.clearChart();
                            for (int i = 0; i < deathLists.size(); i++) {
                                if(deathLists.get(i).getDeathcause() == null){
                                    Toast.makeText(getApplicationContext(), "Data missing at " + i + " data field", Toast.LENGTH_LONG).show();
                                }else {
                                    if (deathLists.get(i).getDeathcause().equals("Karcinom")) {
                                        cancerValue++;
                                    }
                                    if (deathLists.get(i).getDeathcause().equals("Oslabljeno srce")) {
                                        hstrokeValue++;
                                    }
                                    if (deathLists.get(i).getDeathcause().equals("Moždani udar")) {
                                        bstrokeValue++;
                                    }
                                    if (deathLists.get(i).getDeathcause().equals("Samoubojstvo")) {
                                        suicideValue++;
                                    }
                                }
                            }
                            mBarChart.addBar(new BarModel("Karcinom", cancerValue, 0xFF873F56));
                            mBarChart.addBar(new BarModel("Oslablj. srce", hstrokeValue, 0xFA56B7F1));
                            mBarChart.addBar(new BarModel("Moždani udar", bstrokeValue, 0xFF873FBD));
                            mBarChart.addBar(new BarModel("Suicid", suicideValue, 0xFF56B7A4));
                            mBarChart.startAnimation();
                        }
                        cancerValue = 0;
                        hstrokeValue = 0;
                        bstrokeValue = 0;
                        suicideValue = 0;
                    } else
                        mBarChart.clearChart();
                }else
                Toast.makeText(getApplicationContext(), "No data in database", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        secondarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(deathLists.size() > 0){
                        mBarChart.clearChart();
                        for(int i = 0; i<deathLists.size();i++) {
                            if (deathLists.get(i).getDeathdate() == null) {
                                Toast.makeText(getApplicationContext(), "Data missing at " + i + " data field", Toast.LENGTH_LONG).show();
                            } else {
                                String[] values = deathLists.get(i).getDeathdate().split("/");
                                int month = Integer.parseInt(values[1]);
                                for (int j = 0; j < months.length; j++) {
                                    if (j + 1 == month) {
                                        months[j] = months[j] + 1;
                                    }
                                }
                            }
                        }

                        if(parent.getItemAtPosition(position).equals("Siječanj")){
                        mBarChart.addBar(new BarModel("Broj umrlih osoba u siječnju", months[0], 0xFF34495E));
                        }
                        if(parent.getItemAtPosition(position).equals("Veljača")){
                        mBarChart.addBar(new BarModel("Broj umrlih osoba u veljači", months[1], 0xFFDAF7A6));
                        }
                        if(parent.getItemAtPosition(position).equals("Ožujak")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u ožujku", months[2], 0xFF5D6D7E));

                        }
                        if(parent.getItemAtPosition(position).equals("Travanj")){
                        mBarChart.addBar(new BarModel("Broj umrlih osoba u travnju", months[3], 0xFFFFC300));

                        }
                        if(parent.getItemAtPosition(position).equals("Svibanj")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u svibnju", months[4], 0xFFFF5733));

                        }
                        if(parent.getItemAtPosition(position).equals("Lipanj")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u lipanj", months[5], 0xFFC70039));

                        }
                        if(parent.getItemAtPosition(position).equals("Srpanj")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u srpnju", months[6], 0xFFF4D03F));

                        }
                        if(parent.getItemAtPosition(position).equals("Kolovoz")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u kolovozu", months[7], 0xFF138D75));

                        }
                        if(parent.getItemAtPosition(position).equals("Rujan")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u rujnu", months[8], 0xFFCA6F1E));

                        }
                        if(parent.getItemAtPosition(position).equals("Listopad")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u listopadu", months[9], 0xFF76448A));

                        }
                        if(parent.getItemAtPosition(position).equals("Studeni")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u studenom", months[10], 0xFF979A9A));

                        }
                        if(parent.getItemAtPosition(position).equals("Prosinac")){
                            mBarChart.addBar(new BarModel("Broj umrlih osoba u prosincu", months[11], 0xFFF1C40F));

                        }
                        if(parent.getItemAtPosition(position).equals("Cjelokupni pregled podataka")){
                            mBarChart.addBar(new BarModel("01.", months[0], 0xFF34495E));
                            mBarChart.addBar(new BarModel("02.", months[1], 0xFFDAF7A6));
                            mBarChart.addBar(new BarModel("03.", months[2], 0xFF5D6D7E));
                            mBarChart.addBar(new BarModel("04.", months[3], 0xFFFFC300));
                            mBarChart.addBar(new BarModel("05.", months[4], 0xFFAF7AC5));
                            mBarChart.addBar(new BarModel("06.", months[5], 0xFFC70039));
                            mBarChart.addBar(new BarModel("07.", months[6], 0xFFF4D03F));
                            mBarChart.addBar(new BarModel("08.", months[7], 0xFF138D75));
                            mBarChart.addBar(new BarModel("09.", months[8], 0xFFCA6F1E));
                            mBarChart.addBar(new BarModel("10.", months[9], 0xFF76448A));
                            mBarChart.addBar(new BarModel("11.", months[10], 0xFF979A9A));
                            mBarChart.addBar(new BarModel("12.", months[11], 0xFFF1C40F));
                            mBarChart.startAnimation();
                        }
                        for(int i=0;i<months.length;i++){
                            months[i] = 0;
                        }
                }else
                    Toast.makeText(getApplicationContext(), "No data in database", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setUpAdapter(){
        primarySpinner = findViewById(R.id.statisticsSpinner);
        secondarySpinner = findViewById(R.id.monthSpinner);
        primarySpinner.setPrompt("Opcije po statistici");
        secondarySpinner.setPrompt("Opcije po mjesecu");
        primarySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.statistics_array, android.R.layout.simple_spinner_item);
        primarySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        primarySpinner.setAdapter(primarySpinnerAdapter);
        secondarySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        secondarySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondarySpinner.setAdapter(secondarySpinnerAdapter);
    }
}