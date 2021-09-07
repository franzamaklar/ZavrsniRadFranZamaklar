package com.example.zavrsniradfranzamaklar.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zavrsniradfranzamaklar.R;
import com.example.zavrsniradfranzamaklar.inputs.DeathList;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<DeathList>  deathLists;
    private Context context;

    public RecyclerAdapter(Context context, ArrayList<DeathList> deathLists){
        this.deathLists = deathLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DeathList deathList = deathLists.get(position);
        holder.name.setText("Ime : " + deathList.getName());
        holder.surname.setText("Prezime : " + deathList.getSurname());
        holder.gender.setText("Spol : " + deathList.getGender());
        holder.birthdate.setText("Datum rođenja : " + deathList.getBirthdate());
        holder.deathdate.setText("Datum smrti : " + deathList.getDeathdate());
        holder.deathtime.setText("Vrijeme smrti : " + deathList.getDeathtime());
        holder.deathplace.setText("Mjesto smrti : " + deathList.getDeathplace());
        holder.deathcause.setText("Uzrok smrti : " + deathList.getDeathcause());
    }

    @Override
    public int getItemCount() {
        return deathLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, surname, gender, birthdate, deathdate, deathtime, deathplace, deathcause;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            surname = itemView.findViewById(R.id.tvSurname);
            gender = itemView.findViewById(R.id.tvGender);
            birthdate = itemView.findViewById(R.id.tvBirthDate);
            deathdate = itemView.findViewById(R.id.tvDeathDate);
            deathtime = itemView.findViewById(R.id.tvDeathTime);
            deathplace = itemView.findViewById(R.id.tvDeathPlace);
            deathcause = itemView.findViewById(R.id.tvDeathCause);


        }
    }


}
