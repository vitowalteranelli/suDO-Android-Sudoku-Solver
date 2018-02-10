package com.example.testopencvstatic;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Second extends Activity {

	//private static final String CAMERA_DIR = "/dcim/Esempio/";
	//private String basepercorso = Environment.getExternalStorageDirectory().toString();
	//private static final String photo = "IMG_20131223_190909_-1739645051.jpg";
	public Bitmap prova;
	public Bitmap prova2;
	public Bitmap img;
	public Mat m;
	public Mat m2;
	public Mat kernel;
	public Mat prima_cifra;
	public Mat ocr;
	public Mat little_mat;
	//public Bitmap[][] image_vector;
	
	Button.OnClickListener onClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {			
			goOn();
			}
	};
	
	public void goOn () {
		
		Intent intent = new Intent(this, Third.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		OpenCVLoader.initDebug();
		
		
		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		preprocess(message);		
		
		ImageView imagestamp = (ImageView) findViewById(R.id.imageView2);
		 imagestamp.setImageBitmap(img);
		 
			Button btn2 = (Button) findViewById(R.id.button2);
			btn2.setOnClickListener(onClickListener); 
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}
	
public void preprocess(String mensaje) {
		
		m = new Mat();
		m2 = new Mat();
		ocr = new Mat();
		kernel = new Mat();
		prima_cifra = new Mat();
		little_mat = new Mat();
		
		/*prova a scalare l'immagine 3gen2014
		//prova = BitmapFactory.decodeFile("/mnt/sdcard/Pictures/Esempio/IMG_20131223_190909_-1739645051.jpg");
		prova = BitmapFactory.decodeFile(mensaje);
		prova2 = prova.copy(Bitmap.Config.ARGB_8888, true);
		
		*/
		//scalare l'immagine ad 800x600 sennò esplode la memoria
		
		// Get the dimensions of the View
	    int targetW = 640;
	    int targetH = 480;

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mensaje, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap prova = BitmapFactory.decodeFile(mensaje, bmOptions);
	    prova2 = prova.copy(Bitmap.Config.ARGB_8888, true);
		
		
		
	    Utils.bitmapToMat(prova2,m);
		
		m.copyTo(m2);   
		
		//Imgproc.cvtColor(imgToProcess, imgToProcess, Imgproc.COLOR_GRAY2RGBA, 4);
		Imgproc.cvtColor(m, m2, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(m2, m2, new Size(11,11), 0);

		
		//passo da 4 a 2 3gen14
		Imgproc.adaptiveThreshold(m2, m2, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);
		
		
		// 3) Invert the image -> so most of the image is black
		Core.bitwise_not(m2, m2);
		// 4) Dilate -> fill the image using the MORPH_DILATE

		m2.copyTo(ocr);
		
		kernel = Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(3,3), new Point(1,1));
		Imgproc.dilate(m2, m2, kernel);
		 
		
		
		//List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat imgSource = new Mat();
		//Imgproc.Canny(m2, imgSource, 50, 100); proviamo senza
		imgSource = m2.clone();
		//Imgproc.findContours(mIntermediateMat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
	    //find the contours
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	    Imgproc.findContours(imgSource, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

	    double maxArea = -1;
	    int maxAreaIdx = -1;
	    MatOfPoint temp_contour = contours.get(0); //the largest is at the index 0 for starting point
	    MatOfPoint2f approxCurve = new MatOfPoint2f();
	    Mat largest_contour = contours.get(0);
	    List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>();
	    for (int idx = 0; idx < contours.size(); idx++) {
	        temp_contour = contours.get(idx);
	        double contourarea = Imgproc.contourArea(temp_contour);
	        //compare this contour to the previous largest contour found
	        if (contourarea > maxArea) {
	            //check if this contour is a square
	            MatOfPoint2f new_mat = new MatOfPoint2f( temp_contour.toArray() );
	            int contourSize = (int)temp_contour.total();
	            Imgproc.approxPolyDP(new_mat, approxCurve, contourSize*0.05, true);
	            if (approxCurve.total() == 4) {
	                maxArea = contourarea;
	                maxAreaIdx = idx;
	                largest_contours.add(temp_contour);
	                largest_contour = temp_contour;
	            }
	        }
	    }
	    MatOfPoint temp_largest = largest_contours.get(largest_contours.size()-1);
	    largest_contours = new ArrayList<MatOfPoint>();
	    largest_contours.add(temp_largest);

	    //Imgproc.cvtColor(imgSource, imgSource, Imgproc.COLOR_BayerBG2RGB);
	    //Imgproc.drawContours(imgSource, largest_contours, -1, new Scalar(0, 255, 0), 1);
	    
	    
	    
	    //adesso proviamo a scrivere i contorni su una immagine nera quindi commento le righe dell'immagine
	   // Imgproc.cvtColor(imgSource, imgSource, Imgproc.COLOR_BayerBG2RGB);
	   // Imgproc.drawContours(imgSource, contours, maxAreaIdx, new Scalar(0, 255, 0), 1);
	    
	    //creo l'immagine nera
	    Mat m3 = new Mat();
		m3 = imgSource.clone();
		m3.setTo(new Scalar(0, 0, 0, 0));
		
		//cambio il numero di canali e scrivo il contorno
		Imgproc.cvtColor(m3, m3, Imgproc.COLOR_BayerBG2RGB);
		Imgproc.drawContours(m3, contours, maxAreaIdx, new Scalar(0, 255, 0), 1);
		Mat m4 = new Mat();
		Imgproc.cvtColor(m3, m4, Imgproc.COLOR_BGR2GRAY);
	    
	    //create the new image here using the largest detected square
	    //Imgproc.cornerHarris(imgSource, imgSource, 2, 3, 0.04); dà errore
	    //oppure posso usare goodfeaturestotrack su una immagine nera
		
		
		//usiamo goodFeaturesToTrack
		
		MatOfPoint initial = new MatOfPoint();
		Imgproc.goodFeaturesToTrack(m4, initial, 4, 0.01, 0.01);
		//da non usare MatOfPoint2f corners = new MatOfPoint2f( initial.toArray() );
		
		
		//ora dovrò usare getPerspectiveTransform e poi warpPerspective, devo controllare i parametri di goodfeatures..

		
		 Point[] angolo = initial.toArray();
		
		int minimalSum = 0;
		int maximalSum = 0;
		double[] sum = new double[4]; 
		
		for (int i=0;i<4;i++){
			sum[i]=angolo[i].x + angolo[i].y;
			if (sum[i]<sum[minimalSum]) {minimalSum = i;}
			if (sum[i]>sum[maximalSum]) {maximalSum = i;}
			
		}
		
		int topright = -1;
		int bottomleft = -1;
		for (int i=0;i<4;i++){
		if ((i!=minimalSum)&&(i!=maximalSum)){
			if (topright==-1){topright=i;}
			if (angolo[i].y<angolo[topright].y){topright=i;}else{bottomleft=i;}
			
		}
		}
		
		
		
		
		Mat src = new Mat(4,1,CvType.CV_32FC2);
		
		src.put(0, 0, angolo[minimalSum].x , angolo[minimalSum].y);
		src.put(1, 0, angolo[topright].x , angolo[topright].y);
		src.put(2, 0, angolo[maximalSum].x , angolo[maximalSum].y);
		src.put(3, 0, angolo[bottomleft].x , angolo[bottomleft].y);
		
	    Mat dst = new Mat(4,1,CvType.CV_32FC2);
	    dst.put(0,0, 0,0);
	    dst.put(1,0, 899,0);
	    dst.put(2,0, 899,899);
	    dst.put(3,0, 0,899);

	    
	    
	    Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
	    Mat cropped_image = new Mat();
	    
	    
	    
	    //Imgproc.cvtColor(m, ocr, Imgproc.COLOR_BGR2GRAY);
	    //Imgproc.adaptiveThreshold(ocr, ocr, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);

	    
	    
	    //passo da m2 ad ocr
	    Imgproc.warpPerspective(ocr, cropped_image, perspectiveTransform, new Size(900,900));
	    
	    
	    //ruotiamola di 90° in senso orario
	    Core.flip(cropped_image.t(), cropped_image, 1);
		
	    
	    
	    //prendiamo la cifra, se c'è,  nella prima posizione, i quadrati sono 100x100 prenderemo il riquadro centrale 90x90
	    //da ricordare che gli shift nel ciclo dovranno essere comunque di 100 a partire da 0
	    prima_cifra = cropped_image.submat(5, 94, 5, 94);
	    
	    
	    // variabili globali con Android.. e vai col limbo
	    final Global global = (Global) getApplicationContext();
	    global.image_vector = new Bitmap[9][9];
	    
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				
				
				//proviamo con little mat più stringente
				/*
				 * little_mat = cropped_image.submat(((i*100)+5), ((i*100)+94), ((j*100)+5), ((j*100)+94));
				 */
				little_mat = cropped_image.submat(((i*100)+9), ((i*100)+89), ((j*100)+9), ((j*100)+89));
				
				global.image_vector[i][j] = Bitmap.createBitmap(little_mat.cols(), little_mat.rows(),Bitmap.Config.ARGB_8888);
				Utils.matToBitmap(little_mat, global.image_vector[i][j]);
			}
		}

	    //Toast.makeText(getApplicationContext(), "Largest Contour: ", Toast.LENGTH_LONG).show();

	    //return imgSource;
		
		 img = Bitmap.createBitmap(cropped_image.cols(), cropped_image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(cropped_image, img);

       /*
        * 
        *  prova 3gen14
          img = Bitmap.createBitmap(m2.cols(), m2.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m2, img);
        *  
        */
	    
	    
	    /*ora commentiamo un attimo questo e tiriamo fuori l'elemento dall'array
        img = Bitmap.createBitmap(prima_cifra.cols(), prima_cifra.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(prima_cifra, img);*/
	    
	    /*bello ma ha rotto il cazzo
	     * img = global.image_vector[4][4];
	     */
		
		
	}
	

}