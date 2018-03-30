package com.harikesh.smsaes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    Button send;
    EditText key, to, msg;
    String eMsg, eKey, eTo;
    byte[] encryptedMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = findViewById(R.id.send);
        key = findViewById(R.id.key);
        msg = findViewById(R.id.msg);
        to = findViewById(R.id.to);
        final SmsManager sms = SmsManager.getDefault();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 123);
        } else {
            Toast.makeText(this, "Give permissions", Toast.LENGTH_SHORT).show();
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eMsg = msg.getText().toString();
                eKey = key.getText().toString();
                eTo = to.getText().toString();
                if (eMsg.isEmpty() || eKey.isEmpty() || eTo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    encryptedMsg = encryptS(eKey, eMsg);
                    String msgString = byte2Hex(encryptedMsg);
                    sms.sendTextMessage(eTo,null,msgString,null,null);
                    Toast.makeText(MainActivity.this, "Message Sent!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String byte2Hex(byte[] encryptedMsg) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < encryptedMsg.length; i++) {
            stmp = Integer.toHexString(encryptedMsg[i] & 0xFF);
            if (stmp.length() == 1) {
                hs += ("0" + stmp);
            } else {
                hs += stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] encryptS(String eKey, String eMsg) {
        try {
            byte[] returnArray;
            Key key = generateKey(eKey);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            returnArray = c.doFinal(eMsg.getBytes());
            return returnArray;
        } catch (Exception ex) {
            ex.printStackTrace();
            byte[] returnArray = null;
            return returnArray;
        }
    }

    private static Key generateKey(String eKey) throws Exception {
        Key key = new SecretKeySpec(eKey.getBytes(), "AES");
        return key;
    }
}
