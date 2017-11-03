package spondonict.com.phonetracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class CDMA extends AppCompatActivity {

    TextView tvmcc,tvsid,tvbid,tvnid;
    Button btdata;
    TelephonyManager tm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdm);
        tvmcc=(TextView)findViewById(R.id.tvmcc);
        tvsid=(TextView)findViewById(R.id.tvsid);
        tvbid=(TextView)findViewById(R.id.tvbid);
        tvnid=(TextView)findViewById(R.id.tvnid);
        btdata=(Button)findViewById(R.id.btgetdata);

        btdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdatacdma();
            }
        });


    }

    private void setdatacdma() {
        tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation location=tm.getCellLocation();
        List<CellInfo> infos=tm.getAllCellInfo();
        try{
        for(CellInfo info:infos) {

            if (info instanceof CellInfoCdma) {
                CellInfoCdma cdma = (CellInfoCdma) info;
                CellIdentityCdma identityCdma = cdma.getCellIdentity();
                tvmcc.setText("" + 470);
                tvbid.setText("" + identityCdma.getBasestationId());
                tvnid.setText("" + identityCdma.getNetworkId());
                tvsid.setText("" + identityCdma.getSystemId());
            }
        }

    }catch (Exception e){
        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
       /* if(location instanceof CdmaCellLocation){
            CdmaCellLocation cdmaLocation = (CdmaCellLocation) location;
            tvmcc.setText(""+470);
            tvbid.setText(""+cdmaLocation.getBaseStationId());
            tvnid.setText(""+cdmaLocation.getNetworkId());
            tvsid.setText(""+cdmaLocation.getSystemId());
        }else{
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        }*/
    }
}
