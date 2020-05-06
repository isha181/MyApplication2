package com.ashi.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SecondFragment extends Fragment {


    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView category_recycler = view.findViewById(R.id.category_recycler);
        category_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        db.collection("categories").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Category> categories = queryDocumentSnapshots.toObjects(Category.class);
                CategoryAdapter adapter = new CategoryAdapter(getActivity(), R.layout.card_category, categories);
                category_recycler.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(category_recycler, "error " + e.getMessage(), BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("check connection", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //todo open internet settings using intent
                            }
                        }).show();
            }
        });

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

        Context context;
        LayoutInflater inflater;
        List<Category> categories;
        int layout;

        public CategoryAdapter(Context context, int layout, List<Category> categories) {
            this.context = context;
            this.categories = categories;
            this.layout = layout;
            try {
                inflater = LayoutInflater.from(context);
            } catch (Exception e) {

            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(layout, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Category category = categories.get(position);
            holder.option.setText(category.name);
             Glide.with(context).load(category.image).into(holder.image)
            holder.card.setTag(category);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView option;
            CardView card;
            ImageView image;

            public Holder(@NonNull View itemView) {
                super(itemView);
                option = itemView.findViewById(R.id.text);
                card = itemView.findViewById(R.id.card);
                image = itemView.findViewById(R.id.image);
                card.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                Category category = (Category) v.getTag();
                Bundle extraInfo = new Bundle();
                extraInfo.putString("category_name", category.name);

            }
        }
    }

}
