package com.richfield.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.richfield.smartpantry.R;
import com.richfield.smartpantry.adapter.PantryAdapter;
import com.richfield.smartpantry.db.DatabaseHelper;
import com.richfield.smartpantry.model.PantryItem;

/**
 * The Pantry List screen: shows every ingredient the user currently has, using
 * a RecyclerView bound to Cloud Firestore. Supports CREATE (via the FAB),
 * EDIT (tap a row) and DELETE (trash icon) - the full CRUD cycle the marker
 * tests in the video (brief Section 5.1).
 */
public class PantryListFragment extends Fragment implements PantryAdapter.OnItemActionListener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = DatabaseHelper.getInstance(requireContext());
        emptyView = view.findViewById(R.id.text_empty);

        RecyclerView recycler = view.findViewById(R.id.recycler_pantry);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PantryAdapter(this);
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        // Navigate to the Add/Edit screen with an explicit Intent (no id = create).
        fab.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddEditIngredientActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        db.getAllPantryItems(new DatabaseHelper.Callback<java.util.List<PantryItem>>() {
            @Override
            public void onSuccess(java.util.List<PantryItem> items) {
                if (!isAdded()) {
                    return;
                }
                adapter.setItems(items);
                emptyView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(@NonNull Exception error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), error.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent intent = new Intent(requireContext(), AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_item_title)
                .setMessage(getString(R.string.delete_item_message, item.getName()))
                .setPositiveButton(R.string.delete, (d, w) -> {
                    db.deletePantryItem(item.getId(), new DatabaseHelper.Callback<Void>() {
                        @Override
                        public void onSuccess(Void ignored) {
                            refresh();
                        }

                        @Override
                        public void onError(@NonNull Exception error) {
                            if (isAdded()) {
                                Toast.makeText(requireContext(), error.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
