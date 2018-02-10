package com.example.testopencvstatic;

public class Constraint implements Comparable<Constraint> {
	
	public int x;
	public int y;
	public int size;
	
	public Constraint(int row,int col, int some){
		x=row;
		y=col;
		size=some;
	}
	
	public Constraint(int row,int col){
		x=row;
		y=col;
	}
	
	public int getSize(){
		return size;
	}
	
	 @Override
	 public int compareTo(Constraint c2) {
		 int compareSize = ((Constraint) c2).getSize();
		 
	 return size - compareSize;
	 }
	
}
