package us.tier5.u_rang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String DEFAULT = "";
    ImageView nextPage1;
    ImageView previousPage2;
    ImageView nextPage2;
    ImageView previousPage3;
    TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        final Animation slide_in_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_left);
        final Animation slide_in_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        final Animation slide_out_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_left);
        final Animation slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);


        final LinearLayout page1 = (LinearLayout) findViewById(R.id.sign_up_1);
        final LinearLayout page2 = (LinearLayout) findViewById(R.id.sign_up_2);
        final LinearLayout page3 = (LinearLayout) findViewById(R.id.sign_up_3);


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

                page1.setVisibility(View.INVISIBLE);
                page2.setVisibility(View.VISIBLE);
                page1.startAnimation(slide_out_left);
                page2.startAnimation(slide_in_right);
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
                page2.startAnimation(slide_out_left);
                page2.setVisibility(View.INVISIBLE);
                page3.setVisibility(View.VISIBLE);
                page3.startAnimation(slide_in_right);

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
    }
}
