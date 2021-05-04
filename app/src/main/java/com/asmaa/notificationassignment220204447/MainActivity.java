package com.asmaa.notificationassignment220204447;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String password;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         email=getIntent().getStringExtra("email");
         password=getIntent().getStringExtra("password");
        getToken();


    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                   String token= task.getResult();
                    Log.d("asmaa", "token: "+token);
                    addRegestrationToken(token);
                }else {
                    Log.d("asmaa", "error token: "+task.getException());

                }
            }
        });

    }

    private void addRegestrationToken(final String token) {
        StringRequest request = new StringRequest(Request.Method.PUT,
                "https://mcc-users-api.herokuapp.com/add_reg_token"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    Boolean status = j.getBoolean("status");
                    Log.i("asmaa", "Status add "+status);
                    if (status) {
                        JSONObject j2 = j.getJSONObject("data");
                        Log.i("asmaa", "data is "+j);

                        String code = j2.getString("Registration Code");
                        Log.i("asmaa", "code is "+code);
                    } else {
                        Toast.makeText(MainActivity.this, "Faild", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asmaa", "errorrr add");
                Log.e("response", error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap m = new HashMap();
                m.put("email", email);
                m.put("password", password);
                m.put("reg_token", token);

                return m;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(request);
    }


}