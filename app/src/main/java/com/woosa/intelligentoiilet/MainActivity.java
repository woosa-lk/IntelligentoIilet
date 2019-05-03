package com.woosa.intelligentoiilet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IGetMessageCallBack{
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mqtt service
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MainActivity.this);
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        //Intent intent_list=new Intent();
        //intent_list.setClass(MainActivity.this, MyAdapterActivity.class);
        //startActivity(intent_list);
    }
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
    //mqtt service
    @Override
    public void setMessage(String message) {
        TextView view = findViewById(R.id.wenzi);
        view.setText(message);
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.manage:
                break;
            case R.id.add:
                Intent intent_list=new Intent();
                intent_list.setClass(MainActivity.this, MyAdapterActivity.class);
                startActivity(intent_list);
                break;
            case R.id.delete:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
