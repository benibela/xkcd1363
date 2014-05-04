package de.benibela.xkcd1363;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Options  extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        XKCDService.readConfig(this);
        ((EditText) findViewById(R.id.lightThreshold)).setText(XKCDService.THRESHOLD_BRIGHT+"");
        ((EditText) findViewById(R.id.lightIncFactor)).setText(XKCDService.THRESHOLD_BRIGHT_INC_FACTOR+"");
        ((EditText) findViewById(R.id.accelerationThreshold)).setText(XKCDService.THRESHOLD_FALL+"");

        ((Button) findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Options.this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat("light", Float.parseFloat( ((EditText) findViewById(R.id.lightThreshold)).getText().toString()));
                editor.putFloat("lightIncFactor", Float.parseFloat( ((EditText) findViewById(R.id.lightIncFactor)).getText().toString()));
                editor.putFloat("fall", Float.parseFloat( ((EditText) findViewById(R.id.accelerationThreshold)).getText().toString()));
                editor.commit();
                finish();
            }
        });
        ((Button) findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
