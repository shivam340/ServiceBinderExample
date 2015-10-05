# ServiceBinderExample

Procedure

- Create Service class and override it's onBind()

    - inside service class create Binder class
       e.g

     /**
          * Class used for client binder so it can call public methods of service.
          * This service will run in same process so no need to worry about IPC.
          */
         public class LocalBinder extends Binder{

             UnLeashedService getService(){
                 return UnLeashedService.this;
             }
         }

    - in onBind Of service return object of LocalBinder class.
     e.g

            //Binder given to clients
            private final IBinder mBinder = new LocalBinder();

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {

                Log.i(TAG, "in onBind()");
                return mBinder;
            }



- In Activity class

       - create object of ServiceConnection


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



          - In OnStart bindService to activity and pass service connection object as parameter.


              @Override
                 protected void onStart() {
                     super.onStart();

                     // Bind to UnLeashedService
                     Intent intent = new Intent(this, UnLeashedService.class);
                     bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                 }



          - Now u can any public method of service from activity using object of service

                if (mIsBound) {
                    mUnLeashedService.doSomeHeavyWorkHere();
                }