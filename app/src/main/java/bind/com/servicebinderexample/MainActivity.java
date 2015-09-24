package bind.com.servicebinderexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private UnLeashedService mUnLeashedService = null;
    private boolean mIsBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showDialog();
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



    private void showDialog(){
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

}
