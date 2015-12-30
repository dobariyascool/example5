package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
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
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"unchecked", "ConstantConditions"})
public class CategoryItemFragment extends Fragment implements View.OnClickListener, ItemTabFragment.CartIconListener {

    public static boolean isViewChange = false;
    public static short i = 0;
    ViewPager itemViewPager;
    TabLayout itemTabLayout;
    ItemPagerAdapter itemPagerAdapter;
    FloatingActionMenu famRoot;
    FloatingActionButton fabVeg, fabNonVeg, fabJain;
    StringBuilder sbItemTypeMasterId = new StringBuilder();
    boolean isForceToChange = false,isPause;
    short isVegCheck = 0, isNonVegCheck = 0, isJainCheck = 0;
    FrameLayout categoryItemFragment;

    public CategoryItemFragment() {

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

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            categoryItemFragment = (FrameLayout) view.findViewById(R.id.categoryItemFragment);
            Globals.SetScaleImageBackground(getActivity(), null, null, categoryItemFragment);
        }

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

        //event
        fabVeg.setOnClickListener(this);
        fabNonVeg.setOnClickListener(this);
        fabJain.setOnClickListener(this);
        //end

        new GuestHomeCategoryLodingTask().execute();

        setHasOptionsMenu(true);

        Globals.targetFragment = CategoryItemFragment.this;

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            Globals.SetScaleImageBackground(getActivity(), null, null, categoryItemFragment);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.viewChange).setVisible(true);
        menu.findItem(R.id.cart_layout).setVisible(true);
        if (i == 1) {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_grid);
        } else if (i == 2) {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_grid_two);
        } else {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_list);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(MenuActivity.parentActivity)
            {
                Globals.OptionMenuItemClick(item,getActivity(),getActivity().getSupportFragmentManager());
            }
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                if (MenuActivity.parentActivity) {
                    Globals.CategoryItemFragmentResetStaticVariable();
                    getActivity().finish();
                } else {
                    Globals.CategoryItemFragmentResetStaticVariable();
                    Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_category_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (item.getItemId() == R.id.viewChange) {
            ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            i = (short) (i + 1);
            if (i == 1) {
                item.setIcon(R.drawable.view_grid);
                isViewChange = true;
                isForceToChange = true;
                itemTabFragment.SetupRecyclerView();
            } else if (i == 2) {
                item.setIcon(R.drawable.view_grid_two);
                isViewChange = true;
                isForceToChange = true;
                itemTabFragment.SetupRecyclerView();
            } else {
                i = 0;
                item.setIcon(R.drawable.view_list);
                isViewChange = false;
                isForceToChange = true;
                itemTabFragment.SetupRecyclerView();
            }
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

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPause) {
            if (MenuActivity.parentActivity) {
                new GuestHomeCategoryLodingTask().execute();
                isPause = false;
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

    @Override
    public void CartIconOnClick() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
        fragmentTransaction.replace(android.R.id.content, new CartItemFragment(), getActivity().getResources().getString(R.string.title_fragment_cart_item));
        fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item));
        fragmentTransaction.commit();
    }

    @Override
    public void CardViewOnClick(ItemMaster objItemMaster) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
        fragmentTransaction.replace(android.R.id.content, new DetailFragment(objItemMaster.getItemMasterId()), getResources().getString(R.string.title_fragment_detail));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_detail));
        fragmentTransaction.commit();
    }

    private void RemoveFragment(FragmentTransaction fragmentTransaction, int selectedPosition) {
        if (selectedPosition == 0) {
            ItemTabFragment CurrentItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            ItemTabFragment NextItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() + 1);
            fragmentTransaction.remove(CurrentItemTabFragment);
            fragmentTransaction.remove(NextItemTabFragment);
        } else if (selectedPosition == itemPagerAdapter.getCount() - 1) {
            ItemTabFragment CurrentItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            ItemTabFragment PreviousItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() - 1);
            fragmentTransaction.remove(CurrentItemTabFragment);
            fragmentTransaction.remove(PreviousItemTabFragment);
        } else {
            ItemTabFragment CurrentItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            ItemTabFragment NextItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() + 1);
            ItemTabFragment PreviousItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() - 1);
            fragmentTransaction.remove(CurrentItemTabFragment);
            fragmentTransaction.remove(NextItemTabFragment);
            fragmentTransaction.remove(PreviousItemTabFragment);
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
            fragment.setTargetFragment(Globals.targetFragment, 0);
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

                CategoryMaster objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) 0);
                objCategoryMaster.setCategoryName("All");
                ArrayList<CategoryMaster> alCategory = new ArrayList<>();
                alCategory.add(objCategoryMaster);
                alCategoryMaster.addAll(0, alCategory);

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
