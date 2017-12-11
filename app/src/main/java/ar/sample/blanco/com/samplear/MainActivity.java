package ar.sample.blanco.com.samplear;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.craftar.CraftARError;
import com.craftar.CraftAROnDeviceCollection;
import com.craftar.CraftAROnDeviceCollectionManager;
import com.craftar.CraftARTracking;
import com.craftar.SetOnDeviceCollectionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

    }

    private void initUI() {
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndAskIfNotGranted(new Runnable() {
                    @Override
                    public void run() {
                        loadOnDeviceARItemsAndLaunchActivity();
                    }
                });
            }
        });
    }


    private void loadOnDeviceARItemsAndLaunchActivity() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading AR collection");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();

        CraftAROnDeviceCollection collection =  CraftAROnDeviceCollectionManager.Instance().get(Config.MY_COLLECTION_TOKEN);

        CraftARTracking.Instance().setCollection(collection, new SetOnDeviceCollectionListener() {
            @Override
            public void setCollectionProgress(double v) {
                progressDialog.setProgress((int)v);
            }

            @Override
            public void collectionReady(List<CraftARError> list) {
                if (list != null) {
                    for (CraftARError error : list) {
                        Log.d("LaunchersActivity", "Error setting collection: " + error.getErrorMessage());
                    }
                }
                progressDialog.dismiss();
                Intent playExampleIntent = new Intent(MainActivity.this, OnDeviceARActivity.class);
                startActivity(playExampleIntent);
            }

            @Override
            public void setCollectionFailed(CraftARError error) {
                Toast.makeText(getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final int CAMERA_PERMISSION = 0;

    private Runnable doWhenGranted;

    public void checkPermissionAndAskIfNotGranted(Runnable doWhenGranted) {
        this.doWhenGranted = doWhenGranted;
        if (PermissionChecker.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                showExplanation("Camera access", "This app needs to use the camera to demostrate the SDK's capabilities", android.Manifest.permission.CAMERA, CAMERA_PERMISSION);
            } else {
                requestPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION);
            }
        } else {
            doWhenGranted.run();
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
}
