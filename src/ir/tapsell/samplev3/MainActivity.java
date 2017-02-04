package ir.tapsell.samplev3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellConfiguration;
import ir.tapsell.sdk.TapsellRewardListener;
import ir.tapsell.sdk.TapsellShowOptions;

public class MainActivity extends Activity {

    private static final String appKey = "kilkhmaqckffopkpfnacjkobgrgnidkphkcbtmbcdhiokqetigljpnnrbfbnpnhmeikjbq";

    private static final String myAppMainZoneId = "586f52d9bc5c284db9445beb";

    Button requestAdBtn, showAddBtn;
    TapsellAd ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TapsellConfiguration config = new TapsellConfiguration();
        config.setDebugMode(true);

        Tapsell.initialize(this, config, appKey);

        Tapsell.setRewardListener(new TapsellRewardListener() {
            @Override
            public void onAdShowFinished(TapsellAd ad, boolean completed) {
                Log.e("MainActivity","isCompleted? "+completed);
                // store user
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Info")
                        .setMessage("Video view finished")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        

        requestAdBtn = (Button) findViewById(R.id.btnRequestAd);

        requestAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(myAppMainZoneId);
            }
        });

        showAddBtn = (Button) findViewById(R.id.btnShowAd);

        showAddBtn.setEnabled(false);

        showAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ad!=null && ad.isValid())
                {
                    showAddBtn.setEnabled(false);
                    TapsellShowOptions showOptions = new TapsellShowOptions();
                    showOptions.setBackDisabled(false);
                    showOptions.setImmersiveMode(true);
                    showOptions.setRotationMode(TapsellShowOptions.ROTATION_UNLOCKED);
                    ad.show(MainActivity.this, showOptions);
                }
            }
        });
    }

    private void loadAd(String zoneId) {

        TapsellAdRequestOptions options = new TapsellAdRequestOptions(TapsellAdRequestOptions.CACHE_TYPE_STREAMED);

        Tapsell.requestAd(MainActivity.this, zoneId, options, new TapsellAdRequestListener() {
            @Override
            public void onError(String error) {
                Log.d("Tapsell Sample","Error: "+error);
            }

            @Override
            public void onAdAvailable(TapsellAd ad) {

                MainActivity.this.ad = ad;
                showAddBtn.setEnabled(true);
                Log.d("Tapsell Sample","Ad is available");
            }

            @Override
            public void onNoAdAvailable() {
                Log.d("Tapsell Sample","No ad available");
            }

            @Override
            public void onNoNetwork() {
                Log.d("Tapsell Sample","No network");
            }

            @Override
            public void onExpiring(TapsellAd ad) {
                showAddBtn.setEnabled(false);
                loadAd(myAppMainZoneId);
            }
        });

    }

}
