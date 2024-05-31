package com.sp.medify;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class HospitalDetail extends AppCompatActivity implements View.OnClickListener {

    Context context;
    private static final int REQUEST_PHONE_CALL = 1;
    private String gentelephone;
    private String emtelephone;
    private String website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hospital");

        String name = getIntent().getExtras().getString("hospital_name");
        String address = getIntent().getExtras().getString("hospital_address");
        gentelephone = getIntent().getExtras().getString("hospital_general_telephone");
        emtelephone = getIntent().getExtras().getString("hospital_emergency_telephone");
        String email = getIntent().getExtras().getString("hospital_email");
        website = getIntent().getExtras().getString("hospital_website");
        String vhours = getIntent().getExtras().getString("hospital_visiting_hours");
        String logo = getIntent().getExtras().getString("hospital_logo");

        TextView hos_name = findViewById(R.id.hos_detail_name);
        TextView hos_addr = findViewById(R.id.hos_detail_addr);
        TextView hos_gtel = findViewById(R.id.hos_detail_genphone);
        TextView hos_desc = findViewById(R.id.hos_detail_description);
        ImageView img = findViewById(R.id.hos_detail_thumbnail);
        Button call_gtel = findViewById(R.id.btn_call_gen);
        Button call_etel = findViewById(R.id.btn_call_em);
        call_gtel.setOnClickListener(this);
        call_etel.setOnClickListener(this);
        Button open_website = findViewById(R.id.btn_website);
        open_website.setOnClickListener(this);



        hos_name.setText(name);
        hos_addr.setText(address);
        hos_gtel.setText("General Hotline: " + gentelephone);
        hos_desc.setText("Emergency Hotline: " + emtelephone + "\n\nEmail: " + email + "\n\nWebsite: " + website +
                "\n\nVisiting Hours: \n" + vhours);

        Picasso.with(context).load(logo).into(img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_gen:
                Intent callIntent = new Intent(Intent.ACTION_CALL); //change to ACTION_CALL to call number straightaway
                String dial = "tel: " + gentelephone;
                callIntent.setData(Uri.parse(dial));

                if (ActivityCompat.checkSelfPermission(HospitalDetail.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(HospitalDetail.this, new
                            String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                }
                break;

            case R.id.btn_call_em:
                showDialogueCallEm();
                break;

            case R.id.btn_website:
                Intent intent = new Intent(HospitalDetail.this, WebViewActivity.class);
                intent.putExtra("website", website);
                startActivity(intent);
                break;
        }
    }

    private void showDialogueCallEm()
    {
        final AlertDialog.Builder dialogAdd = new AlertDialog.Builder(HospitalDetail.this);
        dialogAdd.setTitle("Calling Emergency Number");
        dialogAdd.setMessage("Are you sure there is an emergency?");

        dialogAdd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent callIntent2 = new Intent(Intent.ACTION_DIAL);
                    String dial2 = "tel: " + emtelephone;
                    callIntent2.setData(Uri.parse(dial2));

                    if (ActivityCompat.checkSelfPermission(HospitalDetail.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent2);
                    } else {
                        ActivityCompat.requestPermissions(HospitalDetail.this, new
                                String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    }
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });
        dialogAdd.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogAdd.show();
    }

}
