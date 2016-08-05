package us.tier5.u_rang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailedPickup extends AppCompatActivity implements View.OnClickListener,AsyncResponse.Response,RegisterHandler.RegisterResponse{

    //server connectiviry variables
    HashMap<String, String> datanew = new HashMap<String,String>();
    ProgressDialog loading;
    String route = "/V1/get-prices";
    RegisterUser registerUser = new RegisterUser("POST");
    HashMap<String, String> data = null;
    String placeOrderRoute = "/V1/place-order";
    RegisterHandler registerHandler = new RegisterHandler("POST");

    //activity variables
    LinearLayout detailed_parent_ll;
    ImageView ivBackToPickUp;
    TextView confirmDetail;
    ArrayList<View> ItemObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_pickup);

        data =(HashMap<String, String>) getIntent().getSerializableExtra("dataObject");
        Log.i("kingsukmajumder","serializable data is "+data);

        registerUser.delegate = this;
        registerHandler.delegate = this;

        if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {
            loading = ProgressDialog.show(DetailedPickup.this, "Please Wait",null, true, true);
            registerUser.register(datanew,route);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }



        confirmDetail = (TextView) findViewById(R.id.confirmDetail);
        confirmDetail.setOnClickListener(this);

        ivBackToPickUp = (ImageView) findViewById(R.id.ivBackToPickUp);
        ivBackToPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        detailed_parent_ll = (LinearLayout) findViewById(R.id.detailed_parent_ll);


    }


    @Override
    public void processFinish(String output) {
        //loading.dismiss();
        Log.i("kingsukmajumder",output);

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
                            int itemId = currentPriceObj.getInt("id");

                            View inflatedLayout= getLayoutInflater().inflate(R.layout.detailed_pickup, null, false);
                            TextView tvItemName = (TextView) inflatedLayout.findViewById(R.id.tvItemName);
                            tvItemName.setText(itemName);
                            tvItemName.setTag(itemId);
                            TextView tvCategoyName = (TextView) inflatedLayout.findViewById(R.id.tvCategoyName);
                            tvCategoyName.setText(categoryName);
                            TextView tvItemPrice = (TextView) inflatedLayout.findViewById(R.id.tvItemPrice);
                            tvItemPrice.setText(itemPrice);

                            NumberPicker npItemNumber = (NumberPicker) inflatedLayout.findViewById(R.id.npItemNumber);
                            npItemNumber.setMinValue(0);
                            npItemNumber.setMaxValue(10);
                            npItemNumber.setWrapSelectorWheel(false);

                            ItemObjects.add(inflatedLayout);
                            detailed_parent_ll.addView(inflatedLayout);

                        }
                    }
                    else
                    {
                        Log.i("kingsukmajumder","pricelist is 0 for this category");
                    }

                }

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

    @Override
    public void onClick(View v) {

        JSONArray totalArray = new JSONArray();

        for(int i=0;i<ItemObjects.size();i++)
        {
            NumberPicker itemNumbers = (NumberPicker) ItemObjects.get(i).findViewById(R.id.npItemNumber);


            if(itemNumbers.getValue()!=0)
            {
                TextView tvItemName = (TextView) ItemObjects.get(i).findViewById(R.id.tvItemName);
                TextView tvItemPrice = (TextView) ItemObjects.get(i).findViewById(R.id.tvItemPrice);

                JSONObject newJsonObject = new JSONObject();
                try
                {
                    newJsonObject.put("id",tvItemName.getTag());
                    newJsonObject.put("number_of_item",itemNumbers.getValue());
                    newJsonObject.put("item_name",tvItemName.getText());
                    newJsonObject.put("item_price",tvItemPrice.getText());
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Sorry cannot add items now!",Toast.LENGTH_SHORT).show();
                    Log.i("kingsukmajumder",e.toString());
                }
                totalArray.put(newJsonObject);
            }
        }
        data.put("list_items_json",totalArray.toString());
        //Log.i("kingsukmajumder",data.toString());
        if(totalArray.length()!=0)
        {
            if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
            {
                registerHandler.register(data,placeOrderRoute);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"You need to select at least one item! Or you can place a fast pickup.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void registerProcessFinish(String response) {
        Log.i("kingsukmajumder",response);
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("status"))
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailedPickup.this,DashboardNew.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error while placing order!",Toast.LENGTH_SHORT).show();
            Log.i("kingsukmajumder",e.toString());
        }


    }
}
