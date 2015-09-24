package bind.com.servicebinderexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private UnLeashedService mUnLeashedService = null;
    private boolean mIsBound = false;
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity Thread Id is : "  + Thread.currentThread().getId());
        showDialog();
        createHandler();
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            // iBinder returns instance of our LocalBinder of UnLeashedService class.
            // typecast it to LocalBinder so we can get instance of service.

            UnLeashedService.LocalBinder localBinder = (UnLeashedService.LocalBinder) iBinder;
            mUnLeashedService = localBinder.getService();

            // to identify that service is bounded to activity.
            mIsBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        // Bind to UnLeashedService
        Intent intent = new Intent(this, UnLeashedService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mIsBound = false;
    }


    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("This is alert Dialog");
        alertDialogBuilder.setPositiveButton("SHOW TOAST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (mIsBound) {
                    mUnLeashedService.doSomeHeavyWorkHere();
                } else {
                    Toast.makeText(getApplicationContext(), "service is not " +
                            "bound to activity yet.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }


    /**
     * Creates handler for main Thread's message queue.
     * It will allow background thread to send messages to this (main/ui) thread.
     */

    private void createHandler() {

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);

                Log.d(TAG, "in handleMessage().");
                Log.d(TAG, "MainActivity (Handler) Thread Id is : "  + Thread.currentThread().getId());

                if (message != null) {
                    Bundle data = message.getData();

                    if (data != null && data.getBoolean("download_completed")) {
                        Toast.makeText(getApplicationContext(), "Download completed.", Toast
                                .LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Download failed.", Toast
                                .LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "no data in handleMessage().");
                }

            }
        };

        // started background thread to do some long running task.
        // and passed handler of main thread so that background thread can send messages to main thread using it's handler
        new MyThread(mHandler).start();

    }


    public Handler getHandler() {
        return mHandler;
    }

}