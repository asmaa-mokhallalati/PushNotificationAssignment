package com.asmaa.notificationassignment220204447;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignUPActivity extends AppCompatActivity {

    private EditText mFistNameSign;
    private EditText mLastnameSign;
    private EditText mEmailSign;
    private EditText mPasswordSign;
    private Button mSignBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);
        mFistNameSign = findViewById(R.id.fistName_sign);
        mLastnameSign = findViewById(R.id.lastname_sign);
        mEmailSign = findViewById(R.id.email_sign);
        mPasswordSign = findViewById(R.id.password_sign);
        mSignBtn = findViewById(R.id.sign_btn);

        mSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });
    }

    private void createNewUser() {
        StringRequest request = new StringRequest(Request.Method.POST,
                "https://mcc-users-api.herokuapp.com/add_new_user"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    Boolean status = j.getBoolean("status");
                    Log.i("asmaa", "Status sign "+status);
                    if (status) {
                        JSONObject j2 = j.getJSONObject("data");
                        String id = j2.getString("id");

                        Intent intent = new Intent(SignUPActivity.this,LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUPActivity.this, "Faild", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asmaa", "errorrr sign"+error.getMessage());
                Log.i("response", error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap m = new HashMap();
                m.put("firstName", mFistNameSign.getText().toString());
                m.put("secondName", mLastnameSign.getText().toString());
                m.put("email", mEmailSign.getText().toString());
                m.put("password", mPasswordSign.getText().toString());
                return m;
            }
        };
        Volley.newRequestQueue(SignUPActivity.this).add(request);
    }

}