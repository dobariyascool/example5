package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unchecked")
public class CategoryItemFragment extends Fragment implements View.OnClickListener {

    public static boolean isViewChange = false;
    ViewPager itemViewPager;
    TabLayout itemTabLayout;
    ItemPagerAdapter itemPagerAdapter;
    FloatingActionMenu famRoot;
    StringBuilder sb = new StringBuilder();
    boolean isForceToChange=false;

    public CategoryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_item, container, false);

        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //floating action button
        FloatingActionButton fabVeg = (FloatingActionButton) view.findViewById(R.id.fabVeg);
        FloatingActionButton fabNonVeg = (FloatingActionButton) view.findViewById(R.id.fabNonVeg);
        FloatingActionButton fabJain = (FloatingActionButton) view.findViewById(R.id.fabJain);
        //end

        //tab layout
        itemTabLayout = (TabLayout) view.findViewById(R.id.itemTabLayout);
        //end

        //view page
        itemViewPager = (ViewPager) view.findViewById(R.id.itemViewPager);
        //end

        //imagebutton
        ImageButton ibViewChange = (ImageButton) view.findViewById(R.id.ibViewChange);
        //end

        //event
        fabVeg.setOnClickListener(this);
        fabNonVeg.setOnClickListener(this);
        fabJain.setOnClickListener(this);
        ibViewChange.setOnClickListener(this);
        //end

        new GuestHomeCategoryLodingTask().execute();
        return view;
    }

    @Override
    public void onClick(View v) {
        ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
        if (v.getId() == R.id.fabVeg) {
            itemTabFragment.ItemDataFilter(String.valueOf(Globals.ItemType.Veg.getValue()));
            famRoot.close(true);
        } else if (v.getId() == R.id.fabNonVeg) {
            itemTabFragment.ItemDataFilter(String.valueOf(Globals.ItemType.NonVeg.getValue()));
            famRoot.close(true);
        } else if (v.getId() == R.id.fabJain) {
            itemTabFragment.ItemDataFilter(String.valueOf(Globals.ItemType.Jain.getValue()));
            famRoot.close(true);
        } else if (v.getId() == R.id.ibViewChange) {
            if (isViewChange) {
                v.setSelected(false);
                isViewChange = false;
                isForceToChange = true;
            } else {
                v.setSelected(true);
                isViewChange = true;
                isForceToChange = true;
            }
            itemTabFragment.SetupRecyclerView();
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

                itemPagerAdapter = new ItemPagerAdapter(getFragmentManager());
                for (int i = 0; i < alCategoryMaster.size(); i++) {
                    itemPagerAdapter.AddFragment(com.arraybit.pos.ItemTabFragment.createInstance((CategoryMaster) alCategoryMaster.get(i)), alCategoryMaster.get(i));
                }

                itemViewPager.setAdapter(itemPagerAdapter);
                itemTabLayout.setupWithViewPager(itemViewPager);

                itemTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
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
