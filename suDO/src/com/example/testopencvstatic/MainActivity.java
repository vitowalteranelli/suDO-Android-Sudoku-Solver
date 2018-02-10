package com.example.testopencvstatic;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


	public class MainActivity extends Activity {
		
		private static final int ACTION_TAKE_PHOTO_B = 1;
		private static final int ACTION_TAKE_PHOTO_S = 2;
		private static final int ACTION_TAKE_VIDEO = 3;

		private static final String BITMAP_STORAGE_KEY = "viewbitmap";
		private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
		private ImageView mImageView;
		private Bitmap mImageBitmap;
		public boolean gon = false;

	
		//private String mCurrentPhotoPath;
		public String mCurrentPhotoPath;

		private static final String JPEG_FILE_PREFIX = "IMG_";
		private static final String JPEG_FILE_SUFFIX = ".jpg";
		public final static String EXTRA_MESSAGE = "com.example.testopencvstatic.MESSAGE";


		private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

		
		/* Photo album for this application */
		private String getAlbumName() {
			return getString(R.string.album_name);
		}

		
		private File getAlbumDir() {
			File storageDir = null;

			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				
				storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

				if (storageDir != null) {
					if (! storageDir.mkdirs()) {
						if (! storageDir.exists()){
							Log.d("CameraSample", "failed to create directory");
							return null;
						}
					}
				}
				
			} else {
				Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
			}
			
			return storageDir;
		}

		private File createImageFile() throws IOException {
			// Create an image file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
			File albumF = getAlbumDir();
			File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
			return imageF;
		}

		private File setUpPhotoFile() throws IOException {
			
			File f = createImageFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			
			return f;
		}

		private void setPic() {

			/* There isn't enough memory to open up more than a couple camera photos */
			/* So pre-scale the target bitmap into which the file is decoded */

			/* Get the size of the ImageView */
			int targetW = mImageView.getWidth();
			int targetH = mImageView.getHeight();

			/* Get the size of the image */
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;
			
			/* Figure out which way needs to be reduced less */
			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0)) {
				scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
			}

			/* Set bitmap options to scale the image decode target */
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			/* Decode the JPEG file into a Bitmap */
			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			
			/* Associate the Bitmap to the ImageView */
			mImageView.setImageBitmap(bitmap);
			mImageView.setVisibility(View.VISIBLE);
		}

		private void galleryAddPic() {
			    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
				File f = new File(mCurrentPhotoPath);
			    Uri contentUri = Uri.fromFile(f);
			    mediaScanIntent.setData(contentUri);
			    this.sendBroadcast(mediaScanIntent);
		}

		private void dispatchTakePictureIntent(int actionCode) {

			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			switch(actionCode) {
			case ACTION_TAKE_PHOTO_B:
				File f = null;
				
				try {
					f = setUpPhotoFile();
					mCurrentPhotoPath = f.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				} catch (IOException e) {
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				break;

			default:
				break;			
			} // switch

			startActivityForResult(takePictureIntent, actionCode);
		}
		
	   

		private void handleBigCameraPhoto() {

			if (mCurrentPhotoPath != null) {
				setPic();
				galleryAddPic();
				//mCurrentPhotoPath = null;
			}

		}


		Button.OnClickListener mTakePicOnClickListener = 
			new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
			}
		};


		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			mImageView = (ImageView) findViewById(R.id.imageView1);
			mImageBitmap = null;

			Button picBtn = (Button) findViewById(R.id.btnIntend);
			setBtnListenerOrDisable( 
					picBtn, 
					mTakePicOnClickListener,
					MediaStore.ACTION_IMAGE_CAPTURE
			);
			
			Button btn = (Button) findViewById(R.id.button1);

			btn.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			        sendPath(v);
			    }
			});

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
			} else {
				mAlbumStorageDirFactory = new BaseAlbumDirFactory();
			}
			
	
			
			
		}
		
		/*
		public void test(View v){
			final Global global = (Global) getApplicationContext();
			global.string_vector = new String[9][9];
			global.string_vector[0][0]= " ";
			global.string_vector[0][1]= " ";
			global.string_vector[0][2]= "1";
			global.string_vector[0][3]= " ";
			global.string_vector[0][4]= "8";
			global.string_vector[0][5]= " ";
			global.string_vector[0][6]= " ";
			global.string_vector[0][7]= " ";
			global.string_vector[0][8]= " ";
			global.string_vector[1][0]= " ";
			global.string_vector[1][1]= " ";
			global.string_vector[1][2]= " ";
			global.string_vector[1][3]= " ";
			global.string_vector[1][4]= " ";
			global.string_vector[1][5]= "5";
			global.string_vector[1][6]= " ";
			global.string_vector[1][7]= "3";
			global.string_vector[1][8]= " ";
			global.string_vector[2][0]= " ";
			global.string_vector[2][1]= "7";
			global.string_vector[2][2]= " ";
			global.string_vector[2][3]= " ";
			global.string_vector[2][4]= " ";
			global.string_vector[2][5]= "4";
			global.string_vector[2][6]= " ";
			global.string_vector[2][7]= " ";
			global.string_vector[2][8]= "9";
			global.string_vector[3][0]= " ";
			global.string_vector[3][1]= " ";
			global.string_vector[3][2]= "2";
			global.string_vector[3][3]= "9";
			global.string_vector[3][4]= " ";
			global.string_vector[3][5]= " ";
			global.string_vector[3][6]= " ";
			global.string_vector[3][7]= " ";
			global.string_vector[3][8]= " ";
			global.string_vector[4][0]= "6";
			global.string_vector[4][1]= " ";
			global.string_vector[4][2]= " ";
			global.string_vector[4][3]= " ";
			global.string_vector[4][4]= " ";
			global.string_vector[4][5]= " ";
			global.string_vector[4][6]= " ";
			global.string_vector[4][7]= " ";
			global.string_vector[4][8]= "8";
			global.string_vector[5][0]= " ";
			global.string_vector[5][1]= " ";
			global.string_vector[5][2]= " ";
			global.string_vector[5][3]= " ";
			global.string_vector[5][4]= " ";
			global.string_vector[5][5]= "3";
			global.string_vector[5][6]= "7";
			global.string_vector[5][7]= " ";
			global.string_vector[5][8]= " ";
			global.string_vector[6][0]= "9";
			global.string_vector[6][1]= " ";
			global.string_vector[6][2]= " ";
			global.string_vector[6][3]= "6";
			global.string_vector[6][4]= " ";
			global.string_vector[6][5]= " ";
			global.string_vector[6][6]= " ";
			global.string_vector[6][7]= "2";
			global.string_vector[6][8]= " ";
			global.string_vector[7][0]= " ";
			global.string_vector[7][1]= "3";
			global.string_vector[7][2]= " ";
			global.string_vector[7][3]= "7";
			global.string_vector[7][4]= " ";
			global.string_vector[7][5]= " ";
			global.string_vector[7][6]= " ";
			global.string_vector[7][7]= " ";
			global.string_vector[7][8]= " ";
			global.string_vector[8][0]= " ";
			global.string_vector[8][1]= " ";
			global.string_vector[8][2]= " ";
			global.string_vector[8][3]= " ";
			global.string_vector[8][4]= "1";
			global.string_vector[8][5]= " ";
			global.string_vector[8][6]= "4";
			global.string_vector[8][7]= " ";
			global.string_vector[8][8]= " ";
			
			
		    Intent intent = new Intent(this, Fourth.class);
		    startActivity(intent);
			
			
			
			
			
		}
		
		
		nell'xml
		
		    <Button
        android:id="@+id/button99"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignLeft="@+id/imageView1"
        android:layout_below="@+id/imageView1"
        android:layout_marginTop="26dp"
        android:onClick="test"
        android:text="test" />
		
		
		
		
		*/

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			/*switch (requestCode) {
			case ACTION_TAKE_PHOTO_B: {
				if (resultCode == Activity.RESULT_OK) {
					handleBigCameraPhoto();
					gon = true;
				}
				break;
			} // ACTION_TAKE_PHOTO_B

			} // switch*/
			//messo in comment prova 3 gen 2014
			handleBigCameraPhoto();
			gon = true;
		}

		// Some lifecycle callbacks so that the image can survive orientation change
		@Override
		protected void onSaveInstanceState(Bundle outState) {
			outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
			outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
			super.onSaveInstanceState(outState);
		}

		@Override
		protected void onRestoreInstanceState(Bundle savedInstanceState) {
			super.onRestoreInstanceState(savedInstanceState);
			mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
			mImageView.setImageBitmap(mImageBitmap);
			mImageView.setVisibility(
					savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
							ImageView.VISIBLE : ImageView.INVISIBLE
			);
		}

		/**
		 * Indicates whether the specified action can be used as an intent. This
		 * method queries the package manager for installed packages that can
		 * respond to an intent with the specified action. If no suitable package is
		 * found, this method returns false.
		 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
		 *
		 * @param context The application's environment.
		 * @param action The Intent action to check for availability.
		 *
		 * @return True if an Intent with the specified action can be sent and
		 *         responded to, false otherwise.
		 */
		public static boolean isIntentAvailable(Context context, String action) {
			final PackageManager packageManager = context.getPackageManager();
			final Intent intent = new Intent(action);
			List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		}

		private void setBtnListenerOrDisable( 
				Button btn, 
				Button.OnClickListener onClickListener,
				String intentName
		) {
			if (isIntentAvailable(this, intentName)) {
				btn.setOnClickListener(onClickListener);        	
			} else {
				btn.setText( 
					getText(R.string.cannot).toString() + " " + btn.getText());
				btn.setClickable(false);
			}
		}
		
		// send path to the recognizer activity
		public void sendPath(View view) {
			
			if (gon){String message = (mCurrentPhotoPath != null) ? mCurrentPhotoPath : "ciai" ;//mCurrentPhotoPath.toString();
/*
		    // Create the text view
		    TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText(message);*/
			
		    Intent intent = new Intent(this, Second.class);
		   // String message = mCurrentPhotoPath;
		    intent.putExtra(EXTRA_MESSAGE, message);
		    startActivity(intent);
			}
		}
		// ! recognizer launch

}
