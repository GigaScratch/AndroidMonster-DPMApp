package com.randomone.androidmonsterc3;

import android.content.Context;

import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.randomone.androidmonsterc3.StudentProfileEdit.SHARED_PREFS;

public class ModuleAdapter extends FirestoreRecyclerAdapter<Module, ModuleAdapter.ModuleViewholder> {
    private static final String TAG = "ModuleAdapter";
    private OnItemClickListener listener;
    SharedPreferences sharedPreferences;
    Boolean unlocked = true;



    public ModuleAdapter(@NonNull FirestoreRecyclerOptions<Module> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ModuleAdapter.ModuleViewholder holder, final int position, @NonNull final Module model) {
        holder.code.setText(model.getCode());
        holder.name.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.level.setText("Level " + model.getLevel());
        holder.credits.setText(model.getCredits() + " Credits");

        String id = getSnapshots().getSnapshot(position).getId();
        sharedPreferences = holder.itemView.getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Set<String> completedModules = new HashSet<String>(sharedPreferences.getStringSet("completedModules", new HashSet<String>()));

        List<String> prerequisites = model.getPrerequisites();


        if (prerequisites != null && !prerequisites.isEmpty()) {
            final String requirements = String.join(", ", prerequisites);
            for (String element : prerequisites) {
                if (completedModules.contains(element)){
                    unlocked = true;
                }
                else {
                    unlocked = false;
                    holder.moduleCheckbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.moduleCheckbox.setChecked(false);
                            Toast.makeText(v.getContext(), "You must complete the following classes first: " + requirements, Toast.LENGTH_SHORT).show();
                        }
                    });

                    holder.strokeColour.setStrokeColor(Color.parseColor("#f0f0f0"));
                    holder.backgroundColour.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    break;
                }
            }
        }
        else {
            holder.strokeColour.setStrokeColor(Color.parseColor("#ffdd00"));
            holder.backgroundColour.setBackgroundColor(Color.parseColor("#ffdd00"));
            holder.moduleCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                }
            });
        }


        if (completedModules.contains(model.getCode())){
            holder.moduleCheckbox.setChecked(true);
        }
        else {
            holder.moduleCheckbox.setChecked(false);
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
        CheckBox moduleCheckbox;
        MaterialCardView strokeColour;
        RelativeLayout backgroundColour;

        public ModuleViewholder(@NonNull View view){
            super(view);

            name = view.findViewById(R.id.module_name);
            code = view.findViewById(R.id.module_code);
            description = view.findViewById(R.id.module_description);
            level = view.findViewById(R.id.module_level);
            credits = view.findViewById(R.id.module_credits);

            moduleCheckbox = view.findViewById(R.id.module_complete);
            backgroundColour = view.findViewById(R.id.card_background);
            strokeColour = view.findViewById(R.id.card_stroke);

            moduleCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = getAdapterPosition();
                    String code = getItem(pos).getCode();

                    sharedPreferences = buttonView.getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    Set<String> completedModules = new HashSet<String>(sharedPreferences.getStringSet("completedModules", new HashSet<String>()));
                    if (isChecked == true) {
                        completedModules.add(code);
                    }
                    else {
                        completedModules.remove(code);
                    }
                    sharedPreferences.edit().putStringSet("completedModules", completedModules).apply();


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
