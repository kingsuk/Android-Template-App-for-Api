package us.tier5.u_rang;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements AsyncResponse.Response {
    TextView signUp;
    Button siginInButton;
    HashMap<String, String> data = new HashMap<String,String>();
    TextView email;
    TextView pass;
    String route = "urang1/V1/login";

    RegisterUser registerUser = new RegisterUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser.delegate = this;

        email = (TextView) findViewById(R.id.email);
        pass = (TextView) findViewById(R.id.pass);


        signUp = (TextView) findViewById(R.id.signUp);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        siginInButton = (Button) findViewById(R.id.siginInButton);
        siginInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String InputEmail = email.getText().toString();
                String InputPass = pass.getText().toString();




                data.put("email",InputEmail);
                data.put("password",InputPass);

                if(CheckNetwork.isInternetAvailable(getApplication())) //returns true if internet available
                {
                    registerUser.register(data,route);
                }
                else
                {
                    Toast.makeText(getApplication(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(getApplicationContext(),output,Toast.LENGTH_SHORT).show();
    }
}
