package com.appspace.evytinkadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appspace.evytinkadmin.R;
import com.appspace.evytinkadmin.fragment.MainActivityFragment;
import com.appspace.evytinkadmin.util.DataStoreUtils;
import com.appspace.evytinkadmin.util.Helper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "MemberCardActivity";

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                gotoLoginActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    LoggerUtils.log2D(TAG, "Barcode read: " + barcode.displayValue);
//                    showBarcodeOnTextView(barcode.displayValue);
//                } else {
//                    LoggerUtils.log2D(TAG, "No barcode captured, intent data is null");
//                }
//            } else {
//                LoggerUtils.log2D(TAG, "barcode error");
//            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    showBarcodeOnTextView("");
                } else {
                    showBarcodeOnTextView(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                launchBarcodeActivityV2();
                break;
        }
    }

    private void launchBarcodeActivityV2() {
        new IntentIntegrator(this).initiateScan();
    }

    private void showBarcodeOnTextView(String barcode) {
        String text;
        if (barcode.equals("")) {
            text = "Cancelled";
        } else {
            text = "Barcode: " + barcode;
        }
        Snackbar.make(fab, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        if (barcode.equals("")) {
            return;
        }
        MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        String id = DataStoreUtils.getInstance().getAppUserId();
        String url = Helper.webUrl(id, barcode);
        fragment.loadUrl(url);
    }

    protected void gotoLoginActivity() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        // close this activity
        finish();
    }

}
