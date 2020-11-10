package com.randomone.androidmonsterc3;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends FirestoreRecyclerAdapter<Module, ModuleAdapter.ModuleViewholder> {
    private static final String TAG = "ModuleAdapter";
    private OnItemClickListener listener;
    private boolean isActive;
    private boolean isFinished;


    public ModuleAdapter(@NonNull FirestoreRecyclerOptions<Module> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ModuleAdapter.ModuleViewholder holder, final int position, @NonNull Module model) {
        holder.code.setText(model.getCode());
        holder.name.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.level.setText("Level " + model.getLevel());
        holder.credits.setText(model.getCredits() + " Credits");

        boolean isActive = model.getisActive();
        Log.d(TAG, "isActive: " + isActive);
        if (isActive == false) {
            holder.layouttest.setBackgroundColor(Color.parseColor("#f0f0f0")); //test colour please change
        }else {
            holder.layouttest.setBackgroundColor(Color.parseColor("#ffdd00")); //test colour please change
        }



    }

    @NonNull
    @Override
    public ModuleAdapter.ModuleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_card, parent, false);
        return new ModuleAdapter.ModuleViewholder(view);
    }

    public class ModuleViewholder extends RecyclerView.ViewHolder {

        TextView name, code, description, level, credits;
        RelativeLayout layouttest;
        public CheckBox moduleComplete;
        Boolean isActive, isFinished;
        List<DocumentSnapshot> myListOfDocuments;

        public ModuleViewholder(@NonNull View view){
            super(view);

            name = view.findViewById(R.id.module_name);
            code = view.findViewById(R.id.module_code);
            description = view.findViewById(R.id.module_description);
            level = view.findViewById(R.id.module_level);
            credits = view.findViewById(R.id.module_credits);
            layouttest = view.findViewById(R.id.module_background);

            moduleComplete = (CheckBox) view.findViewById(R.id.module_complete);

            FirebaseFirestore.getInstance()
                    .collection("modules")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                myListOfDocuments = task.getResult().getDocuments();
                            }
                        }
                    });

            moduleComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    DocumentSnapshot pos = getSnapshots().getSnapshot(getAdapterPosition());
                    if (moduleComplete.isChecked() == true){
                        Module m1 = pos.toObject(Module.class);
                        m1.setisFinished(true);
                        Log.d(TAG, "" + m1.getisFinished());
                        for (DocumentSnapshot ds : myListOfDocuments){
                            Module m = ds.toObject(Module.class);
                            m.setisActive(moduleisactive(m));
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });



        }

        public boolean moduleisactive(Module m){
            boolean areactive = true;
            if (m.getPrerequisites() == null || m.getPrerequisites().size() == 0){
                return areactive;
            }else{
                for (String pre : m.getPrerequisites()){
                    for (DocumentSnapshot ds : myListOfDocuments){
                        Module mo = ds.toObject(Module.class);
                        if (pre.equals(mo.getCode())){
                            if (mo.getisFinished() == false){
                                areactive = false;
                            }
                        }
                    }
                }
                return areactive;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void deleteModule(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

}
