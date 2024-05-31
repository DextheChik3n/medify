package com.sp.medify;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicineForm extends AppCompatActivity implements View.OnClickListener {

    private TextView medicineID;
    private TextView lblmedicineName;
    private EditText medicineName;
    private EditText medicineDosage;
    private Spinner medicineTypes;
    private TextView medicineTypedisplay;
    private EditText medicinePurpose;
    private EditText medicineExpiry;
    private EditText medicineRemarks;
    private Button buttonSave;
    private Button buttonDelete;
    private String medicineDBID = "";
    private Button buttonIdentify;
    private MedicineHelper helper = null;
    private ActionBar actionBar;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_form);

        medicineID = (TextView) findViewById(R.id.medicine_ID);
        medicineName = (EditText) findViewById(R.id.medicine_name);
        lblmedicineName = (TextView) findViewById(R.id.txtMedName);
        medicineDosage = (EditText) findViewById(R.id.medicine_dosage);
        medicineTypes = (Spinner) findViewById(R.id.medicine_types);
        medicinePurpose = (EditText) findViewById(R.id.medicine_purpose);
        medicineExpiry = (EditText) findViewById(R.id.medicine_expire);
        medicineExpiry.setOnClickListener(this);
        medicineRemarks = (EditText) findViewById(R.id.medicine_remarks);

        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);
        buttonDelete = (Button) findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(this);
        buttonIdentify = (Button) findViewById(R.id.button_identify);
        buttonIdentify.setOnClickListener(this);
        medicineTypedisplay = (TextView) findViewById(R.id.medicine_type_display);
        helper = new MedicineHelper(this);
        actionBar = getSupportActionBar();
        calendar = Calendar.getInstance();

        medicineDBID = getIntent().getStringExtra("ID");

        if (medicineDBID != null) {
            load();
        }

        if (getIntent().getStringExtra("med_id") != null) {
            medicineID.setText(getIntent().getStringExtra("med_id"));
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void load() {
        Cursor c = helper.getById(medicineDBID);
        c.moveToFirst();

        medicineID.setText(helper.getMedicineID(c));
        actionBar.setTitle(helper.getMedicineName(c));
        medicineDosage.setText(helper.getMedicineDosage(c));

        if (helper.getMedicineType(c).equals("Liquid")) medicineTypedisplay.setText("Liquid");
        else if (helper.getMedicineType(c).equals("Tablet")) medicineTypedisplay.setText("Tablet");
        else medicineTypedisplay.setText("Capsule");

        medicinePurpose.setText(helper.getMedicinePurpose(c));
        medicineExpiry.setText(helper.getMedicineExpiry(c));
        medicineRemarks.setText(helper.getMedicineRemarks(c));
        ScanBarcode.identifymedID = helper.getMedicineID(c);
        medicineName.setVisibility(View.GONE);
        lblmedicineName.setVisibility(View.GONE);
        medicineTypes.setVisibility(View.INVISIBLE);
        medicineTypedisplay.setVisibility(View.VISIBLE);
        disableEditText(medicineDosage);
        disableEditText(medicinePurpose);
        disableEditText(medicineExpiry);
        disableEditText(medicineRemarks);
        buttonSave.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.VISIBLE);
        buttonIdentify.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                if (checkDateEntered()) {
                    String medID = medicineID.getText().toString();
                    String medName = medicineName.getText().toString();
                    String medDosage = medicineDosage.getText().toString();
                    String medType = String.valueOf(medicineTypes.getSelectedItem());
                    String medPurpose = medicinePurpose.getText().toString();
                    String medExpiry = medicineExpiry.getText().toString();
                    String medRemarks = medicineRemarks.getText().toString();
                    helper.insert(medID, medName, medDosage, medType, medPurpose, medExpiry, medRemarks);
                    showDialogueAnotherMed();
                }
                break;

            case R.id.button_delete:
                showDialogueDelete(medicineDBID);
                break;

            case R.id.button_identify:
                ScanBarcode.ischeckmedID = true;
                Intent intent = new Intent(MedicineForm.this, ScanBarcode.class);
                intent.putExtra("Dosage", medicineDosage.getText().toString());
                startActivity(intent);
                finish();
                break;

            case R.id.medicine_expire:
                new DatePickerDialog(MedicineForm.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.BLACK);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogueDelete(final String medDBID)
    {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MedicineForm.this);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want to delete this?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    helper.delete(medDBID);
                    Toast.makeText(getApplicationContext(), "Deleted successfully",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogueAnotherMed()
    {
        final AlertDialog.Builder dialogAdd = new AlertDialog.Builder(MedicineForm.this);
        dialogAdd.setTitle("Add Another Medicine?");
        dialogAdd.setMessage("Do you want to add another medicine?");
        dialogAdd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    finish();
                    Intent i = new Intent(MedicineForm.this, ScanBarcode.class);
                    startActivity(i);
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });
        dialogAdd.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });


        dialogAdd.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        medicineExpiry.setText(sdf.format(calendar.getTime()));
    }

    private boolean isEmpty (EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean checkDateEntered() {
        if (isEmpty(medicineName)){
            medicineName.setError("Medicine Name is required!");
            return false;
        }
        if (isEmpty(medicineDosage)){
            medicineDosage.setError("Medicine Dosage is required!");
            return false;
        }
        if (isEmpty(medicinePurpose)){
            medicinePurpose.setError("Medicine Purpose is required!");
            return false;
        }
        if (isEmpty(medicineExpiry)){
            medicineExpiry.setError("Please select expiry date");
            return false;
        }
        if (isEmpty(medicineRemarks)){
            medicineRemarks.setText("NIL");
        }
        return true;
    }
}
