package com.github.jotask.gametracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * LoadImage
 *
 * @author Jose Vives Iznardo
 * @since 08/10/2017
 */
class LoadImage extends AsyncTask<Object, Void, Bitmap> {

    private ImageView imv;
    private String path;

    public LoadImage(ImageView imv) {
        this.imv = imv;
        this.path = imv.getTag().toString();
    }

    @Override
    protected Bitmap doInBackground(Object... params) {

        Bitmap bitmap = null;
        try {
            URL aURL = new URL(this.path);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
        }

        return bitmap;

    }
    @Override
    protected void onPostExecute(Bitmap result) {
        if (!imv.getTag().toString().equals(path)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
            return;
        }

        if(result != null && imv != null){
            imv.setVisibility(View.VISIBLE);
            imv.setImageBitmap(result);
        }else{
            imv.setVisibility(View.GONE);
        }
    }

}