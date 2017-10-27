package spondonict.com.phonetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PostRequestToAsp extends AppCompatActivity {

    Button btsend;
    EditText etsend;
    String url=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request_to_asp);
        url="http://192.168.1.116:8088/Home/Create";
        initializeAll();
    }

    private void initializeAll() {
        btsend=(Button)findViewById(R.id.btsend);
        etsend=(EditText)findViewById(R.id.etid);

        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostRequest sendPostRequest=new sendPostRequest();
                sendPostRequest.execute(url,etsend.getText().toString(),etsend.getText().toString(),"5","6","6");
            }
        });
    }

    ///////////////////////////

    class sendPostRequest extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(PostRequestToAsp.this);
            progressDialog.setMessage("Inserting...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String loginurl=params[0];
            //String loginurl="http://jsonweb.comxa.com/login.php";
            String id=params[1];
            String mcc=params[2];
            String mnc=params[3];
            String lac=params[4];
            String cid=params[5];

            try {
                URL url=new URL(loginurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setChunkedStreamingMode(0);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data= URLEncoder.encode("ID","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"
                        +URLEncoder.encode("MCC","UTF-8")+"="+URLEncoder.encode(mcc,"UTF-8")+"&"
                        +URLEncoder.encode("MNC","UTF-8")+"="+URLEncoder.encode(mnc,"UTF-8")+"&"
                        +URLEncoder.encode("LAC","UTF-8")+"="+URLEncoder.encode(lac,"UTF-8")+"&"
                        +URLEncoder.encode("CID","UTF-8")+"="+URLEncoder.encode(cid,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response=new StringBuilder();
                String line="";
                if((line=bufferedReader.readLine())!=null){
                    response.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response.toString().trim();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    ////////////////////////////
}
