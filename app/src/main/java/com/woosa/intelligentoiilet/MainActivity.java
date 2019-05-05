package com.woosa.intelligentoiilet;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IGetMessageCallBack, viewAdapter.Callback {
    private MyServiceConnection serviceConnection;
    private IBinder binder;
    private Button btButton;

    private List<myBean> myBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mqtt service
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MainActivity.this);
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    //
    private class MyServiceConnection implements ServiceConnection {
        private MQTTService mqttService;
        private IGetMessageCallBack IGetMessageCallBack;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = iBinder;
            mqttService = ((MQTTService.CustomBinder)iBinder).getService();
            mqttService.setIGetMessageCallBack(IGetMessageCallBack);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

        public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
            this.IGetMessageCallBack = IGetMessageCallBack;
        }
    }

    //mqtt service
    @Override
    public void setMessage(String message) {
        ListView listView = findViewById(R.id.listview);
        myBean bean1 = new myBean(message,R.mipmap.ic_launcher);
        myBeanList.add(bean1);
        viewAdapter adapter = new viewAdapter(MainActivity.this,R.layout.view_adapter,myBeanList, this);
        listView.setAdapter(adapter);
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
                Parcel data=Parcel.obtain();
                data.writeString("CMD_GET_INIT_DATA");
                Parcel reply=Parcel.obtain();
                try {
                    binder.transact(IBinder.LAST_CALL_TRANSACTION, data, reply, 0);
                } catch (RemoteException e) {
                    //TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //从service里读数据
                //Log.i("Main<<<<<<<<",reply.readString());
                //Log.i("Main<<<<<<<<<",reply.readInt()+"");
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

    @Override
    public void click(View v) {
        Toast.makeText(MainActivity.this, "listview的内部的按钮被点", Toast.LENGTH_SHORT).show();
    }
}