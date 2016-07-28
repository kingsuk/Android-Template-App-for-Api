package us.tier5.u_rang;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements AsyncResponse.Response {
    TextView signUp;
    Button siginInButton;
    HashMap<String, String> data = new HashMap<String,String>();
    TextView email;
    TextView pass;
    String route = "/V1/login";
    int user_id;
    ProgressDialog loading;
    
    RegisterUser registerUser = new RegisterUser("POST");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //checking if the user already logged in or not
        SharedPreferences prefs = getSharedPreferences("U-rang", MODE_PRIVATE);
        int restoredText = prefs.getInt("user_id", 0);

        if (restoredText != 0)
        {
            Intent i = new Intent(LoginActivity.this,Dashboard.class);
            startActivity(i);
        }


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

                    loading = ProgressDialog.show(LoginActivity.this, "Please Wait",null, true, true);
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
        //Toast.makeText(getApplicationContext(),output,Toast.LENGTH_SHORT).show();
        loading.dismiss();
        try
        {
            JSONObject jsonObject = new JSONObject(output);
            Boolean status = jsonObject.getBoolean("status");

            if(status)
            {
                String responseArray = jsonObject.getString("response");
                JSONArray jsonArray = new JSONArray(responseArray);
                
                for (int i=0;i<jsonArray.length();i++)
                {
                    user_id = jsonArray.getJSONObject(i).getInt("id");

                }
                SharedPreferences.Editor editor = getSharedPreferences("U-rang", MODE_PRIVATE).edit();
                editor.putInt("user_id", user_id);
                if(editor.commit())
                {
                    Toast.makeText(getApplication(),"Login Successful.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplication(),"Some problem occoured, You may have to login again when you launch the app!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                    startActivity(intent);
                }

            }
            else
            {
                Snackbar.make(this.findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
        catch (Exception e)
        {
            Log.i("kingsukmajumder",e.toString());
            Toast.makeText(getApplication(),"Error in login",Toast.LENGTH_SHORT).show();
        }

    }
}
