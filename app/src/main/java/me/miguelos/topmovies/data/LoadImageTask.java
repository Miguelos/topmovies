package me.miguelos.topmovies.data;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * AsyncTask to load the bitmaps from an URL to an Image View.
 * 
 * @author Miguel González Pérez
 * 
 * @since 14 Aug 2013 12:11:23
 * 
 */
public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

	/**
	 * ImageView were the bitmap is going to be load. Use of WeakReference to
	 * avoid memory leak.
	 */
	private final WeakReference<ImageView> viewReference;

	public LoadImageTask(ImageView view) {
		viewReference = new WeakReference<ImageView>(view);
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		// Get the image url
		String strUrl = urls[0];
		Bitmap bitmap = null;
		// Try to opem the conection and get the image
		try {
			URLConnection urlCon = new URL(strUrl).openConnection();
			urlCon.setUseCaches(true);
			urlCon.connect();
			bitmap = BitmapFactory.decodeStream(urlCon.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

		// Get the imageView and update the bitmap to the result
		ImageView imageView = viewReference.get();
		if (imageView != null && result != null) {
			imageView.setImageBitmap(result);
		}
	}
}
