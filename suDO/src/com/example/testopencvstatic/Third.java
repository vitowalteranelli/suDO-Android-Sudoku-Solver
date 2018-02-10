package com.example.testopencvstatic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.googlecode.tesseract.android.TessBaseAPI;

public class Third extends Activity {
	public static final String PACKAGE_NAME = "com.example.testopencvstatic";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/TestOpenCVStatic/";
	public static final String lang = "eng";
	private static final String TAG = "OCRsignals";
	protected Button _button;
	// protected ImageView _image;
	protected EditText _field;
	protected String _path;
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	public boolean gon = false;

	Button.OnClickListener onClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {			
			if(gon){goOn();}
			}
	};
	
	public void goOn () {
		
		Intent intent = new Intent(this, Fourth.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		

		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		
		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		
		
		// _image = (ImageView) findViewById(R.id.image);
		_field = (EditText) findViewById(R.id.field);
		_button = (Button) findViewById(R.id.button3);
		_button.setOnClickListener(new ButtonClickHandler());

		_path = DATA_PATH + "/ocr.jpg";
		
		Button btn_to4 = (Button) findViewById(R.id.button_3b);
		btn_to4.setOnClickListener(onClickListener); 
		
		
	}
	
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.v(TAG, "Starting Detection cycle");
			reco();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.third, menu);
		return true;
	}
	
	
	protected void reco() {
		_taken = true;

		/*BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}
*/
		// _image.setImageBitmap( bitmap );
		
		
		//accediamo alle variabili globali
	    final Global global = (Global) getApplicationContext();
		
		
		Log.v(TAG, "Before baseApi");

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		
		baseApi.init(DATA_PATH, lang);
		baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR);
		baseApi.setVariable("tessedit_char_whitelist", "123456789");
		
		
		
		
		/*
		baseApi.setImage(global.image_vector[4][4]);
		
		String recognizedText = baseApi.getUTF8Text();*/
		
		//qua dobbiamo caricare la matrice di testi
		global.string_vector = new String[9][9];
		String recognizedText = new String();
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				
				baseApi.setImage(global.image_vector[i][j]);
				global.string_vector[i][j] = baseApi.getUTF8Text();
				
				recognizedText += (!global.string_vector[i][j].equals("")) ? global.string_vector[i][j] : " ";
				
				
			}
		}
		
		gon = true;
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		Log.v(TAG, "OCRED TEXT: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();

		if ( recognizedText.length() != 0 ) {
			_field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
			_field.setSelection(_field.getText().toString().length());
		}
		
		// Cycle done.
	}
	

}
