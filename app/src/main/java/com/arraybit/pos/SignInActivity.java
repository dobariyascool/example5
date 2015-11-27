package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.UserMaster;
import com.arraybit.parser.UserMasterJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

@SuppressWarnings("ALL")
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    //public static String ServerName = null;
    EditText etName, etPassword;
    UserMaster objUserMaster = null;
    SharePreferenceManage objSharePreferenceManage;
    ToggleButton tbPasswordShow;
    ImageButton ibClear;
    int i = 0;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        //end

        //edittext
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        //end

        //button
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tbPasswordShow = (ToggleButton) findViewById(R.id.tbPasswordShow);
        ibClear = (ImageButton) findViewById(R.id.ibClear);
        //end

        //event
        btnSignIn.setOnClickListener(this);
        tbPasswordShow.setOnClickListener(this);
        ibClear.setOnClickListener(this);
        //end

//        //get server name
//        objSharePreferenceManage = new SharePreferenceManage();
//        Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SignInActivity.this);
//        Globals.ChangeUrl();
//        //end

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibClear.setVisibility(View.VISIBLE);
                if (etName.getText().toString().equals("")) {
                    ibClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tbPasswordShow.setVisibility(View.VISIBLE);
                if (etPassword.getText().toString().equals("")) {
                    tbPasswordShow.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignIn) {
            if (!ValidateControls()) {
                Toast.makeText(SignInActivity.this, getResources().getString(R.string.MsgValidation), Toast.LENGTH_LONG).show();
                return;
            }
            if (Service.CheckNet(SignInActivity.this)) {
                new SignInLodingTask().execute();

            } else {
                Toast.makeText(SignInActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.ibClear) {
            etName.setText("");
            ibClear.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.tbPasswordShow) {
            if (tbPasswordShow.isChecked()) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
    }

    boolean ValidateControls() {
        boolean IsValid = true;

        if (etName.getText().toString().equals("") && etPassword.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.siUserName));
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            IsValid = false;
        } else if (etName.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.siUserName));
            etPassword.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("") && !etName.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            etName.clearError();
            IsValid = false;
        } else {
            etName.clearError();
            etPassword.clearError();
        }

        return IsValid;
    }

    void ClearControls() {
        etName.setText("");
        etPassword.setText("");
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
            return true;
        } else if (id == R.id.changeMode) {

            if (i < 5) {
                i++;
            } else {
                i = 0;
                Snackbar.make(getCurrentFocus(), "Change Settings", Snackbar.LENGTH_LONG)
                        .setAction("Done", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                                if (objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SignInActivity.this) != null) {
                                    objSharePreferenceManage.RemovePreference("ServerPreference", "ServerName", SignInActivity.this);
                                    objSharePreferenceManage.ClearPreference("ServerPreference", SignInActivity.this);
                                    Globals.InitializeFragment(new ServerNameFragment(), getSupportFragmentManager());
                                }

                            }
                        }).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            //getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
    //end

    public void CreateUserPreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objUserMaster.getLinktoUserTypeMasterId() == 1) {

            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", SignInActivity.this) == null) {
                objSharePreferenceManage.CreatePreference("WaiterPreference", "UserName", etName.getText().toString(), SignInActivity.this);
            }

            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", SignInActivity.this) == null) {
                objSharePreferenceManage.CreatePreference("WaiterPreference", "UserMasterId", String.valueOf(objUserMaster.getUserMasterId()), SignInActivity.this);
            }

            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserTypeMasterId", SignInActivity.this) == null) {
                objSharePreferenceManage.CreatePreference("WaiterPreference", "UserTypeMasterId", String.valueOf(objUserMaster.getLinktoUserTypeMasterId()), SignInActivity.this);
            }

        } else {
            if (objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", SignInActivity.this) == null) {
                objSharePreferenceManage.CreatePreference("WaitingPreference", "UserName", etName.getText().toString(), SignInActivity.this);
            }

            if (objSharePreferenceManage.GetPreference("WaitingPreference", "UserMasterId", SignInActivity.this) == null) {
                objSharePreferenceManage.CreatePreference("WaitingPreference", "UserMasterId", String.valueOf(objUserMaster.getUserMasterId()), SignInActivity.this);
            }

            if (objSharePreferenceManage.GetPreference("WaitingPreference", "UserTypeMasterId", SignInActivity.this) == null) {
                objSharePreferenceManage.CreatePreference("WaitingPreference", "UserTypeMasterId", String.valueOf(objUserMaster.getLinktoUserTypeMasterId()), SignInActivity.this);
            }
        }

    }

    public class SignInLodingTask extends AsyncTask {

        ProgressDialog pDialog;
        String strName, strPassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage(getResources().getString(R.string.MsgLoading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            strName = etName.getText().toString();
            strPassword = etPassword.getText().toString();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            UserMasterJSONParser objUserMasterJSONParser = new UserMasterJSONParser();
            objUserMaster = objUserMasterJSONParser.SelectRegisteredUserName(strName, strPassword);
            return objUserMaster;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            pDialog.dismiss();
            if (result == null) {
                Toast.makeText(SignInActivity.this, getResources().getString(R.string.siLoginFailedMsg), Toast.LENGTH_LONG).show();

            } else {

                CreateUserPreference();

                Toast.makeText(SignInActivity.this, getResources().getString(R.string.siLoginSucessMsg), Toast.LENGTH_SHORT).show();
                ClearControls();

                if(SplashScreenActivity.counter > 1 || SplashScreenActivity.counter == 0) {
                    Globals.InitializeFragment(new CounterFragment(), getSupportFragmentManager());
                }
                else
                {
                    Intent intent=new Intent(SignInActivity.this,WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}

