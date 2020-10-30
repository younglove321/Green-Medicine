package ite.smu.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치

    private static String address = "201807133CC9";//내 bluetooth mac address 입력
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // 블루투스 활성화하기
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정

        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            // 여기에 처리 할 코드를 작성하세요.
            Toast.makeText(this, "Cancelled. Not Support Bluetooth", Toast.LENGTH_LONG).show();
        }
        else { // 디바이스가 블루투스를 지원 할 때
            if(bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
            }
            else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)
                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 선택한 값이 onActivityResult 함수에서 콜백된다.
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void selectBluetoothDevice() {
        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();
        // 페어링 된 디바이스의 크기를 저장
        int pariedDeviceCount = devices.size();
        // 페어링 되어있는 장치가 없는 경우
        if(pariedDeviceCount == 0) {
            // 페어링을 하기위한 함수 호출
        }
        // 페어링 되어있는 장치가 있는 경우
        else {

            connectDevice("GREENMEDICINE");
            //여기수정. 블루투스 특정 address가진것만 연결하려고!
//            // 디바이스를 선택하기 위한 다이얼로그 생성
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
//            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
//            List<String> list = new ArrayList<>();
//            // 모든 디바이스의 이름을 리스트에 추가
//            for(BluetoothDevice bluetoothDevice : devices) {
//                list.add(bluetoothDevice.getName());
//            }
//            list.add("취소");
//            // List를 CharSequence 배열로 변경
//            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
//            list.toArray(new CharSequence[list.size()]);
//            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
//            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // 해당 디바이스와 연결하는 함수 호출
//                    connectDevice(charSequences[which].toString());
//                }
//            });
//            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
//            builder.setCancelable(false);
//            // 다이얼로그 생성
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            //여기까지 블루투스 수정한 부분임
        }
    }

    public void connectDevice(String deviceName) {
        // 페어링 된 디바이스들을 모두 탐색
        for(BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
            if(deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            // 데이터 송,수신 스트림을 얻어옵니다.
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
            // 데이터 수신 함수 호출
            receiveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        Toast.makeText(this, "GREEN MEDICINE이 연결되었습니다..", Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        // 데이터를 수신하기 위한 버퍼를 생성
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        // 데이터를 수신하기 위한 쓰레드 생성
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while(!(Thread.currentThread().isInterrupted())) {
                    count++;
                    try {
                        // 데이터를 수신했는지 확인합니다.
                        int byteAvailable = inputStream.available();
                        // 데이터가 수신 된 경우
                        if(byteAvailable > 0) {
                            System.out.println("데이터 수신 성공");
                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);

                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
                            for(int i = 0; i < byteAvailable; i++) {
                                final byte tempByte = bytes[i];
                                // 개행문자를 기준으로 받음(한줄)
                                if(tempByte == '\n') {
                                    // readBuffer 배열을 encodedBytes로 복사
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    // 인코딩 된 바이트 배열을 문자열로 변환
                                    final String text = new String(encodedBytes, "UTF-8");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 텍스트 뷰에 출력
                                            System.out.println(inputStream);

                                            String str = text;
                                            StringTokenizer st = new StringTokenizer(str);

                                            if(st.countTokens() == 3){//센서로부터 받아온 토큰이 3개 이상일 경우에만

                                                String str_pressure = st.nextToken();
                                                String str_distance = st.nextToken();
                                                String str_light = st.nextToken();

                                                int pressV, traffV, distanV;

                                                pressV = Integer.parseInt(str_pressure);
                                                traffV = Integer.parseInt(str_light);
                                                distanV = Integer.parseInt(str_distance);

                                               // isDangerous(pressV, traffV, distanV);

//                                                try {
//                                                    Thread.sleep(1000);
//                                                } catch (InterruptedException e) {
//                                                    e.printStackTrace();
//                                               }

//                                                textViewReceive.append(str_pressure);
//                                                textViewReceive.append(str_distance);
//                                                textViewReceive.append(str_light);
//                                                textViewReceive.append("\n");


                                            }


//                                            1) 압력센서 눌린거 여부 on :1 / off : 0
//                                            2) 거리 10보다 작으면  1 / 크면 0
//                                            3) 초록불 0 / 빨간불 1


                                        }
                                    });
                                } // 개행 문자가 아닐 경우
                                else {
                                    readBuffer[readBufferPosition++] = tempByte;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        // 1초마다 받아옴
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(count >= 5){
                        //sound_flag = false;
                        count = 0;
                    }
                }
            }
        });
        workerThread.start();
    }

    void sendData(String text) {
        // 문자열에 개행문자("\n")를 추가해줍니다.
        text += "\n";
        try{
            // 데이터 송신
            outputStream.write(text.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
