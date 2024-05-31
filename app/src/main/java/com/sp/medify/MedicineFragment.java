package com.sp.medify;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MedicineFragment extends Fragment {

    private Cursor model = null;
    private MedicineAdapter adapter = null;
    private ListView list;
    private MedicineHelper helper = null;
    private TextView empty = null;

    public static MedicineFragment newInstance() {
        MedicineFragment fragment = new MedicineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);

        empty = (TextView) view.findViewById(R.id.empty);
        helper = new MedicineHelper(this.getActivity());
        list = (ListView) view.findViewById(R.id.medicine_list);
        model = helper.getAll();
        adapter = new MedicineAdapter(model);
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);

        setHasOptionsMenu(true);

        return (view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (model != null) model.close();

        model = helper.getAll();

        if (model.getCount() > 0) empty.setVisibility(View.INVISIBLE);
        else empty.setVisibility(View.VISIBLE);

        adapter.swapCursor(model);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_med){
                Intent intent;
                intent = new Intent(MedicineFragment.this.getActivity(), ScanBarcode.class);
                ScanBarcode.ischeckmedID = false;
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    model.moveToPosition(position);
                    String recordID = helper.getID(model);
                    ScanBarcode.medicineDBid = recordID;
                    Intent intent = new Intent(MedicineFragment.this.getActivity(), MedicineForm.class);
                    intent.putExtra("ID", recordID);
                    startActivity(intent);
        }
    };

    class MedicineAdapter extends CursorAdapter {
        MedicineAdapter (Cursor c) {
            super(MedicineFragment.this.getActivity(), c);
        }

        @Override
        public void bindView(View row, Context ctxt, Cursor c) {
            MedicineHolder holder = (MedicineHolder) row.getTag();
            holder.populateFrom(c, helper);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.medicinerow, parent, false);
            MedicineHolder holder = new MedicineHolder(row);

            row.setTag(holder);
            return (row);
        }
    }

    static class MedicineHolder {
        private TextView mName = null;
        private TextView mPurpose = null;

        MedicineHolder (View row) {
            mName = (TextView) row.findViewById(R.id.medName);
            mPurpose = (TextView) row.findViewById(R.id.medPurpose);
        }

        void populateFrom(Cursor c, MedicineHelper helper) {
            mName.setText(helper.getMedicineName(c));
            String temp = helper.getMedicinePurpose(c);
            mPurpose.setText("Purpose: " + temp + " | Dosage: " + helper.getMedicineDosage(c));
        }
    }
}
