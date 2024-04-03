package com.skDevs.noteease;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;


public class BlankFragment extends Fragment {

    FloatingActionButton fab;
    EditText searchBar;
    ListView listView;

    ArrayList<Map<String,String>> listItems=new ArrayList<Map<String,String>>();
    SimpleAdapter adapter;


    public BlankFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        Database database = new Database(requireContext().getApplicationContext());

        fab = view.findViewById(R.id.floatingActionButton);
        searchBar = view.findViewById(R.id.search_bar);
        listView = view.findViewById(R.id.listView);

        searchBar.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        createList(view, database, (Editable) s);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_search_24, 0, R.drawable.baseline_clear_24, 0);
                            searchBar.onTouchEvent(MotionEvent.obtain(0,0, MotionEvent.ACTION_UP, searchBar.getWidth()-1, searchBar.getHeight()/2, 0));
                            searchBar.setOnTouchListener((v, event) -> {
                                if (searchBar.getText().length() == 0) return false;
                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                    if (event.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[2].getBounds().width())) {

                                        searchBar.setText("");
                                        return true;
                                    }
                                }
                                return false;
                            });
                        }
                        else {
                            searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_search_24, 0, 0, 0);
                        }

                    }
                }
        );

        createList(view, database, searchBar.getText());

        listView.setOnItemClickListener(
                (parent, view1, position, id) -> {
                    Map<String,String> item = listItems.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", item.get("id"));
                    bundle.putString("title", item.get("title"));
                    bundle.putString("description", item.get("description"));
                    bundle.putString("date", item.get("date"));
                    Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_blankFragment2, bundle);
                }
        );

        fab.getDrawable().setTint(getResources().getColor(R.color.white));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_blankFragment2);
            }
        });

        return view;
    }

    public void createList(View view, Database database, Editable s) {

        listItems = (s.toString().equals("")) ? database.getData() : database.getData(s.toString());

        adapter=new SimpleAdapter(requireContext().getApplicationContext(),
                listItems,
                R.layout.list_item,
                new String[] {"title", "description", "date"},
                new int[] {R.id.title, R.id.description, R.id.date});

        listView.setAdapter(adapter);
    }
}