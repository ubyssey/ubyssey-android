package ca.ubc.ubyssey;

import android.app.Application;

/**
 * Application class, where long-running tasks should be instantiated
 * <p/>
 * Created by Chris Li on 3/20/2015.
 */
//@ReportsCrashes(formKey = "", // will not be used
//                mailTo = "chrisli@hotmail.ca")
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Remove ACRA library as soon the application becomes public
        //ACRA.init(this);
    }
}
