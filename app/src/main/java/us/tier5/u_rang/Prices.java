package us.tier5.u_rang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Prices extends AppCompatActivity implements AsyncResponse.Response{

    private ArrayList<BeanClassForListView> beanClassArrayList;
    private listViewAdapter listViewAdapter;
    ListView listview;
    HashMap<String, String> data = new HashMap<String,String>();
    ProgressDialog loading;
    String route = "/V1/get-prices";
    RegisterUser registerUser = new RegisterUser("POST");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerUser.delegate = this;


        listview = (ListView) findViewById(R.id.listView);
        beanClassArrayList = new ArrayList<BeanClassForListView>();

        loading = ProgressDialog.show(Prices.this, "Please Wait",null, true, true);
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



        try{
            JSONObject jsonObject = new JSONObject(output);

            if(jsonObject.getBoolean("status"))
            {
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject currentObj = jsonArray.getJSONObject(i);
                    String categoryName = currentObj.getString("name");
                    JSONArray priceListArray = currentObj.getJSONArray("pricelists");
                    if(priceListArray.length()!=0)
                    {

                        for (int j=0;j<priceListArray.length();j++)
                        {
                            JSONObject currentPriceObj = priceListArray.getJSONObject(j);
                            String itemName = currentPriceObj.getString("item");
                            String itemPrice = currentPriceObj.getString("price");

                            BeanClassForListView beanClass = new BeanClassForListView(R.drawable.price, itemName, itemPrice,categoryName);
                            beanClassArrayList.add(beanClass);

                        }
                    }
                    else
                    {
                        Log.i("kingsukmajumder","pricelist is 0");
                    }





                }
                listViewAdapter = new listViewAdapter(Prices.this, beanClassArrayList);
                listview.setAdapter(listViewAdapter);

            }
            else
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error in fetching prices",Toast.LENGTH_SHORT).show();
            Log.i("kingsukmajumder",e.toString());
        }
        loading.dismiss();
    }
}
