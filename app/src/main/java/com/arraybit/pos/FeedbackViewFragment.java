package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.FeedbackAnswerMaster;
import com.arraybit.modal.FeedbackMaster;
import com.arraybit.modal.FeedbackQuestionMaster;
import com.arraybit.parser.FeedbackJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class FeedbackViewFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    LinearLayout feedbackViewFragment;
    FeedbackQuestionMaster objFeedbackQuestionMaster;
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, alFeedbackAnswer, alFeedbackAnswerFilter;
    SharePreferenceManage objSharePreferenceManage;
    int userMasterId, currentView;
    StringBuilder sbAnswerId;
    String checkedString;
    FeedbackMaster objFeedbackMaster;
    View view;


    public FeedbackViewFragment() {
        // Required empty public constructor
    }

    public static FeedbackViewFragment createInstance(FeedbackQuestionMaster objFeedbackQuestionMaster, ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, int position, ArrayList<FeedbackAnswerMaster> alFeedbackAnswer) {
        FeedbackViewFragment feedbackViewFragment = new FeedbackViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objFeedbackQuestionMaster);
        bundle.putParcelableArrayList("FeedbackAnswerMaster", alFeedbackAnswerMaster);
        bundle.putParcelableArrayList("FeedbackAnswer", alFeedbackAnswer);
        bundle.putInt("Position", position);
        feedbackViewFragment.setArguments(bundle);
        return feedbackViewFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_view, container, false);

        feedbackViewFragment = (LinearLayout) view.findViewById(R.id.feedbackViewFragment);

        Bundle bundle = getArguments();
        objFeedbackQuestionMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        alFeedbackAnswerMaster = bundle.getParcelableArrayList("FeedbackAnswerMaster");
        currentView = bundle.getInt("Position");
        alFeedbackAnswer = bundle.getParcelableArrayList("FeedbackAnswer");
        SetLayout();

        return view;
    }

    //region Private Methods and Interface
    private void SetLayout() {
        if (Globals.FeedbackQuestionType.Input.getValue() == objFeedbackQuestionMaster.getQuestionType()) {
            SetInputLayout();
        } else if (Globals.FeedbackQuestionType.Single_Select.getValue() == objFeedbackQuestionMaster.getQuestionType()) {
            SetSingleChoiceLayout(alFeedbackAnswerMaster);
        } else if (Globals.FeedbackQuestionType.Multi_Select.getValue() == objFeedbackQuestionMaster.getQuestionType()) {
            SetMultiChoiceLayout(alFeedbackAnswerMaster);
        } else if (Globals.FeedbackQuestionType.Rating.getValue() == objFeedbackQuestionMaster.getQuestionType()) {
            SetRatingLayout();
        } else if (Globals.FeedbackQuestionType.Simple_Feedback.getValue() == objFeedbackQuestionMaster.getQuestionType()) {
            SetSimpleFeedbackLayout();
        }
    }

    private void SetSingleChoiceLayout(final ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams radioGroupLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroup.setLayoutParams(radioGroupLayoutParams);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        final RadioButton[] rbAnswer = new RadioButton[alFeedbackAnswerMaster.size()];

        for (int j = 0; j < alFeedbackAnswerMaster.size(); j++) {
            rbAnswer[j] = new RadioButton(getActivity());
            LinearLayout.LayoutParams rbAnswerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rbAnswer[j].setId(j);
            rbAnswer[j].setTag(alFeedbackAnswerMaster.get(j).getFeedbackAnswerMasterId());
            rbAnswer[j].setLayoutParams(rbAnswerLayoutParams);
            rbAnswer[j].setText(alFeedbackAnswerMaster.get(j).getAnswer());
            rbAnswer[j].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            rbAnswer[j].setGravity(Gravity.CENTER);
            rbAnswer[j].setTextSize(14f);

            radioGroup.addView(rbAnswer[j]);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbAnswer[checkedId].setChecked(true);
                alFeedbackAnswer.get(currentView).setFeedbackAnswerMasterId((int) rbAnswer[checkedId].getTag());
                alFeedbackAnswer.get(currentView).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
            }
        });

        linearLayout.addView(txtQuestion);
        linearLayout.addView(radioGroup);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetMultiChoiceLayout(final ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster) {
        sbAnswerId = new StringBuilder();
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        LinearLayout answerLinearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams answerLinearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        answerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        answerLinearLayout.setGravity(Gravity.CENTER);
        answerLinearLayout.setLayoutParams(answerLinearLayoutParams);

        final CheckBox[] cbAnswer = new CheckBox[alFeedbackAnswerMaster.size()];

        for (int j = 0; j < alFeedbackAnswerMaster.size(); j++) {
            cbAnswer[j] = new CheckBox(getActivity());
            LinearLayout.LayoutParams rbAnswerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cbAnswer[j].setId(j);
            cbAnswer[j].setTag(alFeedbackAnswerMaster.get(j).getFeedbackAnswerMasterId());
            cbAnswer[j].setLayoutParams(rbAnswerLayoutParams);
            cbAnswer[j].setText(alFeedbackAnswerMaster.get(j).getAnswer());
            cbAnswer[j].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            cbAnswer[j].setGravity(Gravity.CENTER);
            cbAnswer[j].setTextSize(14f);

            cbAnswer[j].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sbAnswerId.append(buttonView.getTag()).append(",");
                    } else {
                        if (sbAnswerId.length() > 0) {
                            String[] alString = sbAnswerId.toString().split(",");
                            sbAnswerId = new StringBuilder();
                            for (String str : alString) {
                                if (!str.equals(String.valueOf(buttonView.getTag()))) {
                                    sbAnswerId.append(str).append(",");
                                }
                            }
                        }
                    }
                    alFeedbackAnswer.get(currentView).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
                    alFeedbackAnswer.get(currentView).setFeedbackAnswerIds(sbAnswerId.toString());
                }
            });

            answerLinearLayout.addView(cbAnswer[j]);
        }

        linearLayout.addView(txtQuestion);
        linearLayout.addView(answerLinearLayout);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetInputLayout() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        final EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextLayoutParams);
        editText.applyStyle(R.style.EditText);
        editText.setHint("Answer");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alFeedbackAnswer.get(currentView).setAnswer(editText.getText().toString());
                alFeedbackAnswer.get(currentView).setFeedbackAnswerMasterId(0);
                alFeedbackAnswer.get(currentView).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linearLayout.addView(txtQuestion);
        linearLayout.addView(editText);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetRatingLayout() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        RatingBar ratingBar = new RatingBar(getActivity());
        LinearLayout.LayoutParams ratingBarLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ratingBar.setLayoutParams(ratingBarLayoutParams);
        ratingBar.setNumStars(5);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                alFeedbackAnswer.get(currentView).setAnswer(String.valueOf(ratingBar.getRating()));
                alFeedbackAnswer.get(currentView).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
                alFeedbackAnswer.get(currentView).setFeedbackAnswerMasterId(0);
            }
        });

        linearLayout.addView(txtQuestion);
        linearLayout.addView(ratingBar);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetSimpleFeedbackLayout() {
        ScrollView scrollview = new ScrollView(getActivity());
        LinearLayout.LayoutParams scLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollview.setLayoutParams(scLayoutParams);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams radioGroupLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroup.setLayoutParams(radioGroupLayoutParams);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        final RadioButton rbSuggestion = new RadioButton(getActivity());
        LinearLayout.LayoutParams rbSuggestionLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rbSuggestion.setLayoutParams(rbSuggestionLayoutParams);
        rbSuggestion.setText(Globals.FeedbackType.Suggestion.toString());
        rbSuggestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
        rbSuggestion.setChecked(true);
        checkedString = rbSuggestion.getText().toString();

        final RadioButton rbBugReport = new RadioButton(getActivity());
        LinearLayout.LayoutParams rbBugReportLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rbBugReport.setLayoutParams(rbBugReportLayoutParams);
        rbBugReport.setText(Globals.FeedbackType.BugReport.toString());
        rbBugReport.setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));

        final RadioButton rbOtherQuery = new RadioButton(getActivity());
        LinearLayout.LayoutParams rbOtherQueryLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rbOtherQuery.setLayoutParams(rbOtherQueryLayoutParams);
        rbOtherQuery.setText(Globals.FeedbackType.OtherQuery.toString());
        rbOtherQuery.setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));

        radioGroup.addView(rbSuggestion);
        radioGroup.addView(rbBugReport);
        radioGroup.addView(rbOtherQuery);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbBugReport.isChecked()) {
                    rbSuggestion.setChecked(false);
                    rbOtherQuery.setChecked(false);
                    checkedString = (String) rbBugReport.getText();
                } else if (rbSuggestion.isChecked()) {
                    rbBugReport.setChecked(false);
                    rbOtherQuery.setChecked(false);
                    checkedString = (String) rbSuggestion.getText();
                } else if (rbOtherQuery.isChecked()) {
                    rbBugReport.setChecked(false);
                    rbSuggestion.setChecked(false);
                    checkedString = (String) rbOtherQuery.getText();
                }
            }
        });

        final EditText etUserName = new EditText(getActivity());
        LinearLayout.LayoutParams etUserNameLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etUserName.setLayoutParams(etUserNameLayoutParams);
        etUserName.applyStyle(R.style.EditText);
        etUserName.setHint(getActivity().getResources().getString(R.string.fbName));
        etUserName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final EditText etEmail = new EditText(getActivity());
        LinearLayout.LayoutParams etEmailLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etEmail.setLayoutParams(etEmailLayoutParams);
        etEmail.applyStyle(R.style.EditText);
        etEmail.setHint(getActivity().getResources().getString(R.string.fbEmail));
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        final EditText etMobileNo = new EditText(getActivity());
        LinearLayout.LayoutParams etMobileNoLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etMobileNo.setLayoutParams(etMobileNoLayoutParams);
        etMobileNo.applyStyle(R.style.EditText);
        etMobileNo.setHint(getActivity().getResources().getString(R.string.fbMobileNo));
        etMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);

        final EditText etFeedback = new EditText(getActivity());
        LinearLayout.LayoutParams etFeedbackLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etFeedback.setLayoutParams(etFeedbackLayoutParams);
        etFeedback.applyStyle(R.style.EditText);
        etFeedback.setHint(getActivity().getResources().getString(R.string.fbFeedback));
        etFeedback.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        SetUser(etEmail, etUserName);
        Button btnSubmit = new Button(getActivity());
        LinearLayout.LayoutParams btnSubmitLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnSubmit.setLayoutParams(btnSubmitLayoutParams);
        btnSubmit.applyStyle(R.style.Button);
        btnSubmit.setText(getActivity().getResources().getString(R.string.fbSubmit));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(getActivity(), v);
                view = v;
                if (!ValidateControls(etEmail, etFeedback, etMobileNo)) {
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                    return;
                }
                if (Service.CheckNet(getActivity())) {

                    objFeedbackMaster = new FeedbackMaster();
                    objFeedbackMaster.setName(etUserName.getText().toString());
                    objFeedbackMaster.setEmail(etEmail.getText().toString());
                    objFeedbackMaster.setPhone(etMobileNo.getText().toString());
                    objFeedbackMaster.setFeedback(etFeedback.getText().toString());
                    objFeedbackMaster.setlinktoFeedbackTypeMasterId((short) Globals.FeedbackType.valueOf(checkedString).getValue());
                    objFeedbackMaster.setlinktoBusinessMasterId(Globals.businessMasterId);
                    if (userMasterId != 0) {
                        objFeedbackMaster.setlinktoRegisteredUserMasterId(userMasterId);
                    }

                    new FeedbackLodingTask().execute();
                } else {
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        });

        linearLayout.addView(radioGroup);
        linearLayout.addView(etUserName);
        linearLayout.addView(etEmail);
        linearLayout.addView(etMobileNo);
        linearLayout.addView(etFeedback);
        linearLayout.addView(btnSubmit);
        scrollview.addView(linearLayout);
        feedbackViewFragment.addView(scrollview);
    }

    private void SetUser(EditText etEmail, EditText etName) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null
                    && objSharePreferenceManage.GetPreference("RegistrationPreference", "RegisteredUserMasterId", getActivity()) != null) {
                userMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("RegistrationPreference", "RegisteredUserMasterId", getActivity()));
                etEmail.setText(objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()));
                etEmail.setEnabled(false);
                if (objSharePreferenceManage.GetPreference("RegistrationPreference", "FirstName", getActivity()) != null) {
                    etName.setText(objSharePreferenceManage.GetPreference("RegistrationPreference", "FirstName", getActivity()));
                    etName.setEnabled(false);
                } else {
                    etName.setEnabled(true);
                }
            } else {
                userMasterId = 0;
                etEmail.setEnabled(true);
            }
        }
    }

    private boolean ValidateControls(EditText etEmail, EditText etFeedback, EditText etMobileNo) {
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
    //endregion

    //region LoadingTask
    class FeedbackLodingTask extends AsyncTask {

        ProgressDialog progressDialog;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            alFeedbackAnswerFilter = new ArrayList<>();
            for (int i = 0; i < alFeedbackAnswer.size(); i++) {
                if (alFeedbackAnswer.get(i).getlinktoFeedbackQuestionMasterId() != 0) {
                    if (alFeedbackAnswer.get(i).getFeedbackAnswerIds() != null && !alFeedbackAnswer.get(i).getFeedbackAnswerIds().equals("")) {
                        String[] strAnswer = alFeedbackAnswer.get(i).getFeedbackAnswerIds().split(",");
                        for (String Str : strAnswer) {
                            FeedbackAnswerMaster objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                            objFeedbackAnswerMaster.setlinktoFeedbackQuestionMasterId(alFeedbackAnswer.get(i).getlinktoFeedbackQuestionMasterId());
                            objFeedbackAnswerMaster.setFeedbackAnswerMasterId(Integer.valueOf(Str));
                            alFeedbackAnswerFilter.add(objFeedbackAnswerMaster);
                        }
                    } else {
                        alFeedbackAnswerFilter.add(alFeedbackAnswer.get(i));
                    }
                }
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            FeedbackJSONParser objFeedbackJSONParser = new FeedbackJSONParser();
            status = objFeedbackJSONParser.InsertFeedbackMaster(objFeedbackMaster, alFeedbackAnswerFilter);
            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (status.equals("-1")) {
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (status.equals("0")) {
                if (MenuActivity.parentActivity) {
                    Globals.userName = null;
                    Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                    intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("GuestScreen", true);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
            progressDialog.dismiss();
        }

    }
    //endregion

}
