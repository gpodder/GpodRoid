package com.unitedcoders.android.gpodroid;


import android.content.Context;
import com.google.inject.Module;
import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;

import java.util.List;

/**
 * Host of TabActivity and general info of state.
 * 
 * @author Nico Heid
 * 
 */
public class GpodRoid extends RoboApplication {
    
	/**
	 * the tag to be referenced in the log cat
	 */
    public static String LOGTAG = "GPR";
    
    public static final String BROADCAST_SUBSCRIPTION_CHANGE = "test321";
    
    /**
     * This member is meant to be populated right at the beginning of the application in the "main" function
     * and will be referenced for the duration of the application
     */
    public static Context context;
    
    public GpodRoid(){
    	context = this;
    }

    protected void addApplicationModules(List<Module> modules){
        modules.add(new GpodRoidModule());
    }

    private class GpodRoidModule extends AbstractAndroidModule {
        @Override
        protected void configure() {
        	Preferences.initPreferences();
        }
    }
}