package de.benibela.xkcd1363;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Random;

public class XKCD1363 extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        (new DownloadImageTask((TextView) findViewById(R.id.caption), (TextView) findViewById(R.id.text), (ImageView) findViewById(R.id.view))).execute();
    }


    @Override
    protected void onResume() {
        super.onResume();

        XKCDService.start(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basemenu, menu);
                                 return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about)
            showMessage(this, "This phone uses data from XKCD and sounds from freesound.org, by: dkmedic (104453), Corsica_S (109838), Banes (161266), kathid (167468), Syna-Max (64939), sironboy Madera del este Films (132106), queen_westeros (222545)");
        else if (item.getItemId() == R.id.options) {
            Intent intent = new Intent(this, Options.class);
            startActivity(intent);
        } else return false;
        return true;
    }
    interface MessageHandler{
        void onDialogEnd(DialogInterface dialogInterface, int i);
    }
    static public void showMessage(Context context, String message){showMessage(context, message, null, "OK", null, null);}
    static public void showMessage(Context context, String message, final MessageHandler handler){showMessage(context, message, null, "OK", null, handler);}
    //static public void showMessageYesNo(String message, MessageHandler handler){ Util.showMessage(message, tr(R.string.no), null, tr(R.string.yes), handler); }
    static public void showMessage(Context context, String message, String negative, String neutral, String positive, final MessageHandler handler){
        if (context == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("XKCD 1363 Phone");
        if (negative != null)
            builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (handler != null) handler.onDialogEnd(dialogInterface, i);
                }
            });
        if (neutral != null)
            builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (handler != null) handler.onDialogEnd(dialogInterface, i);
                }
            });
        if (positive != null)
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (handler != null) handler.onDialogEnd(dialogInterface, i);
                }
            });
        if (handler != null) {
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    handler.onDialogEnd(dialogInterface, -123);
                }
            });
        }
        builder.show();
    }


    //from http://stackoverflow.com/questions/5776851/load-image-from-url
    class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        TextView erica, text;
        ImageView view;

        public DownloadImageTask(TextView avenger,  TextView text, ImageView view) {
            this.view = view;
            this.text = text;
            this.erica = avenger;
        }

        String cap, alt;

        protected Bitmap doInBackground(Void... x) {

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL("http://xkcd.com/info.0.json").openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) sb.append(line + "\n");

                JSONObject obj = (new JSONObject(sb.toString()));

                String urldisplay = obj.getString("img");
                cap = obj.getString("title");
                alt = obj.getString("alt");

                in = new java.net.URL(urldisplay).openStream();

                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null || alt == null || cap == null) return;
            view.setImageBitmap(result);
            text.setText(alt);
            erica.setText(cap);
        }
    }





}
