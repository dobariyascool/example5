package com.arraybit.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etName,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //app_bar
        Toolbar app_bar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //end

        //edittext
        etName=(EditText)findViewById(R.id.etName);
        etPassword=(EditText)findViewById(R.id.etPassword);
        //end

        //button
        Button btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnSignIn.setVisibility(View.GONE);
        //end


        //event
        btnSignIn.setOnClickListener(this);
        //end
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSignIn){

            Intent intent = new Intent(SignInActivity.this, WelcomeActivity.class);
            intent.putExtra("username",etName.getText().toString());
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            if(getSupportFragmentManager().getBackStackEntryCount()!=0)
            {
                getSupportFragmentManager().popBackStack();
            }
            else
            {
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if(getSupportFragmentManager().getBackStackEntryCount()!=0)
        {
            getSupportFragmentManager().popBackStack();
        }
    }
    //end
}
