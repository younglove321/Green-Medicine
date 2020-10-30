package ite.smu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            }
            else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                if(result.getContents().equals("http://m.site.naver.com/0uHl1") ){ //QR코드로 인식한것이 우리 서버 주소이면
                    //아두이노와 블루투스 연결하기 위한 다른 화면으로 이동
                    Intent intent = new Intent(ScanQRActivity.this, BluetoothActivity.class);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(this, "GREEN MEDICINE이 아닙니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
