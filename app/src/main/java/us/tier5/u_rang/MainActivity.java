package us.tier5.u_rang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse.Response,RegisterHandler.RegisterResponse{
    //server connectivity variables
    HashMap<String, String> data = new HashMap<String,String>();
    RegisterUser registerUser = new RegisterUser("POST");
    RegisterHandler registerHandler = new RegisterHandler("POST");
    String route = "/V1/check-email";
    String registerRoute = "/V1/sign-up-user";
    ProgressDialog loading;
    ProgressDialog loadingSecond;


    EmailValidator emailValidator = new EmailValidator();
    private static String DEFAULT = "";
    ImageView nextPage1;
    ImageView previousPage2;
    ImageView nextPage2;
    ImageView previousPage3;
    TextView signin;
    Button submitRegister;
    final int GET_NEW_CARD = 2;



    //setting all the text field variables
    TextView email;
    TextView password;
    TextView conf_password;
    TextView name;
    TextView personal_phone;
    TextView address;

    TextView driving_instruction;
    TextView spcl_instruction;
    TextView cell_phone;
    TextView office_phone;

    TextView cardholder_name;
    Spinner cardtype;
    TextView card_no;
    TextView cvv;
    Spinner select_month;
    Spinner select_year;

    //animation variables
    Animation slide_in_left;
    Animation slide_in_right;
    Animation slide_out_left;
    Animation slide_out_right;

    //Layout variables
    LinearLayout page1;
    LinearLayout page2;
    LinearLayout page3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUser.delegate = this;
        registerHandler.delegate = this;

        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        name = (TextView) findViewById(R.id.name);
        personal_phone = (TextView) findViewById(R.id.personal_phone);
        address = (TextView) findViewById(R.id.address);
        conf_password = (TextView) findViewById(R.id.conf_password);

        driving_instruction = (TextView) findViewById(R.id.driving_instruction);
        spcl_instruction = (TextView) findViewById(R.id.spcl_instruction);
        cell_phone = (TextView) findViewById(R.id.cell_phone);
        office_phone = (TextView) findViewById(R.id.office_phone);

        cardholder_name = (TextView) findViewById(R.id.cardholder_name);
        cardtype = (Spinner) findViewById(R.id.cardtype);
        card_no = (TextView) findViewById(R.id.card_no);
        cvv = (TextView) findViewById(R.id.cvv);
        select_month = (Spinner) findViewById(R.id.select_month);
        select_year = (Spinner) findViewById(R.id.select_year);








        slide_in_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);


        page1 = (LinearLayout) findViewById(R.id.sign_up_1);
        page2 = (LinearLayout) findViewById(R.id.sign_up_2);
        page3 = (LinearLayout) findViewById(R.id.sign_up_3);


        signin = (TextView) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });

        nextPage1 = (ImageView) findViewById(R.id.nextPage1);
        nextPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailValidator.validate(email.getText().toString()))
                {
                    if(!email.getText().toString().equals("") && !password.getText().toString().equals("") && !conf_password.getText().toString().equals("") && !name.getText().toString().equals("") && !personal_phone.getText().toString().equals("") && !address.getText().toString().equals(""))
                    {
                        if(password.getText().toString().equals(conf_password.getText().toString()))
                        {
                            data.put("email",email.getText().toString());

                            loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
                            registerUser.register(data,route);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Passwords did not matched!",Toast.LENGTH_SHORT).show();
                        }



                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Every field is required",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter a valid email!",Toast.LENGTH_SHORT).show();
                }




            }
        });
        previousPage2 = (ImageView) findViewById(R.id.previousPage2);
        previousPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                page2.startAnimation(slide_out_right);
                page2.setVisibility(View.INVISIBLE);
                page1.setVisibility(View.VISIBLE);
                page1.startAnimation(slide_in_left);
            }
        });
        nextPage2 = (ImageView) findViewById(R.id.nextPage2);
        nextPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.put("driving_instruction",driving_instruction.getText().toString());
                data.put("spcl_instruction",spcl_instruction.getText().toString());
                data.put("cell_phone",cell_phone.getText().toString());
                data.put("office_phone",office_phone.getText().toString());

                Intent intent = new Intent(MainActivity.this, CardEditActivity.class);
                startActivityForResult(intent,GET_NEW_CARD);

                /*page2.startAnimation(slide_out_left);
                page2.setVisibility(View.INVISIBLE);
                page3.setVisibility(View.VISIBLE);
                page3.startAnimation(slide_in_right);*/

                //Log.i("kingsukmajumder",data.toString());

            }
        });
        previousPage3 = (ImageView) findViewById(R.id.previousPage3);
        previousPage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page3.startAnimation(slide_out_right);
                page3.setVisibility(View.INVISIBLE);
                page2.setVisibility(View.VISIBLE);
                page2.startAnimation(slide_in_left);
            }
        });
        submitRegister = (Button) findViewById(R.id.submitRegister);
        submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(select_month.getSelectedItemPosition()!=0 && !cardholder_name.getText().toString().equals("") && !card_no.getText().toString().equals("") && !cardtype.getSelectedItem().toString().equals("Select Card") && select_year.getSelectedItemPosition()!=0)
                {
                    data.put("cardholder_name",cardholder_name.getText().toString());
                    data.put("card_no",card_no.getText().toString());
                    data.put("cardtype",cardtype.getSelectedItem().toString());
                    data.put("cvv",cvv.getText().toString());
                    data.put("select_month",Integer.toString(select_month.getSelectedItemPosition()));
                    data.put("select_year",select_year.getSelectedItem().toString());

                    //Log.i("kingsukmajumder",data.toString());
                    loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
                    registerHandler.register(data,registerRoute);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"All fields are required!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent creditData) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {

            String cardHolderName = creditData.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = creditData.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = creditData.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cardCvv = creditData.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            // Your processing goes here.
            String creditCardType = "";

            if (cardNumber.startsWith("4"))
                creditCardType = "Visa";
            else if (cardNumber.startsWith("5"))
                creditCardType = "MasterCard";
            else if (cardNumber.startsWith("6"))
                creditCardType = "Discover";
            else if (cardNumber.startsWith("37"))
                creditCardType = "American Express";
            else
                creditCardType = "Unknown type";

            List<String> expiryMonth = Arrays.asList(expiry.split("/"));
            List<String> expiryYear = Arrays.asList(expiry.substring(3,5));

            Log.i("kingsukmajumder",expiryYear.get(0));

            data.put("cardholder_name",cardHolderName);
            data.put("card_no",cardNumber);
            data.put("cardtype",creditCardType);
            data.put("cvv",cardCvv);
            data.put("select_month",expiryMonth.get(0));
            data.put("select_year",expiryYear.get(0));

            //loadingSecond = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
            registerHandler.register(data,registerRoute);

        }
    }

    @Override
    public void processFinish(String output) {
        loading.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("status"))
            {
                data.put("password",password.getText().toString());
                data.put("conf_password",conf_password.getText().toString());
                data.put("name",name.getText().toString());
                data.put("personal_phone",personal_phone.getText().toString());
                data.put("address",address.getText().toString());

                page1.setVisibility(View.INVISIBLE);
                page2.setVisibility(View.VISIBLE);
                page1.startAnimation(slide_out_left);
                page2.startAnimation(slide_in_right);

            }
            else
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message")+"Please try with another.",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error while checking email!",Toast.LENGTH_SHORT).show();
            Log.i("kingsukmajumder",e.toString());
        }


    }

    @Override
    public void registerProcessFinish(String response) {
        Log.i("kingsukmajumder",response);
        loading.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("status"))
            {
                JSONObject responseObj = new JSONObject(jsonObject.getString("response"));
                int user_id = responseObj.getInt("user_id");
                SharedPreferences.Editor editor = getSharedPreferences("U-rang", MODE_PRIVATE).edit();
                editor.putInt("user_id", user_id);
                if(editor.commit())
                {
                    Toast.makeText(getApplication(),"Registration Successful.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,DashboardNew.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplication(),"Some problem occoured, You may have to login again when you launch the app!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,DashboardNew.class);
                    startActivity(intent);
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Can't register now! Please try again.",Toast.LENGTH_SHORT).show();
        }


    }
}
