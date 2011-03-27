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
    
    public static String LOGTAG = "GpodRoid";
    public static Preferences prefs;


    protected void addApplicationModules(List<Module> modules){
        modules.add(new GpodRoidModule());
    }


    private class GpodRoidModule extends AbstractAndroidModule {
        @Override
        protected void configure() {

        }
    }
}