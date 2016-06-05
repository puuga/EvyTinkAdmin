package com.appspace.evytinkadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evytinkadmin.R;
import com.appspace.evytinkadmin.activity.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "MemberCardActivity";

    private TextView tvBarcode;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
    }

    private void initInstances() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(this);
        tvBarcode = (TextView) findViewById(R.id.tvBarcode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    LoggerUtils.log2D(TAG, "Barcode read: " + barcode.displayValue);
                    showBarcodeOnTextView(barcode.displayValue);
                } else {
                    LoggerUtils.log2D(TAG, "No barcode captured, intent data is null");
                }
            } else {
                LoggerUtils.log2D(TAG, "barcode error");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                launchBarcodeActivity();
                break;
        }
    }

    private void launchBarcodeActivity() {
        LoggerUtils.log2D(TAG, "Scan Barcode");
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    private void showBarcodeOnTextView(String text) {
        tvBarcode.setText(text);
        Snackbar.make(fab, "Barcode OK", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
