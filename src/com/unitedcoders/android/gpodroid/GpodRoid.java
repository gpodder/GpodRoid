package com.unitedcoders.android.gpodroid;


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
    
    public static String LOGTAG = "GPR";
    public static Preferences prefs;

    public static final String BROADCAST_SUBSCRIPTION_CHANGE = "test321";


    protected void addApplicationModules(List<Module> modules){
        modules.add(new GpodRoidModule());
    }


    private class GpodRoidModule extends AbstractAndroidModule {
        @Override
        protected void configure() {

        }
    }
}