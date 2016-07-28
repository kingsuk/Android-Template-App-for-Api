package us.tier5.u_rang;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Neighborhood extends AppCompatActivity implements AsyncResponse.Response{

    LinearLayout neighborParent;
    private ArrayList<BeanClassForListView> beanClassArrayList;
    private listViewAdapter listViewAdapter;
    HashMap<String, String> data = new HashMap<String,String>();
    ProgressDialog loading;
    String route = "/V1/get-neighborhoods";
    RegisterUser registerUser = new RegisterUser("POST");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        neighborParent = (LinearLayout) findViewById(R.id.neigborhood_parent);

        registerUser.delegate = this;
        loading = ProgressDialog.show(Neighborhood.this, "Please Wait",null, true, true);
        registerUser.register(data,route);





       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void processFinish(String output) {

        loading.dismiss();
        Log.i("kingsukmajumder",output);

        try{
            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("status"))
            {
                String response = jsonObject.getString("response");
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject currentObj = jsonArray.getJSONObject(i);
                    String name = currentObj.getString("name");
                    String description = Html.fromHtml(currentObj.getString("description")).toString();
                    String imageName = currentObj.getString("image");
                    final String imageUrl = "http://162.243.64.194/public/dump_images/"+imageName;

                    View inflatedLayout= getLayoutInflater().inflate(R.layout.neighborhood, null, false);
                    TextView title = (TextView) inflatedLayout.findViewById(R.id.tvNeighborhoodTitle);
                    title.setText(name);
                    TextView text = (TextView) inflatedLayout.findViewById(R.id.tvNeighborhoodText);
                    text.setText(description);
                    final TextView loadingTV = (TextView) inflatedLayout.findViewById(R.id.tvNeighborLoading);
                    /*View linlaHeaderProgress = (View) findViewById(R.id.progressBarNeighbor);
                    linlaHeaderProgress.setVisibility(View.VISIBLE);*/
                    final ImageView imageViewNeigbor = (ImageView) inflatedLayout.findViewById(R.id.ivNeighborhoodImage);

                    new AsyncTask<Void, Void, Void>() {
                        Bitmap bmp;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                InputStream in = new URL(imageUrl).openStream();
                                bmp = BitmapFactory.decodeStream(in);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Some error occoured while loading images!",Toast.LENGTH_LONG).show();
                                Log.i("kingsukmajumder","error in loading images "+e.toString());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            //loading.dismiss();
                            if (bmp != null)
                                loadingTV.setVisibility(View.INVISIBLE);
                                imageViewNeigbor.setImageBitmap(bmp);
                        }
                    }.execute();


                    neighborParent.addView(inflatedLayout);
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error in fetching Neighborhood",Toast.LENGTH_SHORT).show();
            Log.i("kingsukmajumder",e.toString());
        }

    }
}
