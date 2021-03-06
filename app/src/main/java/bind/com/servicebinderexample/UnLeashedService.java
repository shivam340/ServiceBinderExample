package bind.com.servicebinderexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shivam on 9/24/15.
 */
public class UnLeashedService extends Service {

    private static final String TAG = UnLeashedService.class.getSimpleName();

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "in onBind()");
        return mBinder;
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


    /**
     * Class used for client binder so it can call public methods of service.
     * This service will run in same process so no need to worry about IPC.
     */
    public class LocalBinder extends Binder{

        UnLeashedService getService(){
            return UnLeashedService.this;
        }
    }


    /** method for clients */
    public void doSomeHeavyWorkHere() {
        Log.e(TAG , " working in service  , don't disturb");
        Toast.makeText(getApplicationContext(), "Hello From Service.", Toast.LENGTH_SHORT).show();
    }

}