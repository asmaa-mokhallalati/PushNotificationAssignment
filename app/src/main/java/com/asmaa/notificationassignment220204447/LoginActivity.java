package com.asmaa.notificationassignment220204447;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText mLoginEmail;
    private EditText mLoginPass;
    private Button mLoginBtn;
    private TextView mGoToSignAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginEmail = findViewById(R.id.login_email);
        mLoginPass = findViewById(R.id.login_pass);
        mLoginBtn = findViewById(R.id.login_btn);
        mGoToSignAct = findViewById(R.id.go_to_signAct);
        mGoToSignAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUPActivity.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkemailAndPassword();
            }
        });
    }

    private void checkemailAndPassword() {
        StringRequest request = new StringRequest(Request.Method.POST,
                "https://mcc-users-api.herokuapp.com/login"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    Boolean status = j.getBoolean("status");
                    Log.i("asmaa", "Status login " + status);
                    if (status) {
                        JSONObject j2 = j.getJSONObject("data");
                        String id = j2.getString("id");
                        Log.i("asmaa", id);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", mLoginEmail.getText().toString());
                        intent.putExtra("password", mLoginPass.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Faild", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asmaa", "errorrr login");

                Log.e("response", error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap m = new HashMap();
                m.put("email", mLoginEmail.getText().toString());
                m.put("password", mLoginPass.getText().toString());
                return m;
            }
        };
        Volley.newRequestQueue(LoginActivity.this).add(request);
    }
}