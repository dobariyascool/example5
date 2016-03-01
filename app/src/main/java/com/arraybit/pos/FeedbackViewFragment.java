package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "UnnecessaryReturnStatement"})
@SuppressLint("SetTextI18n")
public class FeedbackViewFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    LinearLayout feedbackViewFragment, feedbackLayout;
    ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster;
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, alFeedbackAnswer, alFeedbackAnswerFilter;
    SharePreferenceManage objSharePreferenceManage;
    int userMasterId, currentView;
    StringBuilder sbAnswerId;
    String checkedString;
    FeedbackMaster objFeedbackMaster;
    View focusView;
    LinearLayoutManager linearLayoutManager;

    public FeedbackViewFragment() {
        // Required empty public constructor
    }

    public static FeedbackViewFragment createInstance(ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster, ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, int position) {
        FeedbackViewFragment feedbackViewFragment = new FeedbackViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEMS_COUNT_KEY, alFeedbackQuestionMaster);
        bundle.putParcelableArrayList("FeedbackAnswerMaster", alFeedbackAnswerMaster);
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
        feedbackLayout = (LinearLayout) view.findViewById(R.id.feedbackLayout);

        Bundle bundle = getArguments();
        alFeedbackQuestionMaster = bundle.getParcelableArrayList(ITEMS_COUNT_KEY);
        alFeedbackAnswerMaster = bundle.getParcelableArrayList("FeedbackAnswerMaster");
        currentView = bundle.getInt("Position");

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        SetLayout();

        if (currentView != FeedbackFragment.alFinalFeedbackAnswers.size()) {
            FeedbackFragment.alFinalFeedbackAnswers.get(currentView).addAll(alFeedbackAnswer);
        }

        return view;
    }

    //region Private Methods and Interface
    private void SetSelectedAnswer(int linktoFeedbackQuestionMasterId) {
        alFeedbackAnswerFilter = new ArrayList<>();
        for (int i = 0; i < alFeedbackAnswerMaster.size(); i++) {
            if (alFeedbackAnswerMaster.get(i).getlinktoFeedbackQuestionMasterId() == linktoFeedbackQuestionMasterId) {
                alFeedbackAnswerFilter.add(alFeedbackAnswerMaster.get(i));
            }
        }
    }

    private void SetLayout() {
        CreateAnswerList();
        for (int i = 0; i < alFeedbackQuestionMaster.size(); i++) {
            if (Globals.FeedbackQuestionType.Input.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetInputLayout(alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.FeedbackQuestionType.Single_Select.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetSelectedAnswer(alFeedbackQuestionMaster.get(i).getFeedbackQuestionMasterId());
                SetSingleChoiceLayout(alFeedbackAnswerMaster, alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.FeedbackQuestionType.Multi_Select.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetSelectedAnswer(alFeedbackQuestionMaster.get(i).getFeedbackQuestionMasterId());
                SetMultiChoiceLayout(alFeedbackAnswerMaster, alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.FeedbackQuestionType.Rating.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetRatingLayout(alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.FeedbackQuestionType.Simple_Feedback.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetSimpleFeedbackLayout();
            }
        }
    }

    private void SetSingleChoiceLayout(final ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setPadding(16, 4, 16, 4);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumberLayoutParams.gravity = Gravity.TOP;
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.weight = 1f;
        txtQuestionLayoutParams.gravity = Gravity.TOP;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        LinearLayout childLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(childLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txt = new TextView(getActivity());
        LinearLayout.LayoutParams txtLayoutParams = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(txtLayoutParams);
        txt.setVisibility(View.INVISIBLE);

        RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams radioGroupLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroupLayoutParams.weight = 1f;
        radioGroup.setLayoutParams(radioGroupLayoutParams);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        if (Build.VERSION.SDK_INT >= 21) {
            radioGroup.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
        }

        final RadioButton[] rbAnswer = new RadioButton[alFeedbackAnswerMaster.size()];

        for (int j = 0; j < alFeedbackAnswerFilter.size(); j++) {
            rbAnswer[j] = new RadioButton(getActivity());
            LinearLayout.LayoutParams rbAnswerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rbAnswer[j].setId(j);
            rbAnswer[j].setTag(alFeedbackAnswerFilter.get(j).getFeedbackAnswerMasterId());
            rbAnswer[j].setLayoutParams(rbAnswerLayoutParams);
            rbAnswer[j].setText(alFeedbackAnswerFilter.get(j).getAnswer());
            rbAnswer[j].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            rbAnswer[j].setGravity(Gravity.START | Gravity.CENTER);
            rbAnswer[j].setTextSize(14f);
            radioGroup.addView(rbAnswer[j]);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Globals.HideKeyBoard(getActivity(), group);
                rbAnswer[checkedId].setChecked(true);
                alFeedbackAnswer.get(position).setFeedbackAnswerMasterId((int) rbAnswer[checkedId].getTag());
                alFeedbackAnswer.get(position).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
            }
        });

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        childLayout.addView(txt);
        childLayout.addView(radioGroup);
        linearLayout.addView(headerLayout);
        linearLayout.addView(childLayout);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetMultiChoiceLayout(final ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        sbAnswerId = new StringBuilder();
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setPadding(16, 4, 16, 4);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumberLayoutParams.gravity = Gravity.TOP;
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.gravity = Gravity.TOP;
        txtQuestionLayoutParams.weight = 1f;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        LinearLayout childLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(childLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txt = new TextView(getActivity());
        LinearLayout.LayoutParams txtLayoutParams = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(txtLayoutParams);
        txt.setVisibility(View.INVISIBLE);

        LinearLayout answerLinearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams answerLinearLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        answerLinearLayoutParams.weight = 1f;
        answerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        answerLinearLayout.setGravity(Gravity.START | Gravity.CENTER);
        answerLinearLayout.setLayoutParams(answerLinearLayoutParams);

        final CheckBox[] cbAnswer = new CheckBox[alFeedbackAnswerMaster.size()];

        for (int j = 0; j < alFeedbackAnswerFilter.size(); j++) {
            cbAnswer[j] = new CheckBox(getActivity());
            LinearLayout.LayoutParams rbAnswerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cbAnswer[j].setId(j);
            cbAnswer[j].setTag(alFeedbackAnswerFilter.get(j).getFeedbackAnswerMasterId());
            cbAnswer[j].setLayoutParams(rbAnswerLayoutParams);
            cbAnswer[j].setText(alFeedbackAnswerFilter.get(j).getAnswer());
            cbAnswer[j].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            cbAnswer[j].setGravity(Gravity.START | Gravity.CENTER);
            cbAnswer[j].setTextSize(14f);
            cbAnswer[j].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                    Globals.HideKeyBoard(getActivity(), buttonView);
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
                    alFeedbackAnswer.get(position).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
                    alFeedbackAnswer.get(position).setFeedbackAnswerIds(sbAnswerId.toString());
                }
            });

            answerLinearLayout.addView(cbAnswer[j]);
        }

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        childLayout.addView(txt);
        childLayout.addView(answerLinearLayout);
        linearLayout.addView(headerLayout);
        linearLayout.addView(childLayout);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetInputLayout(final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(4, 4, 4, 4);
        linearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.divider));


        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.weight = 1f;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtQuestion.setTextSize(18f);

        final EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextLayoutParams);
        editText.setPadding(30, 0, 0, 0);
        editText.applyStyle(R.style.EditText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alFeedbackAnswer.get(position).setAnswer(editText.getText().toString());
                alFeedbackAnswer.get(position).setFeedbackAnswerMasterId(0);
                alFeedbackAnswer.get(position).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        linearLayout.addView(headerLayout);
        linearLayout.addView(editText);
        feedbackViewFragment.addView(linearLayout);
    }

    private void SetRatingLayout(final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(4, 4, 4, 4);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.brown));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.weight = 1f;
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
                Globals.HideKeyBoard(getActivity(), ratingBar);
                alFeedbackAnswer.get(position).setAnswer(String.valueOf(ratingBar.getRating()));
                alFeedbackAnswer.get(position).setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
                alFeedbackAnswer.get(position).setFeedbackAnswerMasterId(0);
            }
        });

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        linearLayout.addView(headerLayout);
        linearLayout.addView(ratingBar);
        feedbackViewFragment.addView(linearLayout);
    }

    private void CreateAnswerList() {
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

    public void SetSimpleFeedbackLayout() {
        feedbackViewFragment.removeAllViews();
        ScrollView scrollview = new ScrollView(getActivity());
        LinearLayout.LayoutParams scLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scLayoutParams.setMargins(16, 64, 16, 64);
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
                focusView = v;
                if (!ValidateControls(etEmail, etFeedback, etMobileNo)) {
                    Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                } else {
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
                        Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
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
        if (Globals.userName != null) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_home))) {
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
        } else {
            etEmail.setText("");
            etName.setText("");
            etName.setEnabled(true);
            etEmail.setEnabled(true);
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
        ArrayList<FeedbackAnswerMaster> alFinalAnswer, lstFeedbackAnswerMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            alFinalAnswer = new ArrayList<>();
            for (int i = 0; i < FeedbackFragment.alFinalFeedbackAnswers.size(); i++) {
                lstFeedbackAnswerMaster = FeedbackFragment.alFinalFeedbackAnswers.get(i);
                for (int j = 0; j < lstFeedbackAnswerMaster.size(); j++) {
                    FeedbackAnswerMaster objFeedbackAnswerMaster = lstFeedbackAnswerMaster.get(j);
                    if (objFeedbackAnswerMaster.getlinktoFeedbackQuestionMasterId() != 0) {
                        if (objFeedbackAnswerMaster.getFeedbackAnswerIds() != null && !objFeedbackAnswerMaster.getFeedbackAnswerIds().equals("")) {
                            String[] strAnswer = objFeedbackAnswerMaster.getFeedbackAnswerIds().split(",");
                            for (String Str : strAnswer) {
                                FeedbackAnswerMaster objFeedbackAnswer = new FeedbackAnswerMaster();
                                objFeedbackAnswer.setlinktoFeedbackQuestionMasterId(objFeedbackAnswerMaster.getlinktoFeedbackQuestionMasterId());
                                objFeedbackAnswer.setFeedbackAnswerMasterId(Integer.valueOf(Str));
                                alFinalAnswer.add(objFeedbackAnswer);
                            }
                        } else {
                            alFinalAnswer.add(objFeedbackAnswerMaster);
                        }
                    }
                }
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            FeedbackJSONParser objFeedbackJSONParser = new FeedbackJSONParser();
            status = objFeedbackJSONParser.InsertFeedbackMaster(objFeedbackMaster, alFinalAnswer);
            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (status.equals("-1")) {
                Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
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
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

        }

    }
    //endregion

}
