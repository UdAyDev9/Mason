package narasimhaa.com.mitraservice.Application;

import android.app.Application;

public class AppController extends Application {

private static AppController        mInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		mInstance = this;
	}

	public static  AppController getInstance() {
		return mInstance;
	}


}
