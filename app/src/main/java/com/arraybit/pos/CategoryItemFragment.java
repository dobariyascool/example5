package com.arraybit.pos;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"unchecked", "ConstantConditions"})
public class CategoryItemFragment extends Fragment implements View.OnClickListener {

    public static boolean isViewChange = false;
    public static short i = 0;
    ViewPager itemViewPager;
    TabLayout itemTabLayout;
    ItemPagerAdapter itemPagerAdapter;
    FloatingActionMenu famRoot;
    FloatingActionButton fabVeg, fabNonVeg, fabJain;
    StringBuilder sbItemTypeMasterId = new StringBuilder();
    ImageButton ibViewChange;
    boolean isForceToChange = false;
    short isVegCheck = 0, isNonVegCheck = 0, isJainCheck = 0;
    FrameLayout categoryItemFragment;
    Activity activityName;

    public CategoryItemFragment(Activity activityName) {
        // Required empty public constructor
        this.activityName = activityName;
    }

    public CategoryItemFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_item, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_category_item));
        }
        //end

        categoryItemFragment = (FrameLayout)view.findViewById(R.id.categoryItemFragment);
        Globals.SetScaleImageBackground(getActivity(),null,null,categoryItemFragment);

        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //floating action button
        fabVeg = (FloatingActionButton) view.findViewById(R.id.fabVeg);
        fabNonVeg = (FloatingActionButton) view.findViewById(R.id.fabNonVeg);
        fabJain = (FloatingActionButton) view.findViewById(R.id.fabJain);
        //end

        //tab layout
        itemTabLayout = (TabLayout) view.findViewById(R.id.itemTabLayout);
        itemTabLayout.setClickable(true);
        //end

        //view page
        itemViewPager = (ViewPager) view.findViewById(R.id.itemViewPager);
        //end

        //imagebutton
        ibViewChange = (ImageButton) view.findViewById(R.id.ibViewChange);
        //end

        //event
        fabVeg.setOnClickListener(this);
        fabNonVeg.setOnClickListener(this);
        fabJain.setOnClickListener(this);
        ibViewChange.setOnClickListener(this);
        //end

        ViewChange();

        new GuestHomeCategoryLodingTask().execute();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(),null,null,categoryItemFragment);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
    }

    public void ViewChange(){
        if(activityName!=null) {
            if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                i = 2;
                isViewChange = true;
                isForceToChange = false;
                ibViewChange.setVisibility(View.GONE);
            } else {
                ibViewChange.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            i=0;
            isViewChange = false;
            isForceToChange = false;
            ibViewChange.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
        if (v.getId() == R.id.fabVeg) {
            famRoot.close(true);
            if (isVegCheck == 0) {
                fabVeg.setSelected(true);
                fabVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.transparent_orange));
                isVegCheck += 1;
            } else {
                fabVeg.setSelected(false);
                fabVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_dark));
                isVegCheck = 0;
            }
            CheckSelected();
            if (sbItemTypeMasterId.toString().equals("")) {
                itemTabFragment.ItemDataFilter(null);
            } else {
                itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
            }


        } else if (v.getId() == R.id.fabNonVeg) {
            famRoot.close(true);
            if (isNonVegCheck == 0) {
                fabNonVeg.setSelected(true);
                fabNonVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.transparent_orange));
                isNonVegCheck += 1;
            } else {
                fabNonVeg.setSelected(false);
                fabNonVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_dark));
                isNonVegCheck = 0;
            }
            CheckSelected();
            if (sbItemTypeMasterId.toString().equals("")) {
                itemTabFragment.ItemDataFilter(null);
            } else {
                itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
            }

        } else if (v.getId() == R.id.fabJain) {
            famRoot.close(true);
            if (isJainCheck == 0) {
                fabJain.setSelected(true);
                fabJain.setColorNormal(ContextCompat.getColor(getActivity(), R.color.transparent_orange));
                isJainCheck += 1;
            } else {
                fabJain.setSelected(false);
                fabJain.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_dark));
                isJainCheck = 0;
            }
            CheckSelected();
            if (sbItemTypeMasterId.toString().equals("")) {
                itemTabFragment.ItemDataFilter(null);
            } else {
                itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
            }


        } else if (v.getId() == R.id.ibViewChange) {
            i = (short) (i + 1);
            if (i == 1) {
                v.setSelected(true);
                isViewChange = true;
                isForceToChange = true;
                itemTabFragment.SetupRecyclerView();
            } else if (i == 2) {
                v.setSelected(false);
                v.setActivated(true);
                isViewChange = true;
                isForceToChange = true;
                itemTabFragment.SetupRecyclerView();
            } else {
                i = 0;
                v.setActivated(false);
                isViewChange = false;
                isForceToChange = true;
                itemTabFragment.SetupRecyclerView();
            }
        }
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    void CheckSelected() {
        sbItemTypeMasterId = new StringBuilder();
        if (fabVeg.isSelected()) {
            sbItemTypeMasterId.append(Globals.ItemType.Veg.getValue() + ",");
        }
        if (fabNonVeg.isSelected()) {
            sbItemTypeMasterId.append(Globals.ItemType.NonVeg.getValue() + ",");
        }
        if (fabJain.isSelected()) {
            sbItemTypeMasterId.append(Globals.ItemType.Jain.getValue() + ",");
        }
    }

    //pager adapter
    static class ItemPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> itemFragmentList = new ArrayList<>();
        private final List<CategoryMaster> itemFragmentTitleList = new ArrayList<>();

        public ItemPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, CategoryMaster title) {
            itemFragmentList.add(fragment);
            itemFragmentTitleList.add(title);
        }

        public Fragment GetCurrentFragment(int position) {
            return itemFragmentList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return itemFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return itemFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return itemFragmentTitleList.get(position).getCategoryName();
        }
    }
    //end

    //region Loading Task

    public class GuestHomeCategoryLodingTask extends AsyncTask {
        ArrayList<CategoryMaster> alCategoryMaster;
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
            CategoryJSONParser objCategoryJSONParser = new CategoryJSONParser();
            alCategoryMaster = objCategoryJSONParser.SelectAllCategoryMaster();
            return alCategoryMaster;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (alCategoryMaster == null) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgSelectFail), Toast.LENGTH_LONG).show();
            } else if (alCategoryMaster.size() == 0) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgNoRecord), Toast.LENGTH_LONG).show();
            } else {

                //ibViewChange.setVisibility(View.VISIBLE);
                itemPagerAdapter = new ItemPagerAdapter(getFragmentManager());

                CategoryMaster objCategoryMaster =  new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) 0);
                objCategoryMaster.setCategoryName("All");
                ArrayList<CategoryMaster> alCategory = new ArrayList<>();
                alCategory.add(objCategoryMaster);
                alCategoryMaster.addAll(0,alCategory);

                for (int i = 0; i < alCategoryMaster.size(); i++) {
                    itemPagerAdapter.AddFragment(ItemTabFragment.createInstance(alCategoryMaster.get(i)), alCategoryMaster.get(i));
                }

                itemViewPager.setAdapter(itemPagerAdapter);
                itemTabLayout.setupWithViewPager(itemViewPager);

                itemTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        //set the current view on tab click
                        itemViewPager.setCurrentItem(tab.getPosition());

                        //change next layout
                        ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
                        if (isForceToChange) {
                            itemTabFragment.SetupRecyclerView();
                            isForceToChange = false;
                        } else {
                            isForceToChange = false;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                progressDialog.dismiss();
            }
        }
    }

    //endregion
}
