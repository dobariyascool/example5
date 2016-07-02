package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackAnswerMaster;
import com.arraybit.modal.FeedbackQuestionGroupMaster;
import com.arraybit.modal.FeedbackQuestionMaster;
import com.arraybit.parser.FeedbackAnswerJSONParser;
import com.arraybit.parser.FeedbackQuestionGroupJSONParser;
import com.arraybit.parser.FeedbackQuestionJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"ConstantConditions", "unchecked"})
@SuppressLint("ValidFragment")
public class FeedbackFragment extends Fragment implements View.OnClickListener, GuestLoginDialogFragment.LoginResponseListener {

    public static ArrayList<ArrayList<FeedbackAnswerMaster>> alFinalFeedbackAnswers;
    Activity activityName;
    TextView txtFeedbackGroup, txtPrevious, txtNext;
    ArrayList<FeedbackQuestionGroupMaster> alFeedbackQuestionGroupMaster;
    ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster, alFeedbackQuestionMasterFilter;
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, alFeedbackAnswer;
    ViewPager viewPager;
    ImageView ivNext, ivPrevious;
    FrameLayout feedbackFragment;
    short cnt = 0;
    FeedbackPagerAdapter adapter;

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

        feedbackFragment = (FrameLayout) view.findViewById(R.id.feedbackFragment);

        txtFeedbackGroup = (TextView) view.findViewById(R.id.txtFeedbackGroup);
        txtNext = (TextView) view.findViewById(R.id.txtNext);
        txtPrevious = (TextView) view.findViewById(R.id.txtPrevious);

        ivNext = (ImageView) view.findViewById(R.id.ivNext);
        ivPrevious = (ImageView) view.findViewById(R.id.ivPrevious);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        SetViewPagerElevation();

        txtNext.setOnClickListener(this);
        txtPrevious.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);

        if (Service.CheckNet(getActivity())) {
            new FeedbackQuestionGroupLodingTask().execute();
        } else {
            SetNextPreviousEnable(false);
            Globals.ShowSnackBar(container, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        feedbackFragment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }, 1000);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            menu.findItem(R.id.mWaiting).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            if (Globals.isWishListShow == 0) {
                menu.findItem(R.id.logout).setVisible(false);
                menu.findItem(R.id.notification_layout).setVisible(false);
            } else if (Globals.isWishListShow == 1) {
                menu.findItem(R.id.login).setVisible(false);
                menu.findItem(R.id.logout).setVisible(false);
                menu.findItem(R.id.shortList).setVisible(false);
                menu.findItem(R.id.callWaiter).setVisible(false);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_home))) {
                if (Globals.isWishListShow == 0) {
//                    Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } else {
                getActivity().getSupportFragmentManager().popBackStack();
            }
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
        } else if (v.getId() == R.id.txtNext) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else if (v.getId() == R.id.txtPrevious) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void LoginResponse() {
        FeedbackViewFragment feedbackViewFragment = (FeedbackViewFragment) adapter.GetCurrentFragment(cnt - 1);
        feedbackViewFragment.SetSimpleFeedbackLayout();
    }


    //region Private Methods and Interface

    private void SetSelectedQuestion(int linktoFeedbackQuestionGroupMasterId) {
        alFeedbackQuestionMasterFilter = new ArrayList<>();
        for (int i = 0; i < alFeedbackQuestionMaster.size(); i++) {
            if (alFeedbackQuestionMaster.get(i).getLinktoFeedbackQuestionGroupMasterId() == linktoFeedbackQuestionGroupMasterId) {
                alFeedbackQuestionMasterFilter.add(alFeedbackQuestionMaster.get(i));
            }
        }
    }

    private void SetNextPreviousEnable(boolean isEnable) {
        if (isEnable) {
            ivNext.setEnabled(true);
            txtNext.setEnabled(true);
            ivPrevious.setEnabled(true);
            txtPrevious.setEnabled(true);
        } else {
            ivNext.setEnabled(false);
            txtNext.setEnabled(false);
            ivPrevious.setEnabled(false);
            txtPrevious.setEnabled(false);
        }
    }

    private void SetViewPagerElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            viewPager.setPadding(0, 0, 0, 8);
            viewPager.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.offwhite));
            viewPager.setElevation(8f);
            viewPager.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        } else {
            viewPager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bottom_border));
            viewPager.setPadding(0, 0, 0, 16);
        }
    }

    private void SetVisibility(int position, FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster) {
        if (objFeedbackQuestionGroupMaster.getGroupName().equals(Globals.FeedbackQuestionType.Null_Group.toString())) {
            txtFeedbackGroup.setText(getActivity().getResources().getString(R.string.fbFeedbackGroup));
        } else if (objFeedbackQuestionGroupMaster.getGroupName().equals(Globals.FeedbackQuestionType.Simple_Feedback.toString())) {
            txtFeedbackGroup.setText(getActivity().getResources().getString(R.string.fbFeedbackGroup));
        } else {
            txtFeedbackGroup.setText(objFeedbackQuestionGroupMaster.getGroupName());
        }
        if (position == 0) {
            ivNext.setEnabled(true);
            txtNext.setEnabled(true);
            ivPrevious.setEnabled(false);
            txtPrevious.setEnabled(false);
            txtPrevious.setVisibility(View.INVISIBLE);
            ivPrevious.setVisibility(View.INVISIBLE);
            ivNext.setVisibility(View.VISIBLE);
            txtNext.setVisibility(View.VISIBLE);
        } else if (position == viewPager.getOffscreenPageLimit() - 1) {
            ivNext.setEnabled(false);
            txtNext.setEnabled(false);
            ivPrevious.setEnabled(true);
            txtPrevious.setEnabled(true);
            ivNext.setVisibility(View.INVISIBLE);
            txtNext.setVisibility(View.INVISIBLE);
            ivPrevious.setVisibility(View.VISIBLE);
            txtPrevious.setVisibility(View.VISIBLE);
        } else {
            ivNext.setEnabled(true);
            txtNext.setEnabled(true);
            ivPrevious.setEnabled(true);
            txtPrevious.setEnabled(true);
            ivNext.setVisibility(View.VISIBLE);
            txtNext.setVisibility(View.VISIBLE);
            ivPrevious.setVisibility(View.VISIBLE);
            txtPrevious.setVisibility(View.VISIBLE);
        }
    }

    public void RemoveFragment() {
        cnt = 0;
        for (int i = 0; i < viewPager.getOffscreenPageLimit(); i++) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            FeedbackViewFragment CurrentFeedbackViewFragment = (FeedbackViewFragment) adapter.GetCurrentFragment(i);
            fragmentTransaction.remove(CurrentFeedbackViewFragment);
            fragmentTransaction.commit();
        }
    }

    public void ReplaceFragment(Fragment fragment, String fragmentName) {
        if (Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            RemoveFragment();
            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            RemoveFragment();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        }
    }
    //endregion

    //region PagerAdapter
    static class FeedbackPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> feedbackFragmentList = new ArrayList<>();
        private final List<FeedbackQuestionGroupMaster> feedbackFragmentTitle = new ArrayList<>();

        public FeedbackPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster) {
            feedbackFragmentList.add(fragment);
            feedbackFragmentTitle.add(objFeedbackQuestionGroupMaster);
        }

        public Fragment GetCurrentFragment(int position) {
            return feedbackFragmentList.get(position);
        }


        public FeedbackQuestionGroupMaster GetFeedbackQuestionGroup(int position) {
            return feedbackFragmentTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return feedbackFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return feedbackFragmentList.size();
        }

    }
    //endregion

    //region LoadingTask

    class FeedbackQuestionGroupLodingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] params) {
            FeedbackQuestionGroupJSONParser objFeedbackQuestionGroupJSONParser = new FeedbackQuestionGroupJSONParser();
            return objFeedbackQuestionGroupJSONParser.SelectAllFeedbackQuestionGroupMaster(Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            ArrayList<FeedbackQuestionGroupMaster> lstFeedbackQuestionGroupMaster = (ArrayList<FeedbackQuestionGroupMaster>) result;
            if (lstFeedbackQuestionGroupMaster == null) {
                SetNextPreviousEnable(false);
                Globals.ShowSnackBar(feedbackFragment, getResources().getString(R.string.MsgSelectFail), getActivity(), 1000);
            } else if (lstFeedbackQuestionGroupMaster.size() == 0) {
                SetNextPreviousEnable(false);
                Globals.ShowSnackBar(feedbackFragment, getResources().getString(R.string.MsgNoRecord), getActivity(), 1000);
            } else {
                alFeedbackQuestionGroupMaster = lstFeedbackQuestionGroupMaster;
                if (lstFeedbackQuestionGroupMaster.get(0).getTotalNullGroupFeedbackQuestion() == 0) {
                    FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster = new FeedbackQuestionGroupMaster();
                    objFeedbackQuestionGroupMaster.setFeedbackQuestionGroupMasterId((short) 0);
                    objFeedbackQuestionGroupMaster.setGroupName(Globals.FeedbackQuestionType.Simple_Feedback.toString());
                    alFeedbackQuestionGroupMaster.add(lstFeedbackQuestionGroupMaster.size(), objFeedbackQuestionGroupMaster);
                } else {
                    FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster = new FeedbackQuestionGroupMaster();
                    objFeedbackQuestionGroupMaster.setFeedbackQuestionGroupMasterId((short) 0);
                    objFeedbackQuestionGroupMaster.setGroupName(Globals.FeedbackQuestionType.Null_Group.toString());
                    alFeedbackQuestionGroupMaster.add(lstFeedbackQuestionGroupMaster.size(), objFeedbackQuestionGroupMaster);
                    objFeedbackQuestionGroupMaster = new FeedbackQuestionGroupMaster();
                    objFeedbackQuestionGroupMaster.setFeedbackQuestionGroupMasterId((short) 0);
                    objFeedbackQuestionGroupMaster.setGroupName(Globals.FeedbackQuestionType.Simple_Feedback.toString());
                    alFeedbackQuestionGroupMaster.add(objFeedbackQuestionGroupMaster);
                }
                new FeedbackQuestionLodingTask().execute();
            }
        }
    }

    class FeedbackQuestionLodingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            FeedbackQuestionJSONParser objFeedbackQuestionJSONParser = new FeedbackQuestionJSONParser();
            return objFeedbackQuestionJSONParser.SelectAllFeedbackQuestionMaster(String.valueOf(Globals.businessMasterId));
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            ArrayList<FeedbackQuestionMaster> lstQuestionMasters = (ArrayList<FeedbackQuestionMaster>) result;
            if (lstQuestionMasters == null) {
                Globals.ShowSnackBar(feedbackFragment, getResources().getString(R.string.MsgSelectFail), getActivity(), 1000);
            } else if (lstQuestionMasters.size() == 0) {
                Globals.ShowSnackBar(feedbackFragment, getResources().getString(R.string.MsgNoRecord), getActivity(), 1000);
            } else {
                alFeedbackQuestionMaster = lstQuestionMasters;
                new FeedbackAnswerLodingTask().execute();
            }

        }

    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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

                alFeedbackAnswerMaster = lstFeedbackAnswerMaster;

                adapter = new FeedbackPagerAdapter(getActivity().getSupportFragmentManager());

                alFinalFeedbackAnswers = new ArrayList<>();
                for (int i = 0; i < alFeedbackQuestionGroupMaster.size(); i++) {
                    if (alFeedbackQuestionGroupMaster.get(i).getGroupName().equals(Globals.FeedbackQuestionType.Null_Group.toString())) {
                        SetSelectedQuestion(0);
                    } else if (alFeedbackQuestionGroupMaster.get(i).getGroupName().equals(Globals.FeedbackQuestionType.Simple_Feedback.toString())) {
                        FeedbackQuestionMaster objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                        objFeedbackQuestionMaster.setQuestionType((short) Globals.FeedbackQuestionType.Simple_Feedback.getValue());
                        alFeedbackQuestionMasterFilter = new ArrayList<>();
                        alFeedbackQuestionMasterFilter.add(objFeedbackQuestionMaster);
                    } else {
                        SetSelectedQuestion(alFeedbackQuestionGroupMaster.get(i).getFeedbackQuestionGroupMasterId());
                    }
                    if (alFeedbackQuestionMasterFilter.size() > 0) {
                        cnt = (short) (cnt + 1);
                        adapter.AddFragment(FeedbackViewFragment.createInstance(alFeedbackQuestionMasterFilter, alFeedbackAnswerMaster, cnt - 1), alFeedbackQuestionGroupMaster.get(i));
                        if (alFeedbackQuestionMasterFilter.size() == 1) {
                            if (alFeedbackQuestionMasterFilter.get(0).getQuestionType() != Globals.FeedbackQuestionType.Simple_Feedback.getValue()) {
                                ArrayList<FeedbackAnswerMaster> arrayList = new ArrayList<>();
                                alFinalFeedbackAnswers.add(arrayList);
                            }
                        } else {
                            ArrayList<FeedbackAnswerMaster> arrayList = new ArrayList<>();
                            alFinalFeedbackAnswers.add(arrayList);
                        }
                    }
                }

                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(cnt);
                SetVisibility(0, adapter.GetFeedbackQuestionGroup(0));

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
                        SetVisibility(position, adapter.GetFeedbackQuestionGroup(position));
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

