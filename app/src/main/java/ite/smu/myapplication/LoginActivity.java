package ite.smu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button logBtn, signBtn;
    EditText pwd, id;
//    HttpPost httppost;
//    HttpResponse response;
//    HttpClient httpclient;
//    List<NameValuePair> nameValuePairs;
//    ProgressDialog dialog = null;
//    TextView tv;
    private static final String TAG = "LoginActivity";
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logBtn = (Button)findViewById(R.id.loginButton);
        signBtn = (Button)findViewById(R.id.signupButton);
        id = (EditText)findViewById(R.id.idInput);
        pwd = (EditText)findViewById(R.id.passwordInput);
       // tv = (TextView)findViewById(R.id.textView2);
//        adImg = (ImageView)findViewById(R.id.imageAd);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id", id.getText().toString());
                startActivity(intent);

                ///일단 밑에 주석처리해놓음...ㅎㅎ
//                dialog = ProgressDialog.show(LoginActivity.this, "", "Validating user...", true);
//                new Thread(new Runnable() {
//                    public void run() {
//                        login();
//                    }
//                }).start();
            }
        });


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);//아직구현안함
                startActivity(intent);
            }
        });

 /*
        adImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(intent);
            }
        });
        */

    }

    void login() {//로그인함수 내부 구현해야함!!!!!!!
//        try {
//            httpclient = new DefaultHttpClient();
//            httppost = new HttpPost("http://172.30.1.3/login.php");
//            nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("username", id.getText().toString()));
//            nameValuePairs.add(new BasicNameValuePair("password", pwd.getText().toString()));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            response = httpclient.execute(httppost);
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            final String response = httpclient.execute(httppost, responseHandler);
//            System.out.println("Response : " + response);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tv.setText("Response from PHP : " + response);
//                    dialog.dismiss();
//                }
//            });
//
//            if (response.equalsIgnoreCase("User Found")) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                startActivity((new Intent(LoginActivity.this, MainActivity.class)));
//                //finish();
//            } else {
//                //Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } catch(Exception e) {
//            dialog.dismiss();
//            System.out.println("Exception : " + e.getMessage());
//        }
    }

    public void CliSignUp(View view) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }

}
