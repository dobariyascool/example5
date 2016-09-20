package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.parser.TableJSONParser;
import com.arraybit.parser.WaitingJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class AllTablesFragment extends Fragment implements View.OnClickListener, TablesAdapter.LayoutClickListener, SearchView.OnQueryTextListener, TableStatusFragment.UpdateTableStatusListener, ConfirmDialog.ConfirmationResponseListener {

    static boolean isRefresh = false;
    boolean isFilter;
    TablesAdapter tablesAdapter;
    ArrayList<TableMaster> alTableMaster;
    FloatingActionMenu famRoot;
    Activity activityName;
    CoordinatorLayout allTablesFragment;
    boolean isChangeMode, isVacant = false, isClick;
    String linktoOrderTypeMasterId, tableStatusMasterId;
    LinearLayout errorLayout;
    RecyclerView rvTables;
    GridLayoutManager gridLayoutManager;
    int counterMasterId, position, tableMasterId;
    SharePreferenceManage objSharePreferenceManage;
    Bundle bundle;
    DisplayMetrics displayMetrics;
    WaitingMaster objWaitingMaster;
    ImageView ivErrorIcon;
    TextView txtMsg;

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
                if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
                    if (Globals.objAppThemeMaster != null) {
                        Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), Globals.objAppThemeMaster.getColorCardText());
                    } else {
                        Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));
                    }
                } else {
                    Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));
                }
            }
            setHasOptionsMenu(true);
        }

        allTablesFragment = (CoordinatorLayout) view.findViewById(R.id.allTablesFragment);

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (com.rey.material.widget.TextView) errorLayout.findViewById(R.id.txtMsg);
        ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);

        rvTables = (RecyclerView) view.findViewById(R.id.rvTables);
        rvTables.setHasFixedSize(true);
        rvTables.setVisibility(View.GONE);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            if (Globals.objAppThemeMaster != null) {
                objSharePreferenceManage = new SharePreferenceManage();
                String encodedImage = objSharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage1), getActivity());
                if (encodedImage != null && !encodedImage.equals("")) {
                    Globals.SetPageBackground(getActivity(), encodedImage, null, null, null, allTablesFragment);
                } else {
                    Globals.SetScaleImageBackground(getActivity(), allTablesFragment);
                }
//                if (Globals.objAppThemeMaster.getBackImageName1() != null && !Globals.objAppThemeMaster.getBackImageName1().equals("")) {
////                    Log.e("image", " " + Globals.objAppThemeMaster.getBackImageName1());
//                    Glide.with(this).load(Globals.objAppThemeMaster.getBackImageName1()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            Drawable drawable = new BitmapDrawable(resource);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                allTablesFragment.setBackground(drawable);
//                            }
//                        }
//                    });
//                } else {
//                    Globals.SetScaleImageBackground(getActivity(), allTablesFragment);
//                }
            } else {
                Globals.SetScaleImageBackground(getActivity(), allTablesFragment);
            }
            ivErrorIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.errorIconColor), PorterDuff.Mode.SRC_IN);
            txtMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        } else {
            allTablesFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_img));
        }

        //get counterMasterId
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        //end
        bundle = getArguments();
        if (bundle != null) {
            isVacant = bundle.getBoolean("IsVacant");
            isClick = bundle.getBoolean("IsClick");
            objWaitingMaster = bundle.getParcelable("WaitingMaster");
            this.linktoOrderTypeMasterId = String.valueOf(bundle.getInt("linktoOrderTypeMasterId"));
        }

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
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection), R.drawable.wifi_off);
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
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1)
                        .getName().equals(getActivity().getResources().getString(R.string.title_fragment_all_tables))) {
                    Globals.isWishListShow = 0;
                     getActivity().getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_tables), FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            menu.findItem(R.id.notification_layout).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        }
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
            if (isClick) {
                if (objWaitingMaster != null) {
                    tableMasterId = objTableMaster.getTableMasterId();
                    String message = "Want to assign" + " " + objTableMaster.getShortName() + "(" + objTableMaster.getMaxPerson() + ") to " + objWaitingMaster.getPersonName().trim()
                            + "(" + objWaitingMaster.getNoOfPersons() + " persons)?";
                    ConfirmDialog confirmDialog = new ConfirmDialog(message, false);
                    confirmDialog.setTargetFragment(this, 0);
                    confirmDialog.show(getActivity().getSupportFragmentManager(), "");
                }
            } else {
                if (isChangeMode) {
                    Globals.isWishListShow = 1;
                    Globals.counter = 0;
                    Globals.selectTableMasterId = objTableMaster.getTableMasterId();
                    Globals.alOrderItemTran = new ArrayList<>();
                    Globals.DisableBroadCastReceiver(getActivity());
                    Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                    intent.putExtra("TableMaster", objTableMaster);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    if (Globals.selectTableMasterId == 0) {
                        Globals.selectTableMasterId = objTableMaster.getTableMasterId();
                    }
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_all_tables), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.putExtra("IsFavoriteShow", true);
                    intent.setFlags(0);
                    intent.putExtra("TableMaster", objTableMaster);
                    startActivityForResult(intent, 100);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                    TableOnclickListener tableOnclickListener= (TableOnclickListener) getActivity();
//                    tableOnclickListener.TableOnClick(objTableMaster);
                }
            }
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Occupied.toString())) {
            if (isVacant) {
                Globals.isWishListShow = 1;
                Globals.DisableBroadCastReceiver(getActivity());
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
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
            }
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

    @Override
    public void ConfirmResponse() {
        if (Service.CheckNet(getActivity())) {
            new UpdateTableStatusLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(rvTables, getResources().getString(R.string.MsgCheckConnection), getActivity(), 2000);
        }
    }

    //region Private Methods & Interfaces
    private void SetErrorLayout(boolean isShow, String errorMsg, int errorIcon) {
        TextView txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        } else {
            ivErrorIcon.setImageResource(R.drawable.alert);
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
            if (isClick) {
                tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, false, this, getActivity().getSupportFragmentManager(), true, false, true, true);
            } else {
                tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, false, null, getActivity().getSupportFragmentManager(), true, false, true, false);
            }
        } else {
            tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, true, this, getActivity().getSupportFragmentManager(), true, false, false, false);
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

//    public interface TableOnclickListener
//    {
//        void TableOnClick(TableMaster objTableMaster);
//    }

    //endregion

    //region Loading Task
    class TableMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
//            if (isVacant) {
//                tableStatusMasterId = String.valueOf(Globals.TableStatus.Vacant.getValue());
//            }
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
                SetErrorLayout(true, String.format(getActivity().getResources().getString(R.string.MsgNoRecordFound), getActivity().getResources().getString(R.string.MsgTable)), 0);
            } else {
                SetErrorLayout(false, null, 0);
                if (isVacant) {
                    alTableMaster = new ArrayList<>();
                    for (TableMaster objTableMaster : lstTableMaster) {
                        if (objTableMaster.getlinktoTableStatusMasterId() == Globals.TableStatus.Vacant.getValue() || objTableMaster.getlinktoTableStatusMasterId() == Globals.TableStatus.Occupied.getValue()) {
                            if ((objTableMaster.getWaitingPersonName() != null && !objTableMaster.getWaitingPersonName().equals("")) && objTableMaster.getlinktoTableStatusMasterId() == Globals.TableStatus.Occupied.getValue()) {
                                alTableMaster.add(objTableMaster);
                            } else if (objTableMaster.getlinktoTableStatusMasterId() == Globals.TableStatus.Vacant.getValue()) {
                                alTableMaster.add(objTableMaster);
                            } else if (objTableMaster.getlinktoTableStatusMasterId() == Globals.TableStatus.Occupied.getValue()) {
                                alTableMaster.add(objTableMaster);
                            }
                        }
                    }
                } else {
                    alTableMaster = lstTableMaster;
                }
                SetupRecyclerView(rvTables, alTableMaster);
            }
        }
    }

    class UpdateTableStatusLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;
        String status;
        TableMaster objTable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            objTable = new TableMaster();
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Occupied.getValue());
            objTable.setTableMasterId((short) tableMasterId);
            objTable.setStatusColor(Globals.TableStatusColor.Occupied.getValue());
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTable);

            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (status.equals("-1")) {
                progressDialog.dismiss();
            } else if (status.equals("0")) {
                progressDialog.dismiss();
                if (Service.CheckNet(getActivity())) {
                    tablesAdapter.UpdateData(position, objTable);
                    new UpdateWaitingStatusLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(rvTables, getResources().getString(R.string.MsgCheckConnection), getActivity(), 2000);
                }
            }
        }
    }

    class UpdateWaitingStatusLoadingTask extends AsyncTask {

        String status;
        WaitingMaster objUpdateWaitingMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            objUpdateWaitingMaster = new WaitingMaster();
            objUpdateWaitingMaster.setWaitingMasterId(objWaitingMaster.getWaitingMasterId());
            objUpdateWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.Assign.getValue());
            objUpdateWaitingMaster.setLinktoTableMasterId((short) tableMasterId);
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
                objUpdateWaitingMaster.setLinktoUserMasterIdUpdatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity())));
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            status = objWaitingJSONParser.UpdateWaitingStatus(objUpdateWaitingMaster);

            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (status.equals("0")) {
                WaitingStatusFragment.UpdateStatusListener objUpdateStatusListener = (WaitingStatusFragment.UpdateStatusListener) getActivity();
                objUpdateStatusListener.UpdateStatus(false);
            }
        }
    }

    //endregion
}
