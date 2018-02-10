package com.example.testopencvstatic;

import android.app.Application;
import android.graphics.Bitmap;

public class Global extends Application{
     
	public Bitmap[][] image_vector;
	public String[][] string_vector;
    private String name;
    private String email;
    public int modifiedvalue;
     
 
    public String getName() {
         
        return name;
    }
     
    public void setName(String aName) {
        
       name = aName;
         
    }
    
    public String getEmail() {
         
        return email;
    }
     
    public void setEmail(String aEmail) {
        
      email = aEmail;
    }
 
}