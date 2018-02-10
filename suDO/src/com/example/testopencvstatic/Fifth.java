package com.example.testopencvstatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fifth extends Activity {

	Slot[][] slot_vector;
	Slot[][][] slot_vector_backup;
	GridLayout gridLayout;
	TextView[][] text_vector;
	public List<Arrow> theArc;
	public List<Arrow> theDarkArc;
	int counter;
	Scroll[] leveldomain= new Scroll[81];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fifth);
	    final Global global = (Global) getApplicationContext();
	    
	    slot_vector = new Slot[9][9];
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				String s;
						if(global.string_vector[i][j].equals(" ")) {s="0";}
						else if (global.string_vector[i][j].equals("")){s="0";}
						else {s=global.string_vector[i][j];}
						
				slot_vector[i][j]= new Slot(i,j,Integer.parseInt(s));

			}
		}
		
		theArc = new ArrayList<Arrow>();
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if (slot_vector[i][j].value!=0){
					//updateDirectConstraints(i,j,slot_vector[i][j].value); 
					//int val = slot_vector[i][j].value;
					//slot_vector[i][j].setValue(val);
					disseminate(i,j,slot_vector[i][j].value);
					//if(markedUpdateDirectConstraints(i,j,slot_vector[i][j].value)){} funziona anche con marked
				}
			}
		}
		
		counter = 0;
		slot_vector_backup = new Slot[81][9][9];
		
		
		if (verified()){
			
			
			

			if (arcConsistency()){
				
				if (excavate()){
					
					gridLayout = (GridLayout) findViewById(R.id.gridLayout2);
					
					loadGrid();
					
					
				}else{
					
					
					TextView textView = new TextView(this);
				    String message = "Bad boy, girl or whatever you are. You tried solving an inconsistent Sudoku. \n What does it means inconsistent? \n Good point.. \n It means that not exists a set of valid values to be assigned. But probably.. you were wrong copying digits. So go back and correct them. \n However... nice try.. ";
				    textView.setText(message);

				    // Set the text view as the activity layout
				    setContentView(textView);
					
					
					
					
				}
				
			}
			else {
				
				 TextView textView = new TextView(this);
				    String message = "Bad boy, girl or whatever you are. You tried solving an inconsistent Sudoku. \n What does it means inconsistent? \n Good point.. \n It means that not exists a set of valid values to be assigned. But probably.. you were wrong copying digits. So go back and correct them. \n However... nice try.. ";
				    textView.setText(message);

				    // Set the text view as the activity layout
				    setContentView(textView);
				
			}
			
			
			
			
			
			
			
		}else{
			
			
			 TextView textView = new TextView(this);
			    String message = "Bad boy, girl or whatever you are. You tried solving an inconsistent Sudoku. \n What does it means inconsistent? \n Good point.. \n It means that not exists a set of valid values to be assigned. But probably.. you were wrong copying digits. So go back and correct them. \n However... nice try.. ";
			    textView.setText(message);

			    // Set the text view as the activity layout
			    setContentView(textView);
			
		}
		
		
		
		
		//blindSearch();
		
		
		
		
		
		
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
		

	    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
	    int pixels = (int) (30 * scale + 0.5f);
		
		
		TextView tview = new TextView(this);
		//tview.setText(String.valueOf(slot_vector[row][col].value));
		tview.setText(String.valueOf(slot_vector[row][col].value));
		tview.setWidth(pixels);
		tview.setHeight(pixels);
		tview.setTextSize(25);
		tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		if ((row<3)&&(col<3)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row<3)&&(col>5)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row<6)&&(col<6)&&(row>2)&&(col>2)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row>5)&&(col<3)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		if ((row>5)&&(col>5)){tview.setBackgroundColor(Color.parseColor("#e0e0e0"));}
		
		
		return tview;
	}
	
	
	
	public boolean verified(){
		
		
		
		
		for(int a=0;a<9;a++){
			for(int b=0;b<9;b++){
				
				if (slot_vector[a][b].value!=0){
				
				
				
				//riga
				for (int i=0;i<9;i++){
					if (slot_vector[a][i].value!=0){
						if (i!=b){
							if (slot_vector[a][i].value==slot_vector[a][b].value){return false;}
							
						}
					}
				}
				//colonna
				for (int i=0;i<9;i++){
				if (slot_vector[i][b].value!=0){
					if (i!=a){
						if (slot_vector[i][b].value==slot_vector[a][b].value){return false;}
						
					}
				}
				}
				//settore
				int blockx = (Math.round(a/3))*3;
				int blocky = (Math.round(b/3))*3;
				for (int i= blockx;i<(blockx+3);i++){
					if(i!=a){
					for (int j=blocky;j<(blocky+3);j++){
					if(j!=b){
						if (slot_vector[i][j].value!=0){
							if (slot_vector[i][j].value==slot_vector[a][b].value){return false;}
						}
					}	
					}
					}
				}
				
				
				
				
				}
				
				
			}
		}
		
		
		return true;
		
		
		
		
		
	}
	
	
	
	public boolean excavate(){
		
		Arrow base = new Arrow(-1,-1);
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
			if (!slot_vector[i][j].fixed){
				if(base.x==-1){base.x=i;base.y=j;}
				if(slot_vector[i][j].getSize()<slot_vector[base.x][base.y].getSize()){base.x=i;base.y=j;}
			}	
			}			
		}
		if(base.x==-1){return true;}
			int row = base.x;
			int col = base.y;
			int size = slot_vector[base.x][base.y].getSize();
			
			//Toast.makeText(getApplicationContext(), "Excavate to chooseController ", Toast.LENGTH_LONG).show();
			if (chooseController(row, col)){return true;}else{return false;}
			/*
			if (size==0){
				 Toast.makeText(getApplicationContext(), "Incostistent ", Toast.LENGTH_LONG).show();// inconsistente
				 return false;
			} else if (size==1){
				 Toast.makeText(getApplicationContext(), "1 size ", Toast.LENGTH_LONG).show();
				theArc.clear();
				theArc.add(new Arrow(row,col));
				arcConsistency();

			} else if (size>1){
				Toast.makeText(getApplicationContext(), "Going to chooseController ", Toast.LENGTH_LONG).show();
				if (chooseController(row, col)){return true;}else{return false;}
			}
*/
		
		
		
		
	}
	
	
	public void blindSearch(){
		 List<Constraint> theList = new ArrayList<Constraint>();
		 Arrow base = new Arrow(-1,-1);
			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
				if (!slot_vector[i][j].fixed){
					if(base.x==-1){base.x=i;base.y=j;}
					if(slot_vector[i][j].getSize()<slot_vector[base.x][base.y].getSize()){base.x=i;base.y=j;}
					theList.add(new Constraint(i,j,slot_vector[i][j].getSize()));
				}	
				}
				
			}
			
			// Sort d list o Sort the list
			
			//TODO devo trasformare l'if in while e ricostruire la lista ogni volta
			//while (!theList.isEmpty())
			while (base.x!=-1)
			//for (int alpha =0; alpha<81; alpha++)
			{
				//Collections.sort(theList);
				int row = base.x;
				int col = base.y;
				int size = slot_vector[base.x][base.y].getSize();
				
				
				//if ((theList.get(0)).size==0){
				if (size==0){
					 Toast.makeText(getApplicationContext(), "Inconsistent ", Toast.LENGTH_LONG).show();
					 
					 
					    TextView textView = new TextView(this);
					    String message = "Bad boy, girl or whatever you are. You tried solving an inconsistent Sudoku. \n What does it means inconsistent? \n Good point.. \n It means that not exists a set of valid values to be assigned. But probably.. you were wrong copying digits. So go back and correct them. \n However... nice try.. ";
					    textView.setText(message);

					    // Set the text view as the activity layout
					    setContentView(textView);
					    break;
					// inconsistente
				//} else if ((theList.get(0)).size==1){
				} else if (size==1){
					 //Toast.makeText(getApplicationContext(), "1 size ", Toast.LENGTH_LONG).show();
					//updateDirectConstraints((theList.get(0)).x, (theList.get(0)).y, (slot_vector[(theList.get(0)).x][(theList.get(0)).y].domain).get(0));
					theArc.clear();
					//theArc.add(new Arrow((theList.get(0)).x,(theList.get(0)).y));
					theArc.add(new Arrow(row,col));
					arcConsistency();
					
					
				//} else if (theList.get(0).size>1){
				} else if (size>1){
					//mo ci facciamo un sacco di risate perchè dobbiamo fare la scelta
					
					 //Toast.makeText(getApplicationContext(), "Going to chooseController ", Toast.LENGTH_LONG).show();
					//if (chooseController((theList.get(0)).x, (theList.get(0)).y)){}
					if (!chooseController(row, col)){
						
						TextView textView = new TextView(this);
					    String message = "Bad boy, girl or whatever you are. You tried solving an inconsistent Sudoku. \n What does it means inconsistent? \n Good point.. \n It means that not exists a set of valid values to be assigned. But probably.. you were wrong copying digits. So go back and correct them. \n However... nice try.. ";
					    textView.setText(message);

					    // Set the text view as the activity layout
					    setContentView(textView);
					    break;
						
						
						
					}
					
				}
				
				theList.clear();
				base = new Arrow(-1,-1);
				for(int i=0;i<9;i++){
					for(int j=0;j<9;j++){
					if (!slot_vector[i][j].fixed){
						if(base.x==-1){base.x=i;base.y=j;}
						if(slot_vector[i][j].getSize()<slot_vector[base.x][base.y].getSize()){base.x=i;base.y=j;}
						theList.add(new Constraint(i,j,slot_vector[i][j].getSize()));
					}	
					}
					
				}	
			}//!while
			
			
			
		
		
	}
	
	
public boolean darkDisseminate(int row, int col, int finalvalue){
	
	
	slot_vector[row][col].setValue(finalvalue);
	
	//riga
	for (int i=0;i<9;i++){
		if (!slot_vector[row][i].fixed){
			if (slot_vector[row][i].removeChance(finalvalue)){
				if (slot_vector[row][i].getSize()==0){return false;}else{theDarkArc.add(new Arrow(row,i));}
				
			}
		}
	}
	//colonna
	for (int i=0;i<9;i++){
	if (!slot_vector[i][col].fixed){
		if (slot_vector[i][col].removeChance(finalvalue)){
			if (slot_vector[i][col].getSize()==0){return false;}else{theDarkArc.add(new Arrow(i,col));}
			
		}
	}
	}
	//settore
	int blockx = (Math.round(row/3))*3;
	int blocky = (Math.round(col/3))*3;
	for (int i= blockx;i<(blockx+3);i++){
		if(i!=row){
		for (int j=blocky;j<(blocky+3);j++){
		if(j!=col){
			if (!slot_vector[i][j].fixed){
				if (slot_vector[i][j].removeChance(finalvalue)){
					if (slot_vector[i][j].getSize()==0){return false;}else{theDarkArc.add(new Arrow(i,j));}
					
				}
			}
		}	
		}
		}
	}
	
	return true;
}
	
public void disseminate(int row, int col, int finalvalue){
		
		slot_vector[row][col].setValue(finalvalue);
		//scorre riga
		for (int i=0;i<9;i++){

			
			if (!slot_vector[row][i].fixed){
				
				if (slot_vector[row][i].removeChance(finalvalue)){
					//slot_vector[row][i].markIt();//probabilmente inutile
					//if ((slot_vector[row][i].domain).isEmpty()){return false;}
					//if (!markedMohican(row,i)){return false;}
					theArc.add(new Arrow(row,i));
				}
			}
			
		}
		//scorre colonna
		for (int i=0;i<9;i++){
		if (!slot_vector[i][col].fixed){
			//se rimuovo marchio
			//devo aggiungere il controllo per l'arcoconsistenza
			if (slot_vector[i][col].removeChance(finalvalue)){
				//slot_vector[i][col].markIt();//probabilmente inutile
				//if ((slot_vector[i][col].domain).isEmpty()){return false;}
				//if (!markedMohican(i,col)){return false;}
				theArc.add(new Arrow(i,col));
			}
			
		}
		}
		//!riga e colonna
		
		//settore
		int blockx = (Math.round(row/3))*3;
		int blocky = (Math.round(col/3))*3;
		for (int i= blockx;i<(blockx+3);i++){
			if(i!=row){
			for (int j=blocky;j<(blocky+3);j++){
			if(j!=col){
				if (!slot_vector[i][j].fixed){
					if (slot_vector[i][j].removeChance(finalvalue)){
						//slot_vector[i][j].markIt();//probabilmente inutile
						//if ((slot_vector[i][j].domain).isEmpty()){return false;}
						//if (!markedMohican(i,j)){return false;}
						theArc.add(new Arrow(i,j));
					}
					
					/*
					 * mettere boolean removeChance 
					 * if slot_vector[i][j].remove is true lancia una funzione (esterna) passando row col e value
					 * che controlla se il valore è l'unico nella lista ( .size() )
					 * e se è così lancia updateDirectConstraints
					 * ovviamente il return true nel remove deve stare dopo il remove vero e proprio
					 * 
					 */
				}
			}	
			}
			}
		}
		//!settore
		
	}
	
		
	public boolean arcConsistency(){
		
		theDarkArc = new ArrayList<Arrow>();
		
		
		ListIterator<Arrow> iter = theArc.listIterator();
		while(iter.hasNext()){
			Arrow current = iter.next();
			if (!slot_vector[current.x][current.y].fixed){
				
				if(slot_vector[current.x][current.y].getSize()==0){return false;}
				if (slot_vector[current.x][current.y].getSize()==1){
					if(!darkDisseminate(current.x,current.y,slot_vector[current.x][current.y].getFirst())){return false;}
				}
			}
		
		}
		
		
		if (!theDarkArc.isEmpty()){
			
			theArc.clear();
			
			for (Iterator<Arrow> bucket = theDarkArc.iterator(); bucket.hasNext(); ){
				Arrow item = bucket.next();
				theArc.add(new Arrow(Integer.valueOf(item.x),Integer.valueOf(item.y)));
			}
			theDarkArc.clear();
			if(!arcConsistency()){return false;}
			
		}
		return true;
	}
	
	
	
	public boolean chooseController(int x, int y){
		
		counter++;
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				//slot_vector_backup[i][j]=new Slot();
				slot_vector_backup[counter][i][j]= Slot.deepCopy(slot_vector[i][j]); 
			}
		}
		
		leveldomain[counter] = new Scroll();
		for (Iterator<Integer> val = slot_vector[x][y].domain.iterator(); val.hasNext(); ){
			Integer item = Integer.valueOf(val.next());
			leveldomain[counter].domain.add(item);
		}
		
		for (Iterator<Integer> val = leveldomain[counter].domain.iterator(); val.hasNext(); ){
			Integer testvalue = val.next();
			theArc.clear();
			//slot_vector[x][y].setValue(supervalue);
			slot_vector[x][y].setTest(testvalue);
			//disseminate(x,y,supervalue);
			//questo deve essere il problema
			theArc.add(new Arrow(x,y));
			
			if (arcConsistency()){
				if (excavate()){
					
					//Toast.makeText(getApplicationContext(), "It's ok ", Toast.LENGTH_LONG).show();
					return true;
					
				}else{
					
					//Toast.makeText(getApplicationContext(), "Excavate failed ", Toast.LENGTH_LONG).show();
					for(int i=0;i<9;i++){
						for(int j=0;j<9;j++){
							//slot_vector[i][j]=new Slot();
							slot_vector[i][j]= Slot.deepCopy(slot_vector_backup[counter][i][j]); 
							
						}
					}
					
					
				}
				 
				
			} else {
				//Toast.makeText(getApplicationContext(), "Try again ", Toast.LENGTH_LONG).show();
				for(int i=0;i<9;i++){
					for(int j=0;j<9;j++){
						//slot_vector[i][j]=new Slot();
						slot_vector[i][j]= Slot.deepCopy(slot_vector_backup[counter][i][j]); 
						
					}
				}
				
			}
			
			
		}//!for
		counter--;
		return false;
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fifth, menu);
		return true;
	}

	
	
	
	
	
	
	
	
}
