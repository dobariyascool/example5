package com.arraybit.pos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.FeedbackMaster;
import com.arraybit.parser.FeedbackJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class FeedbackFragment extends Fragment {

    EditText etName, etEmail, etMobileNo, etFeedback;
    RadioGroup rgMain;
    RadioButton rbBug, rbSuggestion, rbOther;
    Button btnSubmit;
    ProgressDialog pDialog;
    String status;

    FeedbackJSONParser objFeedbackJSONParser;
    FeedbackMaster objFeedbackMaster;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_feedback));

        setHasOptionsMenu(true);

        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etMobileNo = (EditText) view.findViewById(R.id.etMobileNo);
        etFeedback = (EditText) view.findViewById(R.id.etFeedback);
        rgMain = (RadioGroup) view.findViewById(R.id.rgMain);
        rbBug = (RadioButton) view.findViewById(R.id.rbBug);
        rbSuggestion = (RadioButton) view.findViewById(R.id.rbSuggestion);
        rbOther = (RadioButton) view.findViewById(R.id.rbOther);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        etName.setText(objSharePreferenceManage.GetPreference("WaitingPreference","UserName",getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Globals.HideKeyBoard(getActivity(), v);

                if (!ValidateControls()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgValidation), Toast.LENGTH_LONG).show();
                    return;
                }
                if (Service.CheckNet(getActivity())) {

                    new FeedbackLodingTask().execute();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    void ClearControls() {
        etName.setText("");
        etMobileNo.setText("");
        etEmail.setText("");
        etFeedback.setText("");
    }

    boolean ValidateControls() {
        boolean IsValid = true;
        if (etEmail.getText().toString().equals("")
                && !etFeedback.getText().toString().equals("")) {
            etMobileNo.setError("");
            etEmail.setError("");
            etFeedback.setError("");
            IsValid = false;
        }
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.fbEmail));
            etFeedback.setError("");
            IsValid = false;
        }
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.fbEmail));
            etFeedback.setError("Enter " + getResources().getString(R.string.fbFeedback));
            IsValid = false;
        }
        if (!etMobileNo.getText().toString().equals("")) {
            if (etMobileNo.getText().length() != 10) {
                etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.afMobileNo));
                IsValid = false;
            }
        }
        return IsValid;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.mWaiting).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            Globals.HideKeyBoard(getActivity(),getView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class FeedbackLodingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.MsgLoading));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            short i = 1;
            objFeedbackMaster = new FeedbackMaster();
            objFeedbackMaster.setName(objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", getActivity()));
            objFeedbackMaster.setEmail(etEmail.getText().toString());
            objFeedbackMaster.setPhone(etMobileNo.getText().toString());
            objFeedbackMaster.setFeedback(etFeedback.getText().toString());

            if (rbBug.isChecked()) {
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short) Globals.FeedbakcType.BugReport.getValue());
            } else if (rbSuggestion.isChecked()) {
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short) Globals.FeedbakcType.Suggestion.getValue());
            } else if (rbOther.isChecked()) {
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short) Globals.FeedbakcType.OtherQuery.getValue());
            }

            objFeedbackMaster.setlinktoRegisteredUserMasterId(Short.valueOf(objSharePreferenceManage.GetPreference("WaitingPreference", "UserMasterId", getActivity())));
            objFeedbackMaster.setlinktoBusinessTypeMasterId(i);
            objFeedbackMaster.setIsDeleted(false);
            objFeedbackJSONParser = new FeedbackJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            status = objFeedbackJSONParser.InsertFeedbackMaster(objFeedbackMaster);
            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (status.equals("-1")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgServerNotResponding), Toast.LENGTH_LONG).show();
            } else if (status.equals("0")) {

                Toast.makeText(getActivity(), getResources().getString(R.string.MsgInsertSuccess), Toast.LENGTH_LONG).show();
                ClearControls();

                Intent intent = new Intent(getActivity(), WaitingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
            pDialog.dismiss();
        }

    }
}

