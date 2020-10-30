package ite.smu.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button mileageBtn, mapBtn, scanQRBtn;
    private TextView tv_mileage;

    private String url = "http://13.125.132.81/test.php"; //연결할 웹서버 주소
    private ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        values.put("SAVING_MILEAGE", "1000");
        NetworkTask networkTask = new NetworkTask(url, values); //앱이랑 웹서버랑 연결
        networkTask.execute();


        mileageBtn = (Button) findViewById(R.id.mileageBtn);
        mapBtn = (Button) findViewById(R.id.mapBtn);
        scanQRBtn = (Button) findViewById(R.id.scanQRBtn);
        tv_mileage = (TextView)findViewById(R.id.tv_mileage);

        /////////////////////////////////////////////////////////////////////
        String usrid = getIntent().getStringExtra("id");
        //db에서 마일리지 받아와서 다시 설정필요!!!!!!!!!!!!!!!!!!!!!!!!!!!11111111
        //////////////////////////////////////////////////////////////////////////


        mileageBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, MileageActivity.class);
                startActivity(intent);

            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        scanQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
                startActivity(intent);
                //finish();
            }
        });

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
//            executeClient();

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_mileage.setText(s);
            doJSONParser(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        }
    }

    void doJSONParser(String str){
        try{
            String result = "";
            String tmp = "";
            JSONObject order = new JSONObject(str);
            JSONArray index = order.getJSONArray("webnautes");
            for (int i = 0; i < index.length(); i++) {
                JSONObject tt = index.getJSONObject(i);
                result += tt.getString("user_mileage");
                tmp += tt.getString("saving_mileage");
            }
            tv_mileage.setText(result);
        }
        catch (JSONException e){ ;}
    }
}
