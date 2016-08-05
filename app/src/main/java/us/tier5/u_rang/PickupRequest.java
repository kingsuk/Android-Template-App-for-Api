package us.tier5.u_rang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Switch;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class PickupRequest extends AppCompatActivity implements AsyncResponse.Response,RegisterHandler.RegisterResponse,DatePickerDialog.OnDateSetListener,View.OnClickListener{
    //server connectivity variables
    HashMap<String, String> data = new HashMap<String,String>();
    RegisterUser registerUser = new RegisterUser("POST");
    RegisterHandler registerHandler = new RegisterHandler("POST");
    String route = "/V1/get-user-details";
    String placeOrderRoute = "/V1/place-order";
    ProgressDialog loading;

    //variables
    TextView userName;
    TextView name;
    EditText address;
    ImageView pick_up_date;
    TextView pick_up_dateText;
    TextView submitSchedule;
    Spinner schedule;
    Spinner strach_type;
    EditText spcl_ins;
    EditText driving_ins;
    Spinner pay_method;
    Spinner client_type;

    //values
    String order_type;
    String actualDate = "";

    //switch variables
    com.rey.material.widget.Switch boxed_or_hungSwitch;
    TextView boxed_or_hungText;

    com.rey.material.widget.Switch urang_bagSwitch;
    TextView urang_bagText;

    com.rey.material.widget.Switch doormanSwitch;
    TextView doormanText;

    com.rey.material.widget.Switch isEmergencySwitch;
    TextView isEmergencyText;

    com.rey.material.widget.Switch wash_n_foldSwitch;
    TextView wash_n_foldText;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submitSchedule = (TextView)findViewById(R.id.submitSchedule);
        submitSchedule.setOnClickListener(this);




        order_type = getIntent().getStringExtra("order_type");
        //Log.i("kingsukmajumder",order_type);
        data.put("order_type",order_type);
        data.put("boxed_or_hung","boxed");//by default boxed is set
        data.put("doorman","0");//by default door man is no
        data.put("urang_bag","0");//by default bag is no
        data.put("wash_n_fold","0");//by default wash and fold is no
        data.put("isEmergency","0");//b default emergency is zero

        SharedPreferences sharedPreferences = getSharedPreferences("U-rang", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", 0);

        //fetching all the data b user id
        data.put("user_id",""+user_id);

        registerUser.delegate = this;
        registerHandler.delegate = this;

        if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {
            loading = ProgressDialog.show(PickupRequest.this, "Please Wait",null, true, true);
            registerUser.register(data,route);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }




        //edit text initialization
        userName = (TextView) findViewById(R.id.userName);
        name = (TextView) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        schedule = (Spinner) findViewById(R.id.schedule);
        strach_type = (Spinner) findViewById(R.id.strach_type);
        spcl_ins = (EditText) findViewById(R.id.spcl_ins);
        driving_ins = (EditText) findViewById(R.id.driving_ins);
        pay_method = (Spinner) findViewById(R.id.pay_method);
        client_type = (Spinner) findViewById(R.id.client_type);


        pick_up_date = (ImageView) findViewById(R.id.pick_up_date);
        pick_up_dateText = (TextView) findViewById(R.id.pick_up_dateText);
        pick_up_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                final DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PickupRequest.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.setThemeDark(true);

                dpd.registerOnDateChangedListener(new DatePickerDialog.OnDateChangedListener() {
                    @Override
                    public void onDateChanged() {
                        //Toast.makeText(getApplicationContext(),pick_up_dateText.getText(),Toast.LENGTH_SHORT).show();
                        dpd.notifyOnDateListener();
                    }
                });
                dpd.show(getFragmentManager(),"dsfasdf");
            }
        });



        boxed_or_hungSwitch = (com.rey.material.widget.Switch) findViewById(R.id.boxed_or_hungSwitch);
        boxed_or_hungText = (TextView) findViewById(R.id.boxed_or_hungText);
        boxed_or_hungSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                //Toast.makeText(getApplication(),""+checked,Toast.LENGTH_SHORT).show();
                if(checked)
                {
                    boxed_or_hungText.setText("hung");
                    data.put("boxed_or_hung","hung");
                }
                else
                {
                    boxed_or_hungText.setText("boxed");
                    data.put("boxed_or_hung","boxed");
                }
            }
        });

        doormanSwitch = (com.rey.material.widget.Switch) findViewById(R.id.doormanSwitch);
        doormanText = (TextView) findViewById(R.id.doormanText);
        doormanSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked)
                {
                    doormanText.setText("yes");
                    data.put("doorman","1");
                }
                else
                {
                    doormanText.setText("no");
                    data.put("doorman","0");
                }
            }
        });

        urang_bagSwitch = (com.rey.material.widget.Switch) findViewById(R.id.urang_bagSwitch);
        urang_bagText = (TextView) findViewById(R.id.urang_bagText);
        urang_bagSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked)
                {
                    urang_bagText.setText("yes");
                    data.put("urang_bag","1");
                }
                else
                {
                    urang_bagText.setText("no");
                    data.put("urang_bag","0");
                }
            }
        });

        wash_n_foldSwitch = (com.rey.material.widget.Switch) findViewById(R.id.wash_n_foldSwitch);
        wash_n_foldText = (TextView) findViewById(R.id.wash_n_foldText);
        wash_n_foldSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked)
                {
                    wash_n_foldText.setText("yes");
                    data.put("wash_n_fold","1");
                }
                else
                {
                    wash_n_foldText.setText("no");
                    data.put("wash_n_fold","0");
                }
            }
        });

        isEmergencySwitch = (com.rey.material.widget.Switch) findViewById(R.id.isEmergencySwitch);
        isEmergencyText = (TextView) findViewById(R.id.isEmergencyText);
        isEmergencySwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked)
                {
                    isEmergencyText.setText("yes");
                    data.put("isEmergency","1");
                }
                else
                {
                    isEmergencyText.setText("no");
                    data.put("isEmergency","0");
                }
            }
        });

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
    public void registerProcessFinish(String response) {
        loading.dismiss();
        Log.i("kingsukmajumder","the response is  "+response);
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("status"))
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PickupRequest.this,DashboardNew.class);
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

    @Override
    public void processFinish(String output) {
        loading.dismiss();
        Log.i("kingsukmajumder",""+output);
        try{
            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("status"))
            {
                JSONObject jsonObj = new JSONObject(jsonObject.getString("response"));
                userName.setText(jsonObj.getString("email"));
                JSONObject user_details = new JSONObject(jsonObj.getString("user_details"));
                name.setText(user_details.getString("name"));
                address.setText(user_details.getString("address"));

            }
            else
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Cannot fetch user details!",Toast.LENGTH_SHORT).show();
            Log.i("kingsukmajumder",e.toString());
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String dateToShow = (monthOfYear+1)+"/"+dayOfMonth+"/"+year;
        actualDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        pick_up_dateText.setText(dateToShow);

        //Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(!address.getText().toString().equals("") && schedule.getSelectedItemPosition()!=0 && !actualDate.equals("") && pay_method.getSelectedItemPosition()!=0 && client_type.getSelectedItemPosition()!=0)
        {
            data.put("address",address.getText().toString());
            data.put("pick_up_date",actualDate);
            data.put("schedule",schedule.getSelectedItem().toString());
            data.put("strach_type",strach_type.getSelectedItem().toString());
            data.put("spcl_ins",spcl_ins.getText().toString());
            data.put("driving_ins",driving_ins.getText().toString());
            data.put("pay_method",""+pay_method.getSelectedItemPosition());
            data.put("client_type",client_type.getSelectedItem().toString());

            if(order_type.equals("1"))
            {
                Log.i("kingsukmajumder","data is "+data.toString());


                if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
                {
                    loading = ProgressDialog.show(PickupRequest.this, "Please Wait",null, true, true);
                    registerHandler.register(data,placeOrderRoute);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Select items.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PickupRequest.this,DetailedPickup.class);
                intent.putExtra("dataObject",data);
                startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"All * fields are required!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("kingsukmajumder","onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("kingsukmajumder","onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("kingsukmajumder","onResume");
    }
}
