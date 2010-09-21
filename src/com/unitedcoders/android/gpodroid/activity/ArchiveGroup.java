package com.unitedcoders.android.gpodroid.activity;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ArchiveGroup extends ActivityGroup {

    public static ArchiveGroup group;
    private ArrayList<View> history;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.history = new ArrayList<View>();
        group = this;
        
        View view = getLocalActivityManager().startActivity("ArchiveActivity", 
                new Intent(this, ArchiveActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        
        setContentView(view);
    }
    
   

}
