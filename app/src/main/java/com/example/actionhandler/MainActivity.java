package com.example.actionhandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.validation.Validator;

public class MainActivity extends AppCompatActivity {

    EditText et;
    RadioButton rb_url, rb_geo, rb_telephone;
    RadioGroup rg;
    float dval1, dval2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb_url = findViewById(R.id.is_url);
        rb_geo = findViewById(R.id.is_geo);
        rb_telephone = findViewById(R.id.is_telephone);
        et = findViewById(R.id.reqdata);
        rg = findViewById(R.id.rbtns);

    }
    public void goURL(String data){
        Uri address = Uri.parse(data);
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openLinkIntent);
    }
    public void callTelephone(String data){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data));
        startActivity(intent);
    }
    public void goGeo(float dval1, float dval2){

        if(dval1 >= -90 && dval1 <=90 && dval2 >= -180 && dval2 < 180){
            //go to google maps
            //check Paris: 48.8566 2.3522
            String geoString = String.format("geo:%f,%f?z=8", dval1, dval2);
            Uri geoUri = Uri.parse(geoString);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
            startActivity(mapIntent);
        }
        else et.setError("Latitude is specified in degrees within the range [-90, 90].\n Longitude is specified in degrees within the range [-180, 180)");

    }
    public void onClick(View view) {
        String data = et.getText().toString().trim();
         if(rb_url.isChecked()){
             if (URLUtil.isValidUrl(data)){
                 //navigate
                 goURL(data);
             }
             else {
                 et.setError("Not valid URL");
             }

         } else if (rb_geo.isChecked()) {
             String[] splitted = data.trim().split(" ", 2);
             try {
                 dval1 = Float.parseFloat(splitted[0]);
                 dval2 = Float.parseFloat(splitted[1]);
             } catch(NumberFormatException e) {
                 et.setError("You need to type two numbers!, separated by space");
                 return;
             }
             goGeo(dval1, dval2);

         }else if (rb_telephone.isChecked()) {
             if(PhoneNumberUtils.isGlobalPhoneNumber(data)){
                 //call number
                 callTelephone(data);
             }
             else {
                 et.setError("Not valid telephone number");

             }

         }
         else{
                //call function
             if(URLUtil.isValidUrl(data)){
                 //call number
                 goURL(data);
             }
             else if(data.matches(".*[a-z].*")) {
                 et.setError("Invalid data. Type Telephone number either url or geodata");
             }
             else if(PhoneNumberUtils.isGlobalPhoneNumber(data)) {
                 callTelephone(data);
             }
             else {
                 String[] splitted = data.trim().split(" ", 2);
                 try {
                     dval1 = Float.parseFloat(splitted[0]);
                     dval2 = Float.parseFloat(splitted[1]);
                 } catch(NumberFormatException e) {
                     et.setError("You need to type two numbers!, separated by space");
                     return;
                 }
                 goGeo(dval1, dval2);
             }


         }

    }


    public void onClickURL(View view) {
            et.setHint("Enter URL");
    }

    public void onClickGEO(View view) {
            et.setHint("e.g: \'-57 148\' ");
    }

    public void onClickTelephone(View view) {
            et.setHint("e.g: \'+7(XXX) XXX XX-XX\'");
        }

    public void onClickReset(View view) {
        rg.clearCheck();
        et.setHint("[URL|telephoneNumber|geodata]");
    }
}