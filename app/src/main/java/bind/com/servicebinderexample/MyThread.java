package bind.com.servicebinderexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by shivam on 9/24/15.
 */
public class MyThread extends Thread {

    private Handler mHandler;
    private static final String TAG = MyThread.class.getSimpleName();

    public MyThread(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void run() {

        try {
            Log.d(TAG, "MyThread - Thread Id is : "  + Thread.currentThread().getId());

            Log.d(TAG, "in run() before interrupting thread.");
            Thread.sleep(3000);
            Log.d(TAG, "in run() after interrupting thread.");

            Message message = Message.obtain();
            Bundle data =  new Bundle();
            data.putBoolean("download_completed", true);
            message.setData(data);

            mHandler.sendMessage(message);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
