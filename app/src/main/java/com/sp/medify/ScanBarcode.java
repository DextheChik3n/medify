package com.sp.medify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanBarcode extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    Button buttonStopScan;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    static boolean ischeckmedID = false;
    static String identifymedID = "";
    static String medicineDBid = "";
    private String medicineDosage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        txtBarcodeValue = (TextView) findViewById(R.id.txtBarcodeValue);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        buttonStopScan = (Button) findViewById(R.id.buttonStop);
        buttonStopScan.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (ischeckmedID) {
            buttonStopScan.setVisibility(View.VISIBLE);
            medicineDosage = getIntent().getExtras().getString("Dosage");
            buttonStopScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent;
                    intent = new Intent(ScanBarcode.this, MedicineForm.class);
                    intent.putExtra("ID", medicineDBid);
                    startActivity(intent);
                }
            });
        }
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarcode.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanBarcode.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                /*Toast.makeText(getApplicationContext(),
                        "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!ischeckmedID) { //registering medID
                                if (barcodes.valueAt(0).rawValue != null) { //QRcode detected
                                    txtBarcodeValue.removeCallbacks(null);
                                    intentData = barcodes.valueAt(0).rawValue;
                                    txtBarcodeValue.setText(intentData);
                                    startActivity(new Intent(ScanBarcode.this, MedicineForm.class)
                                            .putExtra("med_id", intentData));
                                    finish();
                                } else { //QR code not detected
                                    txtBarcodeValue.setText("No Barcode Detected");
                                }
                            } else { //Identifying medID
                                if (barcodes.valueAt(0).rawValue != null) { //QRcode detected
                                    txtBarcodeValue.removeCallbacks(null);
                                    intentData = barcodes.valueAt(0).rawValue;
                                    if (intentData.equals(identifymedID)) {
                                        txtBarcodeValue.setText(medicineDosage);
                                        txtBarcodeValue.setTextColor(Color.rgb(29 ,185,84));
                                    } else {
                                        txtBarcodeValue.setText("Wrong Medicine, Scan another one");
                                        txtBarcodeValue.setTextColor(Color.RED);
                                    }
                                } else { //QR code not detected
                                    txtBarcodeValue.setText("No Barcode Detected");
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
