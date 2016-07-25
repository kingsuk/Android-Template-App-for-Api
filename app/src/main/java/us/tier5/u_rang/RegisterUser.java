package us.tier5.u_rang;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by root on 15/7/16.
 */
public class RegisterUser {

    public AsyncResponse.Response delegate = null;


    public void register(final HashMap<String, String> data, String route) {
        final String FEED_URL = UserConstants.BASE_URL+route;
        class RegisterUserData extends AsyncTask<String, Void, String> {

            ConnectToServer ruc = new ConnectToServer();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.i("kingsukmajumder",s);

                delegate.processFinish(s);
            }

            @Override
            protected String doInBackground(String... params) {

                String result = ruc.sendPostRequest(FEED_URL,data);

                return  result;
            }
        }


        RegisterUserData ru = new RegisterUserData();

        ru.execute();

    }
}
