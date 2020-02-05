package nl.sunflare.sunflare_sensorviewer.Classes;

import android.os.Handler;
import android.os.Looper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Databaselogger {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mStatusChecker;
    private int UPDATE_INTERVAL = 1000;


    public Databaselogger() {
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                // Run the passed runnable
                Date c = new Date();
                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                String currentTime = time.format(c);
                String currentDate = date.format(c);
                Map<String, Object> childUpdates = new HashMap<>();
                DataProcessor.currentSession.setCurrentTime(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()));
                childUpdates.put("/logs/" + "/" + currentDate + "/" + currentTime, DataProcessor.currentSession);
                mDatabase.updateChildren(childUpdates);
                System.out.println("writing to db.");
                // Re-run it after the update interval
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    public  void startUpdates(){
        mStatusChecker.run();
    }

    public  void stopUpdates(){
        mHandler.removeCallbacks(mStatusChecker);
    }
}
