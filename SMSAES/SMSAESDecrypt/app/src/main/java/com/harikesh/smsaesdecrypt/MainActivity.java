package com.harikesh.smsaesdecrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText key, Emsg;
    Button dec;
    String sKey, sMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key = findViewById(R.id.key);
        Emsg = findViewById(R.id.msg);
        dec = findViewById(R.id.decrypt);

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sKey = key.getText().toString();
                sMsg = Emsg.getText().toString();
                Log.d("msg", sMsg);
                Log.d("key", sKey);
                if (sMsg.isEmpty() || sKey.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        byte msg[] = hex2byte(sMsg.getBytes());
                        byte result[] = decryptSMS(sKey, msg);
                        String ans = new String(result);
                        Emsg.setText(ans);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public static byte[] decryptSMS(String sKey, byte[] msg) throws Exception {
        Key key = generateKey(sKey);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = c.doFinal(msg);
        return decValue;
    }

    private static Key generateKey(String sKey) {
        Key key = new SecretKeySpec(sKey.getBytes(), "AES");
        return key;
    }

    public static byte[] hex2byte(byte[] bytes) {
        if ((bytes.length % 2) != 0) {
            throw new IllegalArgumentException("hello");
        }
        byte[] b2 = new byte[bytes.length / 2];
        for (int i = 0; i < bytes.length; i += 2) {
            String item = new String(bytes, i, 2);
            b2[i / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
