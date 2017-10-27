package spondonict.com.phonetracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView tvcellid,tvmcc,tvmnc,tvlac,tvimei,tvssno,tvssid,tvphone;
    Button btdata,btconnect,btnetwork;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=10;
    TelephonyManager tm;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    final int MY_PERMISSIONS_REQUEST_LOC=100;
    final int MY_PERMISSIONS_REQUEST_PSTATE=110;
    int resultCode=0;
    boolean threadFlag=false;
    boolean buttonflag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionCrossLoc();
        checkPermissionPhoneState();
        initializeAll();
    }
    private void checkPermissionPhoneState() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_PSTATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void checkPermissionCrossLoc() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOC);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PSTATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(),"Permission Granted ",Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(),"Permission not Granted ",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(),"Permission Granted ",Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(),"Permission not Granted ",Toast.LENGTH_SHORT).show();
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    void initializeAll(){

        tvcellid=(TextView) findViewById(R.id.tvcellid);
        tvmcc=(TextView)findViewById(R.id.tvmcc);
        tvmnc=(TextView)findViewById(R.id.tvmnc);
        tvlac=(TextView)findViewById(R.id.tvlac);
        tvimei=(TextView)findViewById(R.id.tvimei);
        btdata=(Button)findViewById(R.id.btdata);
        tvssno=(TextView)findViewById(R.id.tvssno);
        tvssid=(TextView)findViewById(R.id.tvssid);
        tvphone=(TextView)findViewById(R.id.tvphone);
        btconnect=(Button)findViewById(R.id.btconnect);
        btnetwork=(Button)findViewById(R.id.btnetwork);
         tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        btdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    printInfos();
                }else{
                    //printInfos();
                    getInfos();
                }
                //getInfos(); //for sdk greater than 23
            }
        });

        btconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckConnectivity();
            }
        });

        final Thread thread=new Thread(new networkThread());
        btnetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonflag){
                    threadFlag=false;
                    buttonflag=false;
                    btnetwork.setText("Start");
                    //thread.stop();

                }else{
                    buttonflag=true;
                    threadFlag=true;
                    btnetwork.setText("Stop");
                   thread.start();



                }
            }
        });
    }

    class networkThread implements Runnable{
        @Override
        public void run() {
            while (threadFlag) {

                try {
                    netWork n=new netWork();
                    n.execute();
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class netWork extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String result= checkNetwork();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }
    }


    private String checkNetwork() {
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ){
            String networktype=networkInfo.getTypeName().toLowerCase();
            if(networktype.equals("wifi")){
                //Toast.makeText(getApplicationContext(),networktype,Toast.LENGTH_SHORT).show();
                WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                //Toast.makeText(getApplicationContext(),"Connect to "+ssid,Toast.LENGTH_SHORT).show();
                return "Connect to "+ssid;
            }else if(networktype.equals("mobile")){
                return "Connect to mobile data";
            }
        }else{
            //Toast.makeText(getApplicationContext(),"No connectivity",Toast.LENGTH_SHORT).show();
        }
        return "No Connectivity";
    }

    @TargetApi(21)
    void getInfos(){
        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        try{
            List<CellInfo> cellinofs=tm.getAllCellInfo();
            tvmnc.setText("size: "+cellinofs.size());

            for(CellInfo cinfo:cellinofs) {
                if(cinfo instanceof CellInfoGsm) {
                    CellInfo info = (CellInfoGsm)cinfo;
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                    CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
                    CellSignalStrengthGsm cellStrength = cellInfoGsm.getCellSignalStrength();
                    int cid = cellIdentity.getCid();
                    int lac = cellIdentity.getLac();
                    tvmcc.setText("MCC: " + cellIdentity.getMcc());
                    tvmnc.setText("MNC: " + cellIdentity.getMnc());
                    tvcellid.setText("CellID: " + cid);
                    tvlac.setText("LAC: " + lac);
                    tvimei.setText("IMEI: " + tm.getDeviceId());
                    tvssno.setText("SSNO: "+tm.getSimSerialNumber());
                    tvssid.setText("SSID: "+tm.getSubscriberId());
                    tvphone.setText("PhN: "+tm.getLine1Number());
                    //break;
                }

            }
        }catch (Exception e){
            tvmcc.setText(e.getMessage());
        }
        /*CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        CellSignalStrengthGsm cellStrength = cellInfoGsm.getCellSignalStrength();
        int cid=cellIdentity.getCid();
        int lac=cellIdentity.getLac();
        tvmcc.setText("MCC: "+ cellIdentity.getMcc());
        tvmnc.setText("MNC: " + cellIdentity.getMnc());
        tvcellid.setText("CellID: "+cid);
        tvlac.setText("LAC: "+lac);
        tvimei.setText("IMEI: "+tm.getDeviceId());*/
    }
    @TargetApi(18)
    void printInfos(){
        //TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation)tm.getCellLocation();
        String networkOperator = tm.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);
        //tvmcc.setText("MCC: " + mcc);
        tvmcc.setText("MCC: "+ mcc);
        tvmnc.setText("MNC: " + mnc);

        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();

        tvcellid.setText("CellID: "+cid);
        tvlac.setText("LAC: "+lac);
        tvimei.setText("IMEI: "+tm.getDeviceId());
        tvssno.setText("SSNO: "+tm.getSimSerialNumber());
        tvssid.setText("SSID: "+tm.getSubscriberId());
        tvphone.setText("PhN: "+tm.getLine1Number());
    }


    void CheckConnectivity(){
        //do your work inside onPostExecute method
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ){
            sendRequest sendRequest=new sendRequest();
            sendRequest.execute();
        }else{
            Toast.makeText(getApplicationContext(),"Connection not available",Toast.LENGTH_SHORT).show();
        }
    }

    class sendRequest extends AsyncTask<String,Integer,Integer>{
            String result="error";
        @Override
        protected Integer doInBackground(String... params) {
            int code=0;
            HttpURLConnection urlc=null;
            try
            {
                urlc = (HttpURLConnection) ((new URL("http://www.google.com")).openConnection());
                urlc.setConnectTimeout(4000); //choose your own timeframe
                urlc.setReadTimeout(3000); //choose your own timeframe
                code = urlc.getResponseCode();
                result=urlc.getResponseMessage();
            } catch (IOException e)
            {
                result=e.getMessage();
            }finally {
                if(urlc!=null){
                    try {
                        urlc.disconnect();
                    }catch (Exception e){

                    }
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            if(code==200){
                ///do your code here that is performed after internet connection check
                Intent intent=new Intent(MainActivity.this,PostRequestToAsp.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Internet available ",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext()," Connectivity OK but Internet not available ",Toast.LENGTH_SHORT).show();
            }
        }
    }



}
