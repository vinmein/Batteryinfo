package com.example.vasanth.batterymeteralarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private TextView batteryLevel;
    private TextView voltageLevel;
    private TextView temperatureLevel;
    private ImageView batterymeter;
    private TextView Technology;
    private TextView pluged;
    Ringtone r1,r;
    private TextView BatteryStats;
   public ToggleButton t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
t1=(ToggleButton) findViewById(R.id.toggleButton2);

        batteryLevel = (TextView)findViewById(R.id.battery_level);
        voltageLevel = (TextView)findViewById(R.id.voltage_level);
        temperatureLevel = (TextView)findViewById(R.id.temperature_level);
        batterymeter=(ImageView) findViewById(R.id.battery);
        Technology=(TextView)findViewById(R.id.technology);
        pluged=(TextView) findViewById(R.id.plugged);
        BatteryStats=(TextView)findViewById(R.id.BatteryStatus);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri notificatn = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r1 = RingtoneManager.getRingtone(getApplicationContext(), notificatn);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
       String togglevalue=t1.getText().toString();
        if(togglevalue=="on"){
            Intent intent = new Intent(this, BatteryService.class);
            startService(intent);
        }else{
            Intent intent = new Intent(this, BatteryService.class);
            stopService(intent);
        }
    }


    public BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {

            AudioManager alrmmode = (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if(level==100) {
                switch (alrmmode.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:
                        Uri sound=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notifybell.mp3");
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
                        r.play();
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        alrmmode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        Uri notificatn = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Uri soundd=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +getPackageName()+ "/raw/notifybell.mp3");
                        Ringtone r1 = RingtoneManager.getRingtone(getApplicationContext(), soundd);
                        r1.play();
                        break;
                }
                int plgnw=intent.getIntExtra("plugged", -1);
                if(getPlugTypeString(plgnw)=="Not Charging")
                {
                    r1.stop();
                    r.stop();

                }

            }
            int voltage = intent.getIntExtra("voltage", 0);
            String technology = intent.getStringExtra("technology");
            int plugd = intent.getIntExtra("plugged", -1);
            Technology.setText("Battery Technology:" +technology);
            pluged.setText("Plugged State:"+getPlugTypeString(plugd));
            int temperature = intent.getIntExtra("temperature", 0);
            batteryLevel.setText("Battery Status: " + String.valueOf(level) + "%");
            voltageLevel.setText("Battery Voltage: " + String.valueOf(voltage));
            double temps = (double)temperature / 10;
            temperatureLevel.setText("Battery Temperature: " + String.valueOf(temps));
            int health = intent.getIntExtra("health", 0);
            BatteryStats.setText("BatteryHealth:"+getHealthString(health));
        }
    };
    private String getPlugTypeString(int plugged) {
        String plugType = "Not Charging";

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
        }

        return plugType;
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";

        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
        }

        return healthString;
    }

}
