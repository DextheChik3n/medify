package com.sp.medify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HospitalFragment extends Fragment{
    private List<Hospital> hospitals = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewadapter;
    JSONArray array;

    public static HospitalFragment newInstance() {
        HospitalFragment fragment = new HospitalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);
        super.onCreate(savedInstanceState);

        try {
            String json;
            try {
                InputStream is = getActivity().getAssets().open("Hospitalinfo.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }

            JSONObject json2 = new JSONObject(json);
            JSONArray jsonArray = json2.getJSONArray("hospitals");

            for (int i = 0; i < jsonArray.length(); i++) {
                Hospital hospital = new Hospital();
                JSONObject AllHospital = jsonArray.getJSONObject(i);

                hospital.setHospitalname(AllHospital.getString("hospitalname"));
                hospital.setAddress(AllHospital.getString("address"));
                hospital.setGeneraltelephone(AllHospital.getString("generaltelephone"));
                hospital.setEmergencytelephone(AllHospital.getString("emergencytelephone"));
                hospital.setEmail(AllHospital.getString("email"));
                hospital.setWebsite(AllHospital.getString("website"));
                hospital.setVisitinghours(AllHospital.getString("visitinghours"));
                hospital.setImage_url(AllHospital.getString("imageurl"));

                hospitals.add(hospital);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerViewadapter = new HospitalAdapter(this.getContext(),hospitals);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewadapter);

        return (view);
    }

}
