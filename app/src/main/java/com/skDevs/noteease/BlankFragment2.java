package com.skDevs.noteease;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BlankFragment2 extends Fragment {

    EditText titleText;
    EditText descriptionText;
    Button saveButton;

    Toolbar toolbar;

    String id;
    String title;
    String description;
    String date;


    public BlankFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank2, container, false);
        titleText = view.findViewById(R.id.titleText);
        descriptionText = view.findViewById(R.id.descriptionText);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requireActivity().onBackPressed();
                    }
                });
        saveButton = view.findViewById(R.id.button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });
        Bundle bundle = this.getArguments();

        if (bundle == null) {
            toolbar.setTitle("Add Note");
            id = null;
            title = null;
            description = null;
            date = null;
        } else {

            toolbar.setTitle("Edit Note");
            toolbar.inflateMenu(R.menu.blank_fragment_menu);
            toolbar.setOnMenuItemClickListener(
                    item -> {
                        deleteNote();
                        return true;
                    });

            id = bundle.getString("id");
            title = bundle.getString("title");
            description = bundle.getString("description");
            date = bundle.getString("date");

            titleText.setText(title);
            descriptionText.setText(description);
        }
        return view;
    }


    public void saveNote(View view) {
        Date date = new Date();

        DateFormat format = SimpleDateFormat.getDateTimeInstance();
        String formattedDate = format.format(date);
        boolean isInserted = false;
        try {
            Database database = new Database(requireContext().getApplicationContext());


            isInserted = (id != null) ? database.updateData(id, titleText.getText().toString(), descriptionText.getText().toString(), this.date)
             : database.addData(titleText.getText().toString(), descriptionText.getText().toString(), formattedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(requireContext().getApplicationContext(), isInserted ? "Note saved" : "Note not saved", Toast.LENGTH_SHORT).show();

        requireActivity().onBackPressed();
    }

    public void deleteNote() {
        Database database = new Database(requireContext().getApplicationContext());
        boolean isDeleted = database.deleteData(id);
        Toast.makeText(requireContext().getApplicationContext(), isDeleted ? "Note deleted" : "Note not deleted", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}