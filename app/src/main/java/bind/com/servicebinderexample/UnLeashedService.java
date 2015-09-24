package bind.com.servicebinderexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shivam on 9/24/15.
 */
public class UnLeashedService extends Service {

    private static final String TAG = UnLeashedService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "in onBind()");
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "in onCreate()");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "in onDestroy()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "in onUnbind()");

        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "in onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

}