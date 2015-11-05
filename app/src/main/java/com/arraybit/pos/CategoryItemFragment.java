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

import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unchecked")
public class CategoryItemFragment extends Fragment {

    ViewPager itemViewPager;
    TabLayout itemTabLayout;
    ItemPagerAdapter itemPagerAdapter;

    public CategoryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_item, container, false);

        //floating action menu
        FloatingActionMenu famRoot = (FloatingActionMenu)view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //tab layout
        itemTabLayout = (TabLayout)view.findViewById(R.id.itemTabLayout);
        //end

        //view page
        itemViewPager = (ViewPager)view.findViewById(R.id.itemViewPager);
        //end

        //imagebutton
        ImageButton ibViewChange = (ImageButton)view.findViewById(R.id.ibViewChange);
        //end

        new GuestHomeCategoryLodingTask().execute();
        return view;
    }

    //pager adapter
    static class ItemPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ItemPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
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
            CategoryJSONParser objCategoryJSONParser=new CategoryJSONParser();
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
                    itemPagerAdapter.AddFragment(com.arraybit.pos.ItemTabFragment.createInstance((CategoryMaster) alCategoryMaster.get(i)), alCategoryMaster.get(i).getCategoryName());
                }

                itemViewPager.setAdapter(itemPagerAdapter);
                itemTabLayout.setupWithViewPager(itemViewPager);
                progressDialog.dismiss();
            }
        }
    }
    //endregion
}
