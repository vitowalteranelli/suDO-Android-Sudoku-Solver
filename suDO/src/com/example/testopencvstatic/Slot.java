package com.example.testopencvstatic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Slot {
	
	public int row;
	public int col;
	public int value;
	public List<Integer> domain = new ArrayList<Integer>();
	public boolean fixed = false;
	public boolean mark = false;
	
	
	public static Slot deepCopy(Slot original){
		Slot copy;
		copy = new Slot();
		copy.row = Integer.valueOf(original.row);
		copy.col = Integer.valueOf(original.col);
		copy.value = Integer.valueOf(original.value);
		copy.domain =  new ArrayList<Integer>();
		
		for (Iterator<Integer> val = original.domain.iterator(); val.hasNext(); ){
			Integer item = Integer.valueOf(val.next());
			copy.domain.add(item);
		}
		copy.fixed = Boolean.valueOf(original.fixed);
		copy.mark =  Boolean.valueOf(original.mark);
		
		return copy;
	}
	
	public void setSlot(int x, int y, int v){
		row = x;
		col = y;
		value = v;	
		
	}

	public Slot(){
		
		//for(int i=0;i<9;i++){domain.add(i+1);}
	}
	
	public Slot(int x, int y){
		row = x;
		col = y;
		for(int i=0;i<9;i++){domain.add(i+1);}
	}
	
	public Slot(int x, int y, int v){
		row = x;
		col = y;
		value = v;
		domain = new ArrayList<Integer>();
		domain.clear();
		if (value!=0){
			fixed=true;
			domain.add(Integer.valueOf(v));
			
			
		} else {
			domain.add(Integer.valueOf(1));
			domain.add(Integer.valueOf(2));
			domain.add(Integer.valueOf(3));
			domain.add(Integer.valueOf(4));
			domain.add(Integer.valueOf(5));
			domain.add(Integer.valueOf(6));
			domain.add(Integer.valueOf(7));
			domain.add(Integer.valueOf(8));
			domain.add(Integer.valueOf(9));
			
			
			
		}
		
		
		
	}
	
	public Slot(int x, int y, String v){
		row = x;
		col = y;
		value = Integer.parseInt(v);
		for(int i=0;i<9;i++){domain.add(i+1);}
	}
	
	public int getSize(){
		int sum=0;
	for (Iterator<Integer> val = domain.iterator() ; val.hasNext();){
				
				
				if (val.next()!=100){
					sum++;
				}
				
			}
		return sum;
	}
	
	public int getFirst(){
		
		int valid = -1;//domain.get(Integer.valueOf(0));
		
		for (Iterator<Integer> val = domain.iterator() ; val.hasNext();){
			
			int temp = val.next();
			if (valid==-1){
				valid = temp;
			}
			
		}
		return valid;
	}
	
	public void setValue(int v){
		value = v;
		fixed = true;
		
		domain.clear();
		domain.add(Integer.valueOf(v));
		/*for (Iterator<Integer> val = domain.iterator() ; val.hasNext();){
			
			
			if (val.next()!=value){
				//domain.remove(val);
				val.remove();
			}
			
		}*/
		
		
		
	}
	
	public void setValue(String v){
		value = Integer.parseInt(v);
		fixed = true;
		
		domain.clear();
		domain.add(Integer.valueOf(value));
		
		/*for (Iterator<Integer> val = domain.iterator() ; val.hasNext();){
			
			
			if (val.next()!=value){
				//domain.remove(val);
				val.remove();
			}
			
		}*/
		
	}
	
	public void setTest(int test){
		value = test;
		domain.clear();
		domain.add(Integer.valueOf(value));
		
	}
	
	public boolean removeChance(int domnum){
		int check = domnum;
		
		//if (this.domain.remove(Integer.valueOf(domnum))){return true;}else{return false;}
		if (this.domain.remove(Integer.valueOf(domnum)) )
		{return true;}else{return false;}
		
		/*
		for (Iterator<Integer> val = domain.iterator() ; val.hasNext();){
			
			
			if (val.next()==check){
				//domain.remove(val);
				val.remove();
				return true;
			}
			
		}*/
		
		
		/*for (Integer val : domain){
			if (domain.get(val)==check){
				domain.remove(val);
				return true;
			}
			
		}*/
	}
	
	
	/*public boolean removeChance(String domnum){
		int check = Integer.parseInt(domnum);
		for (Iterator<Integer> val = domain.iterator() ; val.hasNext();){
			
			
			if (val.next()==check){
				//domain.remove(val);
				val.remove();
				return true;
			}
			
		}
		
		return false;
	}*/
	/*
	public boolean isThereOtherFrom(int domnum){
		int check = domnum;
		for (Integer val : domain){
			if (domain.get(val)!=check){
				return true;
				
			}
		}
		return false;
	}
	
	public boolean isThereOtherFrom(String domnum){
		int check = Integer.parseInt(domnum);
		for (Integer val : domain){
			if (domain.get(val)!=check){
				return true;
				
			}
		}
		return false;
	}*/
	

	
	public void markIt(){
		
		mark = true;
	}
	
	public void unMarkIt(){
		
		mark = false;
	}
	
}
