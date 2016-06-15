package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.TableJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class AllTablesFragment extends Fragment implements View.OnClickListener, TablesAdapter.LayoutClickListener, SearchView.OnQueryTextListener, TableStatusFragment.UpdateTableStatusListener {

    static boolean isRefresh = false;
    boolean isFilter;
    TablesAdapter tablesAdapter;
    ArrayList<TableMaster> alTableMaster;
    FloatingActionMenu famRoot;
    Activity activityName;
    CoordinatorLayout allTablesFragment;
    boolean isChangeMode, isVacant = false;
    String linktoOrderTypeMasterId, tableStatusMasterId;
    LinearLayout errorLayout;
    RecyclerView rvTables;
    GridLayoutManager gridLayoutManager;
    int counterMasterId, position;
    SharePreferenceManage objSharePreferenceManage;
    Bundle bundle;
    DisplayMetrics displayMetrics;


    public AllTablesFragment(Activity activityName, boolean isChangeMode, String linktoOrderTypeMasterId) {
        this.activityName = activityName;
        this.isChangeMode = isChangeMode;
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tables, container, false);

        if (this.activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            view.findViewById(R.id.app_bar).setVisibility(View.GONE);
            setHasOptionsMenu(false);
        } else {

            Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
            app_bar.setVisibility(View.VISIBLE);

            if (app_bar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_all_tables));
            }

            allTablesFragment = (CoordinatorLayout) view.findViewById(R.id.allTablesFragment);
            Globals.SetScaleImageBackground(getActivity(), allTablesFragment);

            setHasOptionsMenu(true);
        }

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        rvTables = (RecyclerView) view.findViewById(R.id.rvTables);
        rvTables.setHasFixedSize(true);
        rvTables.setVisibility(View.GONE);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        //get counterMasterId
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        //end
        bundle = getArguments();
        if (bundle != null) {
            isVacant = bundle.getBoolean("IsVacant");
        }


        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //floating action button
        FloatingActionButton fabVacant = (FloatingActionButton) view.findViewById(R.id.fabVacant);
        FloatingActionButton fabBusy = (FloatingActionButton) view.findViewById(R.id.fabBusy);
        FloatingActionButton fabAll = (FloatingActionButton) view.findViewById(R.id.fabAll);
        //end

        //event
        fabVacant.setOnClickListener(this);
        fabBusy.setOnClickListener(this);
        fabAll.setOnClickListener(this);
        //end

        if (Service.CheckNet(getActivity())) {
            new TableMasterLoadingTask().execute();
        } else {
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection), R.drawable.wifi_drawable);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1)
                        .getName().equals(getActivity().getResources().getString(R.string.title_fragment_all_tables))) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } else {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.cart_layout).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), allTablesFragment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabVacant) {
            famRoot.close(true);
            TableDataFilter(String.valueOf(Globals.TableStatus.Vacant.getValue()));
        } else if (v.getId() == R.id.fabBusy) {
            famRoot.close(true);
            TableDataFilter(String.valueOf(Globals.TableStatus.Occupied.getValue()));
        } else if (v.getId() == R.id.fabAll) {
            famRoot.close(true);
            TableDataFilter(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            isRefresh = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            mSearchView.setMaxWidth(displayMetrics.widthPixels);
            mSearchView.setOnQueryTextListener(this);

            MenuItemCompat.setOnActionExpandListener(searchItem,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            // Do something when collapsed
                            if (alTableMaster.size() != 0 && alTableMaster != null) {
                                tablesAdapter.SetSearchFilter(alTableMaster);
                                Globals.HideKeyBoard(getActivity(), MenuItemCompat.getActionView(searchItem));
                            }
                            return true; // Return true to collapse action view
                        }

                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            // Do something when expanded
                            return true; // Return true to expand action view
                        }
                    });
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (alTableMaster.size() != 0 && alTableMaster != null) {
            final ArrayList<TableMaster> filteredList = Filter(alTableMaster, newText);
            tablesAdapter.SetSearchFilter(filteredList);
        }

        return false;
    }

    @Override
    public void UpdateTableStatus(boolean flag, TableMaster objTableMaster) {
        if (flag) {
            tablesAdapter.UpdateData(position, objTableMaster);
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void ChangeTableStatusClick(TableMaster objTableMaster, int position) {
        this.position = position;
        if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Vacant.toString())) {
            if (isChangeMode) {
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                if (Globals.selectTableMasterId == 0) {
                    Globals.selectTableMasterId = objTableMaster.getTableMasterId();
                }
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("IsFavoriteShow",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Occupied.toString())) {

            AllTablesFragment.isRefresh = true;
            Bundle bundle = new Bundle();
            bundle.putParcelable("TableMaster", objTableMaster);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
            orderSummaryFragment.setArguments(bundle);
            if (Build.VERSION.SDK_INT >= 21) {
                Slide slideTransition = new Slide();
                slideTransition.setSlideEdge(Gravity.RIGHT);
                slideTransition.setDuration(500);

                orderSummaryFragment.setEnterTransition(slideTransition);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            }

            fragmentTransaction.replace(R.id.allTablesFragment, orderSummaryFragment, getActivity().getResources().getString(R.string.title_fragment_order_summary));
            fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_order_summary));
            fragmentTransaction.commit();

        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Block.toString())) {
            TableStatusFragment tableStatusFragment = new TableStatusFragment(objTableMaster);
            tableStatusFragment.setTargetFragment(this, 0);
            tableStatusFragment.show(getFragmentManager(), "");
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Dirty.toString())) {
            TableStatusFragment tableStatusFragment = new TableStatusFragment(objTableMaster);
            tableStatusFragment.setTargetFragment(this, 0);
            tableStatusFragment.show(getFragmentManager(), "");
        }
    }

    //region Private Methods
    private void SetErrorLayout(boolean isShow, String errorMsg, int errorIcon) {
        TextView txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        } else {
            ivErrorIcon.setImageResource(R.drawable.alert_drawable);
        }
        if (isShow) {
            errorLayout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            rvTables.setVisibility(View.GONE);
            famRoot.setVisibility(View.GONE);

        } else {
            errorLayout.setVisibility(View.GONE);
            rvTables.setVisibility(View.VISIBLE);
            if (isVacant) {
                famRoot.setVisibility(View.GONE);
            } else {
                famRoot.setVisibility(View.VISIBLE);
            }
        }
    }

    private ArrayList<TableMaster> Filter(ArrayList<TableMaster> lstTableMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<TableMaster> filteredList = new ArrayList<>();
        for (TableMaster objTableMaster : lstTableMaster) {
            isFilter = false;
            ArrayList<String> alString = new ArrayList<>(Arrays.asList(objTableMaster.getShortName().toLowerCase().split(" ")));
            alString.add(0, objTableMaster.getShortName().toLowerCase());
            for (String str : alString) {
                if (str.length() >= filterName.length()) {
                    final String strItem = str.substring(0, filterName.length()).toLowerCase();
                    if (!isFilter) {
                        if (strItem.contains(filterName)) {
                            filteredList.add(objTableMaster);
                            isFilter = true;
                        }
                    }
                }
            }
        }
        return filteredList;
    }


    private void SetupRecyclerView(RecyclerView rvTables, ArrayList<TableMaster> alTableMaster) {
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, false, null, getActivity().getSupportFragmentManager(), true, false);
        } else {
            tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, true, this, getActivity().getSupportFragmentManager(), true, false);
        }

        rvTables.setAdapter(tablesAdapter);
        rvTables.setLayoutManager(gridLayoutManager);
        rvTables.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Globals.HideKeyBoard(getActivity(), recyclerView);
                if (!tablesAdapter.isItemAnimate) {
                    tablesAdapter.isItemAnimate = true;
                }
            }
        });
    }

    public void TableDataFilter(String tableStatusMasterId) {
        ArrayList<TableMaster> alTableMasterFilter = new ArrayList<>();
        if (tableStatusMasterId != null) {
            for (int i = 0; i < alTableMaster.size(); i++) {
                if (alTableMaster.get(i).getlinktoTableStatusMasterId() == Short.valueOf(tableStatusMasterId)) {
                    alTableMasterFilter.add(alTableMaster.get(i));
                }
            }
            if (alTableMasterFilter.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvTables, 0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvTables, 0);
                SetupRecyclerView(rvTables, alTableMasterFilter);
            }
        } else {
            if (alTableMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvTables, 0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvTables, 0);
                SetupRecyclerView(rvTables, alTableMaster);
            }

        }
    }
    //endregion

    //region Loading Task
    class TableMasterLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
            if (isVacant) {
                tableStatusMasterId = String.valueOf(Globals.TableStatus.Vacant.getValue());
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            return objTableJSONParser.SelectAllTableMaster(counterMasterId, tableStatusMasterId, linktoOrderTypeMasterId, Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            ArrayList<TableMaster> lstTableMaster = (ArrayList<TableMaster>) result;
            if (lstTableMaster == null) {
                SetErrorLayout(true, getActivity().getResources().getString(R.string.MsgSelectFail), 0);
            } else if (lstTableMaster.size() == 0) {
                SetErrorLayout(true, getActivity().getResources().getString(R.string.MsgNoRecord), 0);
            } else {
                SetErrorLayout(false, null, 0);
                alTableMaster = lstTableMaster;
                SetupRecyclerView(rvTables, alTableMaster);
            }
        }
    }
    //endregion
}
