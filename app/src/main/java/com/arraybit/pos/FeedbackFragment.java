package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.FeedbackAnswerMaster;
import com.arraybit.modal.FeedbackQuestionMaster;
import com.arraybit.parser.FeedbackAnswerJSONParser;
import com.arraybit.parser.FeedbackQuestionJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "unchecked"})
@SuppressLint("ValidFragment")
public class FeedbackFragment extends Fragment implements View.OnClickListener {

    EditText etName, etEmail, etMobileNo, etFeedback;
    RadioGroup rgMain;
    RadioButton rbBug, rbSuggestion, rbOther;
    Button btnSubmit;
    Activity activityName;
    int userMasterId;
    SharePreferenceManage objSharePreferenceManage;
    ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster;
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, alFeedbackAnswerFilter, alFeedbackAnswer;
    ViewPager viewPager;
    ImageView ivNext, ivPrevious;
    LinearLayout feedbackFragment;

    public FeedbackFragment(Activity activityName) {
        this.activityName = activityName;
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

        feedbackFragment = (LinearLayout) view.findViewById(R.id.feedbackFragment);

        ivNext = (ImageView) view.findViewById(R.id.ivNext);
        ivPrevious = (ImageView) view.findViewById(R.id.ivPrevious);

        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etMobileNo = (EditText) view.findViewById(R.id.etMobileNo);
        etFeedback = (EditText) view.findViewById(R.id.etFeedback);
        rgMain = (RadioGroup) view.findViewById(R.id.rgMain);
        rbBug = (RadioButton) view.findViewById(R.id.rbBug);
        rbSuggestion = (RadioButton) view.findViewById(R.id.rbSuggestion);
        rbOther = (RadioButton) view.findViewById(R.id.rbOther);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        SetUser();

        if (Service.CheckNet(getActivity())) {
            new FeedbackQuestionLodingTask().execute();

        } else {
            Globals.ShowSnackBar(container, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);

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
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            menu.findItem(R.id.mWaiting).setVisible(false);
        } else if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
        }

        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            Globals.HideKeyBoard(getActivity(), getView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivNext) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else if (v.getId() == R.id.ivPrevious) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    //region Private Methods and Interface
    private void ClearControls() {
        etName.setText("");
        etMobileNo.setText("");
        etEmail.setText("");
        etFeedback.setText("");
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

        if (etEmail.getText().toString().equals("") && !etFeedback.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.fbEmail));
            etFeedback.clearError();
            IsValid = false;
        } else if (!etEmail.getText().toString().equals("") && etFeedback.getText().toString().equals("")) {

            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            etFeedback.setError("Enter " + getResources().getString(R.string.fbFeedback));
            IsValid = false;
        } else if (etEmail.getText().toString().equals("") && etFeedback.getText().toString().equals("")) {
            etFeedback.setError("Enter " + getResources().getString(R.string.fbFeedback));
            etEmail.setError("Enter " + getResources().getString(R.string.fbEmail));
            IsValid = false;
        }
        if (!etMobileNo.getText().toString().equals("") && etMobileNo.getText().length() != 10) {
            etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.fbMobileNo));
            IsValid = false;
        } else {
            etMobileNo.clearError();
        }
        return IsValid;
    }

    private void SetUser() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            if (objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", getActivity()) != null
                    && objSharePreferenceManage.GetPreference("WaitingPreference", "UserMasterId", getActivity()) != null) {
                etName.setText(objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", getActivity()));
                etName.setEnabled(false);
                userMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("WaitingPreference", "UserMasterId", getActivity()));

            }
        } else if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", getActivity()) != null
                    && objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
                etName.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", getActivity()));
                etName.setEnabled(false);
                userMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()));
            }
        }
    }

    private void SetSelectedAnswer(int linktoFeedbackQuestionMasterId) {
        alFeedbackAnswerFilter = new ArrayList<>();
        for (int i = 0; i < alFeedbackAnswerMaster.size(); i++) {
            if (alFeedbackAnswerMaster.get(i).getlinktoFeedbackQuestionMasterId() == linktoFeedbackQuestionMasterId) {
                alFeedbackAnswerFilter.add(alFeedbackAnswerMaster.get(i));
            }
        }
    }

    private void SetVisibility(int position) {
        if (position == 0) {
            ivNext.setVisibility(View.VISIBLE);
            ivPrevious.setVisibility(View.GONE);
        } else if (position == viewPager.getOffscreenPageLimit() - 1) {
            ivNext.setVisibility(View.GONE);
            ivPrevious.setVisibility(View.VISIBLE);
        } else {
            ivNext.setVisibility(View.VISIBLE);
            ivPrevious.setVisibility(View.VISIBLE);
        }
    }
    //endregion

    //region PagerAdapter
    static class TablePagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> tableFragmentList = new ArrayList<>();

        public TablePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment) {
            tableFragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return tableFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return tableFragmentList.size();
        }

    }

    class FeedbackQuestionLodingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            FeedbackQuestionJSONParser objFeedbackQuestionJSONParser = new FeedbackQuestionJSONParser();
            return objFeedbackQuestionJSONParser.SelectAllFeedbackQuestionMaster();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            ArrayList<FeedbackQuestionMaster> lstQuestionMasters = (ArrayList<FeedbackQuestionMaster>) result;
            if (lstQuestionMasters == null) {
                Globals.ShowSnackBar(feedbackFragment, getResources().getString(R.string.MsgSelectFail), getActivity(), 1000);
            } else if (lstQuestionMasters.size() == 0) {
                Globals.ShowSnackBar(feedbackFragment, getResources().getString(R.string.MsgNoRecord), getActivity(), 1000);
            } else {
                alFeedbackQuestionMaster = lstQuestionMasters;
                FeedbackQuestionMaster objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                objFeedbackQuestionMaster.setQuestionType((short) Globals.FeedbackQuestionType.Simple_Feedback.getValue());
                alFeedbackQuestionMaster.add(lstQuestionMasters.size(), objFeedbackQuestionMaster);
                new FeedbackAnswerLodingTask().execute();
            }

        }

    }

    class FeedbackAnswerLodingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alFeedbackAnswer = new ArrayList<>();
            for (int j = 0; j < alFeedbackQuestionMaster.size(); j++) {
                FeedbackAnswerMaster objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                objFeedbackAnswerMaster.setFeedbackAnswerMasterId(0);
                objFeedbackAnswerMaster.setFeedbackQuestion(null);
                objFeedbackAnswerMaster.setAnswer(null);
                objFeedbackAnswerMaster.setlinktoFeedbackQuestionMasterId(0);
                alFeedbackAnswer.add(objFeedbackAnswerMaster);
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            FeedbackAnswerJSONParser objFeedbackAnswerJSONParser = new FeedbackAnswerJSONParser();
            return objFeedbackAnswerJSONParser.SelectAllFeedbackAnswerMaster();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            ArrayList<FeedbackAnswerMaster> lstFeedbackAnswerMaster = (ArrayList<FeedbackAnswerMaster>) result;
            if (lstFeedbackAnswerMaster != null && lstFeedbackAnswerMaster.size() != 0) {
                alFeedbackQuestionMaster.size();
                alFeedbackAnswerMaster = lstFeedbackAnswerMaster;

                TablePagerAdapter adapter = new TablePagerAdapter(getActivity().getSupportFragmentManager());

                for (int i = 0; i < alFeedbackQuestionMaster.size(); i++) {
                    SetSelectedAnswer(alFeedbackQuestionMaster.get(i).getFeedbackQuestionMasterId());
                    adapter.AddFragment(FeedbackViewFragment.createInstance(alFeedbackQuestionMaster.get(i), alFeedbackAnswerFilter, i, alFeedbackAnswer));
                }

                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(alFeedbackQuestionMaster.size());
                SetVisibility(0);
                viewPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Globals.HideKeyBoard(getActivity(), getView());
                    }
                });
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Globals.HideKeyBoard(getActivity(), getView());
                        SetVisibility(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

        }
    }
    //endregion
}

