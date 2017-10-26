package spondonict.com.phonetracker;

import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Created by USER on 10/22/2017.
 */

public class MobileData {

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=10;
    TelephonyManager tm;
    int  CID=0,LAC=0,MCC=0,MNC=0;
    String IMEI="";

    public MobileData(TelephonyManager tm){
        this.tm=tm;
    }

    void getCellInfos() {

        try {
            List<CellInfo> cellinofs = tm.getAllCellInfo();

            for (CellInfo cinfo : cellinofs) {
                if (cinfo instanceof CellInfoGsm) {
                    CellInfo info = (CellInfoGsm) cinfo;
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                    CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
                    CellSignalStrengthGsm cellStrength = cellInfoGsm.getCellSignalStrength();

                    int CID = cellIdentity.getCid();
                    int LAC = cellIdentity.getLac();
                    int MCC=cellIdentity.getMcc();
                    int MNC=cellIdentity.getMnc();
                    String IMEI=tm.getDeviceId();
                    if(CID>0 && LAC>0 && MCC>0 && MNC>0 && !IMEI.equals("")){
                        setCID(CID);
                        setMNC(MNC);
                        setLAC(LAC);
                        setMCC(MCC);
                        setIMEI(IMEI);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public int getLAC() {
        return LAC;
    }

    public void setLAC(int LAC) {
        this.LAC = LAC;
    }

    public int getMCC() {
        return MCC;
    }

    public void setMCC(int MCC) {
        this.MCC = MCC;
    }

    public int getMNC() {
        return MNC;
    }

    public void setMNC(int MNC) {
        this.MNC = MNC;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

}
