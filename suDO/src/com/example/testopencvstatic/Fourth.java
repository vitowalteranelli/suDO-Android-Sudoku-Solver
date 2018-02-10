package com.example.testopencvstatic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Fourth extends Activity {

	
	GridLayout gridLayout;
	TextView[][] text_vector;
	
	Button.OnClickListener onGoonListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {			
			goOn();
			}
	};
	
	public void goOn () {
		
		Intent intent = new Intent(this, Fifth.class);
		startActivity(intent);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fourth);
	    final Global global = (Global) getApplicationContext();

		gridLayout = (GridLayout) findViewById(R.id.gridLayout1);
		final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
	    int pixels = (int) (40 * scale + 0.5f);
		
		loadGrid();
		
		Button btn_to5 = (Button) findViewById(R.id.button_4);
		btn_to5.setOnClickListener(onGoonListener); 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fourth, menu);
		return true;
	}
	
	public void loadGrid(){
		
		text_vector = new TextView[9][9];
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				
				text_vector[i][j] = createTextView(i,j);
				GridLayout.Spec row = GridLayout.spec((i+1), 1); 
				GridLayout.Spec col = GridLayout.spec((j+1), 1);
				gridLayout.addView(text_vector[i][j], new GridLayout.LayoutParams(row , col));
			}
		}
		
	
		
	}
		
	public TextView createTextView(int row, int col){
	    final Global global = (Global) getApplicationContext();
		
	    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
	    int pixels = (int) (30 * scale + 0.5f);
	    
		TextView tview = new TextView(this);
		tview.setText(global.string_vector[row][col]);
		tview.setWidth(pixels);
		tview.setHeight(pixels);
		tview.setTextSize(25);
		tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		if ((row<3)&&(col<3)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row<3)&&(col>5)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row<6)&&(col<6)&&(row>2)&&(col>2)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row>5)&&(col<3)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row>5)&&(col>5)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		
		//codice critico
		int supertag = (row*1000)+col;
		tview.setTag(Integer.valueOf(supertag));

		OnClickListener onclicklistener = new OnClickListener() {
				
		    @Override
		    public void onClick(View v) {
		    	
		    	TextView textV = (TextView) v;
		    	int supertag = (Integer) v.getTag();
		    	int row;
		    	int col;
		    	row= Math.round(supertag/1000);
		    	col = supertag % 1000;
		    	String num = textV.getText().toString();
		    	
		    	
		    	// adesso devosostituire  il richiamo qui sotto e passare i due parametri a ConfirmRemoval
		    	//NumberChooser numberpicker = new NumberChooser();
		    	dialogNumber(row,col,num);

		    	
		        // TODO Auto-generated method stub
		        if(v == text_vector[0][0]){
		            //do whatever you want....
		        }
		    }
		    

		    
		};
		
		tview.setOnClickListener(onclicklistener);
		
		//!codice critico
		
		return tview;
	}
	
	
	// codice crtico
	private void dialogNumber(final int row, final int col, String number){
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setCancelable(false);
        bld.setTitle(R.string.dialog_fire_missiles);
        bld.setMessage(R.string.dialog_fire_missiles);
        //inserire numberpicker
        final NumberPicker np = (NumberPicker) new NumberPicker(this);
        np.setMaxValue(9);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        bld.setView(np);
        
        bld.setPositiveButton(R.string.fire, new AlertDialog.OnClickListener() {

        	@Override
            public void onClick(DialogInterface dialog, int which) {
         // funziona con le funzioni di classe -- loadGrid();
            		//TODO something
            	//aggiornare string_vector
            	//aggiornare text_vector
               //	int n = np.getValue();
        		String s = String.valueOf(np.getValue());
            	//text_vector[row][col].setText(s);
        		//string_vector[row][col].setText(s);
        		//potremmo fare una funzione di update
        		updateNumber(row,col,s);
	            dialog.dismiss();

            }


        });
        
        
        bld.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // o dialog.dismiss(); ???
            }
        });
        AlertDialog alert = bld.create();
        alert.show();
}
	
	
	
	public static class NumberChooser implements DialogInterface.OnClickListener {
	    int deptID;
	    public void processAlert(int _id){
	        deptID = _id;
	    }
	    public void onClick(DialogInterface dialog, int which) {
	   //      Dept.RemoveDept(deptID);
	            dialog.dismiss();
	    //        GetDepartments();   
	    }
	}
	
	//!codice critico
	public void updateNumber(int row,int col, String s){
	    final Global global = (Global) getApplicationContext();
	    if (s.equals("0")) {s=" ";}
		text_vector[row][col].setText(s);
		global.string_vector[row][col] = s;
				
	}

}


