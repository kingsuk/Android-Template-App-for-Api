package us.tier5.u_rang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class Faq extends AppCompatActivity implements AsyncResponse.Response{

    RegisterUser registerUser = new RegisterUser("POST");
    HashMap<String, String> data = new HashMap<String,String>();
    String route = "/V1/get-faq";
    LinearLayout lm;
    LinearLayout.LayoutParams LLparams;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        lm = (LinearLayout) findViewById(R.id.llParent);


        registerUser.delegate = this;
        registerUser.register(data,route);
        loading = ProgressDialog.show(Faq.this, "Please Wait",null, true, true);


        LLparams = new LinearLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.MATCH_PARENT, 1.0f);




        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("U-rang", MODE_PRIVATE).edit();
            editor.putInt("user_id", 0);
            if(editor.commit())
            {
                Intent intent = new Intent(Faq.this,LoginActivity.class);
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String output) {
        loading.dismiss();

        try
        {
            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("status"))
            {
                String arrayString = jsonObject.getString("response");
                JSONArray jsonArray = new JSONArray(arrayString);

                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject faqObj = jsonArray.getJSONObject(i);

                    String question = faqObj.getString("question");
                    String answer = Html.fromHtml(faqObj.getString("answer")).toString();
                    String imageName = faqObj.getString("image");
                    final String imageUrl = "http://162.243.64.194/public/dump_images/"+imageName;

                    View inflatedLayout= getLayoutInflater().inflate(R.layout.neighborhood, null, false);
                    TextView title = (TextView) inflatedLayout.findViewById(R.id.tvNeighborhoodTitle);
                    title.setText(question);
                    TextView text = (TextView) inflatedLayout.findViewById(R.id.tvNeighborhoodText);
                    text.setText(answer);
                    final TextView loadingTV = (TextView) inflatedLayout.findViewById(R.id.tvNeighborLoading);
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


                    lm.addView(inflatedLayout);//adding the total laout to parent layout
                }

            }
            else
            {
                Toast.makeText(getApplication(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            Log.i("kingsukmajumder",e.toString());
        }


    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }


}
