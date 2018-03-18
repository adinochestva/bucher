package com.plazza.app.main;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.stmt.QueryBuilder;
import com.plazza.app.main.databinding.FragmentDrawerBinding;
import com.plazza.app.main.model.Section;
import com.plazza.app.main.util.MenuAdapter;
import com.plazza.app.main.util.SectionType;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {


    FragmentDrawerBinding binding;
    MenuAdapter adapter;
    List<Section> sectionz = new ArrayList<Section>();
    MyApp appState;

    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appState = ((MyApp) getActivity().getApplicationContext());
        binding = FragmentDrawerBinding.inflate(
                inflater, container, false);
        binding.listMenu.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshContent();

        return binding.getRoot();
    }


    public void refreshContent() {
        try {

            QueryBuilder<Section, Integer> qb = appState.sectionDao.queryBuilder();
            qb.where().eq("parent", 0);
            qb.where().eq("place", 1);
            qb.orderBy("or", true);
            sectionz = qb.query();

            if (appState.getSettingBool("setting")) {
                Section sec = new Section();
                sec.id = 0;
                sec.name = getString(R.string.setting);
                sec.type = SectionType.List.value();
                sec.icon = "\ue136";
                sec.descr="|setting|";
                sectionz.add(sec);
            }

        } catch (java.sql.SQLException e) {
//            finish();
            sectionz = new ArrayList<>();
        }

        adapter = new MenuAdapter(getActivity(), sectionz);
        binding.listMenu.setAdapter(adapter);


    }

}
