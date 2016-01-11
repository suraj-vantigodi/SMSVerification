package com.example.sruaj.retrofittest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    public static final String MyPREFERENCES = "UttaraPreferences";
//    //SharedPreferences sharedpreferences;
//    SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("UttaraPreferences", 0);


    SharedPreferences app_preferences;

    //Declaring views
    private TextView responseText;
    private EditText editTextName;
    private EditText editTextMobile;
    private EditText editTextEmail;
    private EditText OTPText;

    private Button buttonRegister,RegisterUser;

    String outString,nameString,emailString,mobileString,statusString;
    int status=100;

    //This is our root url
    public static final String ROOT_URL = "http://166.62.10.28/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);


        //Initializing Views
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        OTPText =(EditText)findViewById(R.id.OTPText);
        responseText = (TextView)findViewById(R.id.serverResponse);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        RegisterUser = (Button)findViewById(R.id.RegisterUser);

        //Adding listener to button
        buttonRegister.setOnClickListener(this);

        RegisterUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                checkOTP();
            }
        });

    }


    private void insertUser(){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RegisterAPI api = adapter.create(RegisterAPI.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextMobile.getText().toString(),

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast

                        Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    private void checkOTP(){

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RegisterOTP api = adapter.create(RegisterOTP.class);
        api.checkOTP(

                OTPText.getText().toString(),
                new Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {

                        BufferedReader reader = null;
                        String output = "";
                        StringBuilder sb = new StringBuilder();

                        try {
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            String line;
                            try {
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            output = reader.readLine();
                            outString = sb.toString();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try{

                            JSONObject responseObj = new JSONObject(outString);
                            boolean error = responseObj.getBoolean("error");
                            String message = responseObj.getString("message");

                            if (!error) {
                                // parsing the user profile information
                                JSONObject profileObj = responseObj.getJSONObject("profile");

                                nameString = profileObj.getString("name");
                                emailString = profileObj.getString("email");
                                mobileString = profileObj.getString("mobile");
                                statusString = profileObj.getString("status");
                            }

                        }catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        responseText.setText(nameString+" "+emailString+" "+mobileString+" "+statusString);
                        Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }


        );

    }

    //Overriding onclick method
    @Override
    public void onClick(View v) {
        //Calling insertUser on button click
        insertUser();
    }
}