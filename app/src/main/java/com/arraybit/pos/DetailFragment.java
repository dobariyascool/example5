package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ToggleButton;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.adapter.ItemOptionValueAdapter;
import com.arraybit.adapter.ItemSuggestedAdapter;
import com.arraybit.adapter.ModifierAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OptionMaster;
import com.arraybit.modal.OptionValueTran;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.ItemRemarkJSONParser;
import com.arraybit.parser.OptionValueJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment implements View.OnClickListener, ModifierAdapter.ModifierCheckedChangeListener, ItemSuggestedAdapter.ImageViewClickListener, AddItemQtyDialogFragment.AddToCartListener {

    public static ArrayList<OptionMaster> alOptionValue = new ArrayList<>();
    public static ArrayList<OptionMaster> alSubItemOptionValue = new ArrayList<>();
    public static boolean isItemSuggestedClick;
    ImageView ivItemImage, ivTest, ivJain;
    TextView txtItemName, txtDescription, txtItemPrice, txtDineIn;
    EditText etQuantity;
    int counterMasterId;
    ItemMaster objItemMaster, objOrderItemTran;
    SharePreferenceManage objSharePreferenceManage;
    AppCompatMultiAutoCompleteTextView actRemark;
    TextInputLayout textInputLayout;
    ArrayList<String> alString, alStringFilter, alStringModifierFilter, alStringModifierRateFilter;
    ArrayAdapter<String> adapter;
    String[] selectedValue;
    boolean isDeleted;
    boolean isDuplicate = false, isShow = false;
    ArrayList<ItemMaster> alOrderItemModifierTran, alItemMasterModifier, alItemMasterModifierFilter;
    StringBuilder sbModifier;
    double totalModifierAmount, totalAmount, totalTax;
    ResponseListener objResponseListener;
    ArrayList<OptionMaster> alOptionMaster;
    String itemName, strOptionName;
    boolean isVeg, isNonVeg, isJain;
    RecyclerView rvModifier, rvOptionValue, rvSuggestedItem;
    ArrayList<OptionValueTran> lstOptionValueTran, lstOptionValue;
    ArrayList<ItemMaster> alCheckedModifier = new ArrayList<>();
    LinearLayout itemSuggestionLayout, wishListLayout, btnLayout;
    boolean isDineIn, isKeyClick = false;
    Button btnOrder, btnOrderDisable;
    ModifierAdapter modifierAdapter;
    ItemOptionValueAdapter itemOptionValueAdapter;
    FrameLayout detailLayout;
    StringBuilder sbOptionValue;
    boolean isWishList;
    ToggleButton tbLike;
    NestedScrollView scrollView;
    ImageButton ibPlus,ibMinus,ibDisablePlus,ibDisableMinus;



    public DetailFragment(ItemMaster objItemMaster) {
        this.objItemMaster = objItemMaster;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (objItemMaster.getCategory() != null) {
                app_bar.setTitle(objItemMaster.getCategory());
            } else {
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_detail));
            }
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        detailLayout = (FrameLayout) view.findViewById(R.id.detailLayout);
        itemSuggestionLayout = (LinearLayout) view.findViewById(R.id.itemSuggestionLayout);
        wishListLayout = (LinearLayout) view.findViewById(R.id.wishListLayout);
        btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);

        scrollView = (NestedScrollView)view.findViewById(R.id.scrollView);

        ivItemImage = (ImageView) view.findViewById(R.id.ivItemImage);
        ivTest = (ImageView) view.findViewById(R.id.ivTest);
        ivJain = (ImageView) view.findViewById(R.id.ivJain);

        rvModifier = (RecyclerView) view.findViewById(R.id.rvModifier);
        rvOptionValue = (RecyclerView) view.findViewById(R.id.rvOptionValue);
        rvSuggestedItem = (RecyclerView) view.findViewById(R.id.rvSuggestedItem);

        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
        txtDineIn = (TextView) view.findViewById(R.id.txtDineIn);

        //Button
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibDisablePlus = (ImageButton) view.findViewById(R.id.ibDisablePlus);
        ibDisableMinus = (ImageButton) view.findViewById(R.id.ibDisableMinus);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etQuantity.setSelectAllOnFocus(true);

        tbLike = (ToggleButton) view.findViewById(R.id.tbLike);

        btnOrder = (Button) view.findViewById(R.id.btnOrder);
        btnOrderDisable = (Button) view.findViewById(R.id.btnOrderDisable);

        actRemark = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actRemark);
        if (Globals.isWishListShow == 0) {
            actRemark.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.arrow_drop_down_vector_drawable), null);
        }
        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);

        ibPlus.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        textInputLayout.setOnClickListener(this);
        tbLike.setOnClickListener(this);
        if (Globals.isWishListShow == 0) {
            actRemark.setOnClickListener(this);
        }

        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }

        if (getArguments() != null) {
            isWishList = getArguments().getBoolean("IsWishList");
        }

        setHasOptionsMenu(true);
        SetLoadingTask(container);
        SetDetail();

        etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etQuantity.setSelectAllOnFocus(true);
                }
            }
        });

        etQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        isKeyClick = false;
                        Globals.HideKeyBoard(getActivity(), v);
                    } else {
                        isKeyClick = true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.viewChange).setVisible(false);
        menu.findItem(R.id.cart_layout).setVisible(false);
        if(Globals.isWishListShow==0){
            if(!GuestHomeActivity.isMenuMode) {
                menu.findItem(R.id.logout).setVisible(false);
            }
        } else if(Globals.isWishListShow==1){
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.registration).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
            menu.findItem(R.id.callWaiter).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Globals.HideKeyBoard(getActivity(), getView());
        if (item.getItemId() == android.R.id.home) {
            if (Globals.isWishListShow == 1) {
                SaveWishListData();
            }
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {

                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_sub_detail))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_sub_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {

                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            isItemSuggestedClick = false;
            alOptionValue = new ArrayList<>();
            alSubItemOptionValue = new ArrayList<>();
        }
        return super.onOptionsItemSelected(item);
    }

    public void EditTextOnClick() {
        if (!isKeyClick) {
            etQuantity.clearFocus();
            etQuantity.requestFocus();
        } else {
            isKeyClick = false;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibPlus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            etQuantity.requestFocus();

        } else if (v.getId() == R.id.ibMinus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            etQuantity.requestFocus();

        }else if (v.getId() == R.id.btnOrder) {
            SetOrderItemModifierTran();
            SetOrderItemTran();
            if(Globals.isWishListShow==1){
                SaveWishListData();
            }
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_sub_detail))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_sub_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            if (isShow) {
                if (isItemSuggestedClick) {
                    Globals.ShowSnackBar(detailLayout, String.format(getActivity().getResources().getString(R.string.MsgCartItem), itemName), getActivity(), 3000);
                    isItemSuggestedClick = false;
                }
                if (getTargetFragment() != null) {
                    objResponseListener = (ResponseListener) getTargetFragment();
                    objResponseListener.ShowMessage(itemName, false, null);
                }
            }
            isItemSuggestedClick = false;
            alOptionValue = new ArrayList<>();
            alSubItemOptionValue = new ArrayList<>();
        } else if (v.getId() == R.id.textInputLayout) {
            textInputLayout.setFocusable(true);
            etQuantity.setFocusable(false);
        } else if (v.getId() == R.id.actRemark) {
            if (actRemark.getText().toString().isEmpty()) {
                SetArrayListAdapter(alString);
            } else {
                if (isDeleted) {
                    if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                        selectedValue = String.valueOf(actRemark.getText().subSequence(0, actRemark.length()) + " ").split(", ");
                    } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                        selectedValue = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                    } else {
                        selectedValue = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                        actRemark.setText(actRemark.getText() + ", ");
                    }
                    UpdateArrayListAdapter(null);
                    isDeleted = false;
                }

            }
            actRemark.showDropDown();
        } else if (v.getId() == R.id.tbLike) {
            if (tbLike.isChecked()) {
                tbLike.setChecked(true);
            } else {
                tbLike.setChecked(false);
            }
        }
    }

    @Override
    public void ModifierCheckedChange(boolean isChecked, ItemMaster objItemModifier, boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
        if (isChecked) {
            if (alCheckedModifier.size() > 0) {
                for (ItemMaster objCheckedItemModifier : alCheckedModifier) {
                    if (objItemModifier.getItemMasterId() == objCheckedItemModifier.getItemMasterId()) {
                        this.isDuplicate = true;
                        break;
                    }
                }
                if (!this.isDuplicate) {
                    alCheckedModifier.add(objItemModifier);
                }
                this.isDuplicate = false;
            } else {
                alCheckedModifier.add(objItemModifier);
            }
        } else {
            for (ItemMaster objCheckedItemModifier : alCheckedModifier) {
                if (objItemModifier.getItemMasterId() == objCheckedItemModifier.getItemMasterId()) {
                    alCheckedModifier.remove(alCheckedModifier.indexOf(objCheckedItemModifier));
                    break;
                }
            }
        }
    }

    @Override
    public void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran) {
        if (isAddToCart) {
            if (objOrderItemTran.getItemName() != null) {
                Globals.counter = Globals.counter + 1;
                Globals.alOrderItemTran.add(objOrderItemTran);
                Globals.ShowSnackBar(detailLayout, String.format(getActivity().getResources().getString(R.string.MsgCartItem), objOrderItemTran.getItemName()), getActivity(), 3000);
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void ImageOnClick(ItemMaster objItemMaster, View view, String transitionName) {
        isItemSuggestedClick = true;
        alCheckedModifier = new ArrayList<>();
        if (objItemMaster.getItemModifierIds().equals("") && objItemMaster.getOptionValueTranIds().equals("")) {
            AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
            addItemQtyDialogFragment.setTargetFragment(this, 0);
            addItemQtyDialogFragment.show(getFragmentManager(), "");
        } else {
            objItemMaster.setIsChecked(CheckSuggestedItemInWishList(objItemMaster.getItemMasterId()));
            DetailFragment detailFragment = new DetailFragment(objItemMaster);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 21) {
                Slide slideTransition = new Slide();
                slideTransition.setSlideEdge(Gravity.RIGHT);
                slideTransition.setDuration(350);
                detailFragment.setEnterTransition(slideTransition);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
            }
            fragmentTransaction.replace(R.id.detailLayout, detailFragment, getResources().getString(R.string.title_fragment_sub_detail));
            fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_sub_detail));
            fragmentTransaction.commit();
        }
    }

    public void SaveWishListData() {
        if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
        } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
        }else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
            CheckDuplicate("1", objItemMaster);
            SaveWishListInSharePreference();
        } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
            CheckDuplicate("0", objItemMaster);
            SaveWishListInSharePreference();
        }
    }

    //region Private Methods and Interface
    private int IncrementDecrementValue(int id, int value) {
        if (id == R.id.ibPlus) {
            value++;
            etQuantity.setText(String.valueOf(value));
        } else {
            if (value > 1) {
                value--;
            }
            etQuantity.setText(String.valueOf(value));
        }
        return value;
    }

    private void SetLoadingTask(ViewGroup container) {
        if(GuestHomeActivity.isMenuMode){
            btnLayout.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.GONE);
            rvModifier.setVisibility(View.GONE);
            itemSuggestionLayout.setVisibility(View.GONE);
            rvOptionValue.setVisibility(View.GONE);
            wishListLayout.setVisibility(View.GONE);
            btnOrder.setVisibility(View.GONE);
            btnOrderDisable.setVisibility(View.GONE);
            ibDisableMinus.setVisibility(View.GONE);
            ibDisablePlus.setVisibility(View.GONE);
            ibMinus.setVisibility(View.GONE);
            ibPlus.setVisibility(View.GONE);
            etQuantity.setEnabled(false);
            if (objItemMaster.getIsDineInOnly() && Globals.orderTypeMasterId == Globals.OrderType.TakeAway.getValue()) {
                txtDineIn.setVisibility(View.VISIBLE);
            }else{
                txtDineIn.setVisibility(View.GONE);
            }
        }else {
            scrollView.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.separator));
            btnLayout.setVisibility(View.VISIBLE);
            if (objItemMaster.getIsDineInOnly() && Globals.orderTypeMasterId == Globals.OrderType.TakeAway.getValue()) {
                txtDineIn.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.GONE);
                rvModifier.setVisibility(View.GONE);
                itemSuggestionLayout.setVisibility(View.GONE);
                rvOptionValue.setVisibility(View.GONE);
                wishListLayout.setVisibility(View.GONE);
                btnOrder.setVisibility(View.GONE);
                btnOrderDisable.setVisibility(View.VISIBLE);
                ibDisableMinus.setVisibility(View.VISIBLE);
                ibDisablePlus.setVisibility(View.VISIBLE);
                ibMinus.setVisibility(View.GONE);
                ibPlus.setVisibility(View.GONE);
                etQuantity.setEnabled(false);
            } else {
                txtDineIn.setVisibility(View.GONE);
                textInputLayout.setVisibility(View.VISIBLE);
                btnOrder.setVisibility(View.VISIBLE);
                btnOrderDisable.setVisibility(View.GONE);
                ibDisableMinus.setVisibility(View.GONE);
                ibDisablePlus.setVisibility(View.GONE);
                ibMinus.setVisibility(View.VISIBLE);
                ibPlus.setVisibility(View.VISIBLE);
                etQuantity.setEnabled(true);
                if (Globals.isWishListShow == 1) {
                    wishListLayout.setVisibility(View.VISIBLE);
                } else {
                    wishListLayout.setVisibility(View.GONE);
                }
                if (objItemMaster.getItemModifierIds().equals("")) {
                    if (Service.CheckNet(getActivity())) {
                        if (Globals.isWishListShow == 0) {
                            new RemarkLoadingTask().execute();
                        }
                        if (!objItemMaster.getOptionValueTranIds().equals("")) {
                            new OptionValueLoadingTask().execute();
                        } else {
                            rvOptionValue.setVisibility(View.GONE);
                        }
                        if (isItemSuggestedClick) {
                            itemSuggestionLayout.setVisibility(View.GONE);
                        } else {
                            if (Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue()) {
                                isDineIn = true;
                            }
                            new ItemSuggestionLoadingTask().execute();
                        }
                    } else {
                        Globals.ShowSnackBar(container, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                } else {
                    if (Service.CheckNet(getActivity())) {
                        if (Globals.isWishListShow == 0) {
                            new RemarkLoadingTask().execute();
                        }
                        if (!objItemMaster.getItemModifierIds().equals("")) {
                            new ModifierLoadingTask().execute();
                        } else {
                            rvModifier.setVisibility(View.GONE);
                        }
                        if (!objItemMaster.getOptionValueTranIds().equals("")) {
                            new OptionValueLoadingTask().execute();
                        } else {
                            rvOptionValue.setVisibility(View.GONE);
                        }
                        if (isItemSuggestedClick) {
                            itemSuggestionLayout.setVisibility(View.GONE);
                        } else {
                            if (Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue()) {
                                isDineIn = true;
                            }
                            new ItemSuggestionLoadingTask().execute();
                        }
                    } else {
                        Globals.ShowSnackBar(container, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        }
    }

    private void SetItemModifierRecyclerView() {
        if (alItemMasterModifier != null && alItemMasterModifier.size() != 0) {
            rvModifier.setVisibility(View.VISIBLE);
            alItemMasterModifierFilter = new ArrayList<>();
            modifierAdapter = new ModifierAdapter(getActivity(), alItemMasterModifier, this);
            rvModifier.setAdapter(modifierAdapter);
            rvModifier.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            rvModifier.setVisibility(View.GONE);
        }

    }

    private void SetItemSuggestion(ArrayList<ItemMaster> alItemSuggestion) {
        if (alItemSuggestion != null && alItemSuggestion.size() != 0) {
            itemSuggestionLayout.setVisibility(View.VISIBLE);
            ItemSuggestedAdapter itemSuggestedAdapter = new ItemSuggestedAdapter(getActivity(), alItemSuggestion, this);
            rvSuggestedItem.setAdapter(itemSuggestedAdapter);
            rvSuggestedItem.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            itemSuggestionLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetDetail() {
        if (objItemMaster != null) {
            txtItemName.setText(objItemMaster.getItemName());
            if (!objItemMaster.getShortDescription().equals("")) {
                txtDescription.setVisibility(View.VISIBLE);
                txtDescription.setText(objItemMaster.getShortDescription());
            } else {
                txtDescription.setVisibility(View.GONE);
            }
            txtItemPrice.setText(getActivity().getResources().getString(R.string.dfRupee) + " " + (Globals.dfWithPrecision.format(objItemMaster.getSellPrice())));

            if (objItemMaster.getMD_ImagePhysicalName() != null && !objItemMaster.getMD_ImagePhysicalName().equals("")) {
                Picasso.with(getActivity()).load(objItemMaster.getMD_ImagePhysicalName()).into(ivItemImage);
            } else {
                Picasso.with(getActivity()).load(R.drawable.default_image).into(ivItemImage);
            }
            if (!objItemMaster.getOptionValueTranIds().equals("")) {
                if (CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.DoubleSpicy.getValue()))) {
                    ivTest.setVisibility(View.VISIBLE);
                    ivTest.setImageResource(R.mipmap.extra_spicy);
                } else if (CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Spicy.getValue()))) {
                    ivTest.setVisibility(View.VISIBLE);
                    ivTest.setImageResource(R.mipmap.spicy);
                } else if (CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Sweet.getValue()))) {
                    ivTest.setVisibility(View.VISIBLE);
                    ivTest.setImageResource(R.mipmap.sweet);
                } else {
                    ivTest.setVisibility(View.GONE);
                }

                isVeg = CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Veg.getValue()));
                isNonVeg = CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.NonVeg.getValue()));
                isJain = CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Jain.getValue()));
                if (isNonVeg && !isVeg) {
                    ivJain.setVisibility(View.VISIBLE);
                    ivJain.setImageResource(R.mipmap.nonvegicon);
                } else if (isJain && !isNonVeg) {
                    ivJain.setVisibility(View.VISIBLE);
                    ivJain.setImageResource(R.mipmap.jain_icon);
                } else {
                    ivJain.setVisibility(View.GONE);
                }
            } else {
                ivTest.setVisibility(View.GONE);
                ivJain.setVisibility(View.GONE);
            }
        }
        if (objItemMaster.getIsChecked() == 1) {
            tbLike.setChecked(true);
        } else {
            tbLike.setChecked(false);
        }
    }

    private void UpdateArrayListAdapter(String name) {
        int isRemove = -1;
        String str;
        if (name == null && isDeleted) {
            alStringFilter = new ArrayList<>();
            for (String strSelectedValue : selectedValue) {
                if (!strSelectedValue.equals("")) {
                    if (alStringFilter.size() == 0) {
                        for (int j = 0; j < alString.size(); j++) {
                            if (!strSelectedValue.equals(alString.get(j))) {
                                alStringFilter.add(alString.get(j));
                            }
                        }
                    } else {
                        for (int j = 0; j < alStringFilter.size(); j++) {
                            str = strSelectedValue;
                            if (alStringFilter.get(j).equals(str)) {
                                alStringFilter.remove(j);
                            }
                        }
                    }
                }
            }
            adapter = new ArrayAdapter<>
                    (getActivity(), R.layout.row_remark, alStringFilter);
            actRemark.setAdapter(adapter);
        } else {
            if (alStringFilter.size() == 0) {
                for (int i = 0; i < alString.size(); i++) {
                    if (!alString.get(i).equals(name)) {
                        alStringFilter.add(alString.get(i));
                    }
                }
                adapter = new ArrayAdapter<>
                        (getActivity(), R.layout.row_remark, alStringFilter);
                actRemark.setAdapter(adapter);

            } else {
                for (int j = 0; j < alStringFilter.size(); j++) {
                    if (alStringFilter.get(j).equals(name)) {
                        isRemove = j;
                    }
                }
                if (isRemove != -1) {
                    alStringFilter.remove(isRemove);
                }
            }
        }
        adapter = new ArrayAdapter<>
                (getActivity(), R.layout.row_remark, alStringFilter);
        actRemark.setAdapter(adapter);
    }

    private void SetArrayListAdapter(ArrayList<String> alString) {
        alStringFilter = new ArrayList<>();
        alStringModifierFilter = new ArrayList<>();
        alStringModifierRateFilter = new ArrayList<>();
        if (alString != null) {
            adapter = new ArrayAdapter<>
                    (getActivity(), R.layout.row_remark, alString);
            actRemark.setAdapter(adapter);
        }
    }

    private boolean CheckOptionValue(String optionValueIds, String optionValue) {
        List<String> items = Arrays.asList(optionValueIds.split(","));
        boolean isMatch = false;
        for (String str : items) {
            if (str.equals(optionValue)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    private void SetOrderItemTran() {
        objOrderItemTran = new ItemMaster();
        try {
            if (Globals.alOrderItemTran.size() > 0) {
                SetItemRemark();
                CheckDuplicateRemarkAndModifier();
                if (!isDuplicate) {
                    itemName = objItemMaster.getItemName();
                    objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                    objOrderItemTran.setItemName(objItemMaster.getItemName());
                    objOrderItemTran.setActualSellPrice(objItemMaster.getSellPrice());
                    objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                    objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                    SetItemRemark();
                    if (actRemark.getText().toString().isEmpty()) {
                        if (!sbOptionValue.toString().equals("")) {
                            objOrderItemTran.setRemark(sbOptionValue.toString());
                            objOrderItemTran.setOptionValue(sbOptionValue.toString());
                        }
                    } else {
                        objOrderItemTran.setItemRemark(actRemark.getText().toString());
                        if (!sbOptionValue.toString().equals("")) {
                            if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                                objOrderItemTran.setRemark(actRemark.getText().toString() + " " + sbOptionValue.toString());
                            } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                                objOrderItemTran.setRemark(actRemark.getText().toString() + sbOptionValue.toString());
                            } else {
                                objOrderItemTran.setRemark(actRemark.getText().toString() + ", " + sbOptionValue.toString());
                            }

                            objOrderItemTran.setOptionValue(sbOptionValue.toString());
                        } else {
                            objOrderItemTran.setRemark(actRemark.getText().toString());
                        }
                    }
                    //objOrderItemTran.setRemark(actRemark.getText().toString());
                    //objOrderItemTran.setItemRemark(actRemark.getText().toString());
                    objOrderItemTran.setTax(objItemMaster.getTax());
                    CountTax(objOrderItemTran, isDuplicate);
                    objOrderItemTran.setTotalTax(totalTax);
                    if (alOrderItemModifierTran.size() != 0) {
                        totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice() + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString()));
                        objOrderItemTran.setTotalAmount(totalAmount);
                    }
                    if (Integer.valueOf(etQuantity.getText().toString()) > 1) {
                        SetOrderItemModifierQty(alOrderItemModifierTran, Integer.valueOf(etQuantity.getText().toString()));
                        objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                    } else {
                        objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                    }
                    objOrderItemTran.setRateIndex(objItemMaster.getRateIndex());
                    Globals.alOrderItemTran.add(objOrderItemTran);
                    Globals.counter = Globals.counter + 1;
                    isShow = true;
                }
            } else {
                itemName = objItemMaster.getItemName();
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setActualSellPrice(objItemMaster.getSellPrice());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                SetItemRemark();
                if (actRemark.getText().toString().isEmpty()) {
                    if (!sbOptionValue.toString().equals("")) {
                        objOrderItemTran.setRemark(sbOptionValue.toString());
                        objOrderItemTran.setOptionValue(sbOptionValue.toString());
                    }
                } else {
                    objOrderItemTran.setItemRemark(actRemark.getText().toString());
                    if (!sbOptionValue.toString().equals("")) {
                        if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                            objOrderItemTran.setRemark(actRemark.getText().toString() + " " + sbOptionValue.toString());
                        } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                            objOrderItemTran.setRemark(actRemark.getText().toString() + sbOptionValue.toString());
                        } else {
                            objOrderItemTran.setRemark(actRemark.getText().toString() + ", " + sbOptionValue.toString());
                        }
                        objOrderItemTran.setOptionValue(sbOptionValue.toString());
                    } else {
                        objOrderItemTran.setRemark(actRemark.getText().toString());
                    }
                }
                //objOrderItemTran.setRemark(actRemark.getText().toString());
                //objOrderItemTran.setItemRemark(actRemark.getText().toString());
                objOrderItemTran.setTax(objItemMaster.getTax());
                CountTax(objOrderItemTran, isDuplicate);
                objOrderItemTran.setTotalTax(totalTax);
                if (alOrderItemModifierTran.size() != 0) {
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice() + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString()));
                    objOrderItemTran.setTotalAmount(totalAmount);
                }
                if (Integer.valueOf(etQuantity.getText().toString()) > 1) {
                    SetOrderItemModifierQty(alOrderItemModifierTran, Integer.valueOf(etQuantity.getText().toString()));
                    objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                } else {
                    objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                }
                objOrderItemTran.setRateIndex(objItemMaster.getRateIndex());
                Globals.alOrderItemTran.add(objOrderItemTran);
                Globals.counter = Globals.counter + 1;
                isShow = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetOrderItemModifierQty(ArrayList<ItemMaster> alItemMasterModifier, int Quantity) {
        for (int j = 0; j < alItemMasterModifier.size(); j++) {
            alItemMasterModifier.get(j).setMRP(alItemMasterModifier.get(j).getActualSellPrice() * Quantity);
        }
    }

    private void CheckDuplicateRemarkAndModifier() {
        String[] strNewRemark = new String[0], strOldRemark;
        String strOptionValue;
        int cnt, cntModifier;
        try {
            //for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
            for (ItemMaster objFilterOrderItemTran : Globals.alOrderItemTran) {
                cnt = 0;
                cntModifier = 0;

                if (actRemark.getText().toString().isEmpty()) {
                    strOptionValue = sbOptionValue.toString();
                } else {
                    if (!sbOptionValue.toString().equals("")) {
                        if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                            strOptionValue = actRemark.getText().toString() + " " + sbOptionValue.toString();
                        } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                            strOptionValue = actRemark.getText().toString() + sbOptionValue.toString();
                        } else {
                            strOptionValue = actRemark.getText().toString() + ", " + sbOptionValue.toString();
                        }
                    } else {
                        strOptionValue = actRemark.getText().toString();
                    }
                }

                if (!strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() != null && !objFilterOrderItemTran.getRemark().equals(""))) {
                    if (strOptionValue.subSequence(strOptionValue.length() - 1, strOptionValue.length()).toString().equals(",")) {
                        strNewRemark = String.valueOf(strOptionValue.subSequence(0, strOptionValue.length()) + " ").split(", ");
                    } else if (strOptionValue.subSequence(strOptionValue.length() - 1, strOptionValue.length()).toString().equals(" ")) {
                        strNewRemark = strOptionValue.subSequence(0, strOptionValue.length()).toString().split(", ");
                    } else {
                        strNewRemark = strOptionValue.subSequence(0, strOptionValue.length()).toString().split(", ");
                    }

                    String listRemark = objFilterOrderItemTran.getRemark();
                    if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(",")) {
                        strOldRemark = String.valueOf(listRemark.subSequence(0, listRemark.length()) + " ").split(", ");
                    } else if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(" ")) {
                        strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
                    } else {
                        strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
                    }
                    if (strNewRemark.length != 0) {
                        for (String newRemark : strNewRemark) {
                            for (String oldRemark : strOldRemark) {
                                if (newRemark.equals(oldRemark)) {
                                    cnt = cnt + 1;
                                }
                            }
                        }
                    }
                }

                if (objFilterOrderItemTran.getAlOrderItemModifierTran() != null && objFilterOrderItemTran.getAlOrderItemModifierTran().size() != 0) {
                    if (objItemMaster.getItemMasterId() == objFilterOrderItemTran.getItemMasterId() && alOrderItemModifierTran.size() != 0) {
                        ArrayList<ItemMaster> alOldOrderItemTran = objFilterOrderItemTran.getAlOrderItemModifierTran();
                        if (alOrderItemModifierTran.size() != 0) {
                            if (alOrderItemModifierTran.size() == alOldOrderItemTran.size()) {
                                for (ItemMaster objCheckedModifier : alOrderItemModifierTran) {
                                    for (ItemMaster objOldOrderItemTran : alOldOrderItemTran) {
                                        if (objCheckedModifier.getItemMasterId() == objOldOrderItemTran.getItemMasterId()) {
                                            cntModifier = cntModifier + 1;
                                        }
                                    }
                                }
                            }
                        }
                        if (cntModifier == alOrderItemModifierTran.size() && ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt) || (strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() == null || objFilterOrderItemTran.getRemark().equals(""))))) {
                            isDuplicate = true;
                            itemName = objItemMaster.getItemName();
                            objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            if (alOrderItemModifierTran.size() > 0) {
                                objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                            } else {
                                objFilterOrderItemTran.setTotalAmount(objFilterOrderItemTran.getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            }
                            objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            if (objFilterOrderItemTran.getAlOrderItemModifierTran().size() > 0) {
                                SetOrderItemModifierQty(objFilterOrderItemTran.getAlOrderItemModifierTran(), objFilterOrderItemTran.getQuantity());
                            }
                            CountTax(objFilterOrderItemTran, isDuplicate);
                            objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                            break;
                        } else if (strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() == null || objFilterOrderItemTran.getRemark().equals("")) && objFilterOrderItemTran.getAlOrderItemModifierTran().size() == 0) {
                            isDuplicate = true;
                            itemName = objItemMaster.getItemName();
                            objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            if (alOrderItemModifierTran.size() > 0) {
                                objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                            } else {
                                objFilterOrderItemTran.setTotalAmount(objFilterOrderItemTran.getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            }
                            objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            if (objFilterOrderItemTran.getAlOrderItemModifierTran().size() > 0) {
                                SetOrderItemModifierQty(objFilterOrderItemTran.getAlOrderItemModifierTran(), objFilterOrderItemTran.getQuantity());
                            }
                            CountTax(objFilterOrderItemTran, isDuplicate);
                            objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                            break;
                        }
                    }
                } else {
                    if ((objItemMaster.getItemMasterId() == objFilterOrderItemTran.getItemMasterId())
                            && ((strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() == null || objFilterOrderItemTran.getRemark().equals("")) && alOrderItemModifierTran.size() == 0 && objFilterOrderItemTran.getAlOrderItemModifierTran().size() == 0)
                            || (strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt && alCheckedModifier.size() == 0 && objFilterOrderItemTran.getAlOrderItemModifierTran().size() == 0))) {
                        isDuplicate = true;
                        itemName = objItemMaster.getItemName();
                        objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
                        if (alOrderItemModifierTran.size() > 0) {
                            objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                        } else {
                            objFilterOrderItemTran.setTotalAmount(objFilterOrderItemTran.getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                        }
                        objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        if (objFilterOrderItemTran.getAlOrderItemModifierTran().size() > 0) {
                            SetOrderItemModifierQty(objFilterOrderItemTran.getAlOrderItemModifierTran(), objFilterOrderItemTran.getQuantity());
                        }
                        CountTax(objFilterOrderItemTran, isDuplicate);
                        objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                        break;
                    }
                }
            }

//                if (!actRemark.getText().toString().isEmpty() && !Globals.alOrderItemTran.get(i).getRemark().isEmpty()) {
//
//                    if (sbOptionValue.)
//
//                        if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
//                            strNewRemark = String.valueOf(actRemark.getText().subSequence(0, actRemark.length()) + " ").split(", ");
//                        } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
//                            strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
//                        } else {
//                            strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
//                        }
//
//                    String listRemark = Globals.alOrderItemTran.get(i).getRemark();
//                    if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(",")) {
//                        strOldRemark = String.valueOf(listRemark.subSequence(0, listRemark.length()) + " ").split(", ");
//                    } else if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(" ")) {
//                        strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
//                    } else {
//                        strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
//                    }
//
//                    if (strNewRemark.length != 0) {
//                        for (String newRemark : strNewRemark) {
//                            for (String oldRemark : strOldRemark) {
//                                if (newRemark.equals(oldRemark)) {
//                                    cnt = cnt + 1;
//                                }
//                            }
//                        }
//                    }
//                }
//                if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran() != null && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() != 0) {
//                    if (objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId() && alOrderItemModifierTran.size() != 0) {
//                        ArrayList<ItemMaster> alOldOrderItemTran = Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran();
//                        if (alOrderItemModifierTran.size() != 0) {
//                            if (alOrderItemModifierTran.size() == alOldOrderItemTran.size()) {
//                                for (int j = 0; j < alOrderItemModifierTran.size(); j++) {
//                                    for (int k = 0; k < alOldOrderItemTran.size(); k++) {
//                                        if (alOrderItemModifierTran.get(j).getItemModifierIds().equals(alOldOrderItemTran.get(k).getItemModifierIds())) {
//                                            cntModifier = cntModifier + 1;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        if (cntModifier == alOrderItemModifierTran.size() && ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt) || (actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty()))) {
//                            isDuplicate = true;
//                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
//                            if (alOrderItemModifierTran.size() > 0) {
//                                Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
//                            }
//                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
//                            if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
//                                SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
//                            }
//                            CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
//                            Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
//                            break;
//                        } else if (actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty() && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0) {
//                            isDuplicate = true;
//                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
//                            if (alOrderItemModifierTran.size() > 0) {
//                                Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
//                            }
//                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
//                            if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
//                                SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
//                            }
//                            CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
//                            Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
//                            break;
//                        }
//                    }
//                } else {
//                    if ((objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId())
//                            && ((actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty() && alOrderItemModifierTran.size() == 0 && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0)
//                            || (strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt && alOrderItemModifierTran.size() == 0 && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0))) {
//                        isDuplicate = true;
//                        Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
//                        if (alOrderItemModifierTran.size() > 0) {
//                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
//                        }
//                        Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
//                        if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
//                            SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
//                        }
//                        CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
//                        Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
//                        break;
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetOrderItemModifierTran() {
        ItemMaster objOrderItemModifier;
        alOrderItemModifierTran = new ArrayList<>();
        sbModifier = new StringBuilder();
        try {
            for (int i = 0; i < alCheckedModifier.size(); i++) {

                objOrderItemModifier = new ItemMaster();
                objOrderItemModifier.setItemName(alCheckedModifier.get(i).getItemName());
                objOrderItemModifier.setItemModifierIds(String.valueOf(alCheckedModifier.get(i).getItemMasterId()));
                objOrderItemModifier.setActualSellPrice(alCheckedModifier.get(i).getMRP());
                objOrderItemModifier.setMRP(alCheckedModifier.get(i).getMRP());
                totalModifierAmount = totalModifierAmount + alCheckedModifier.get(i).getMRP();
                objOrderItemModifier.setTotalAmount(totalModifierAmount);
                alOrderItemModifierTran.add(objOrderItemModifier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CountTax(ItemMaster objOrderItemMaster, boolean isDuplicate) {
        totalTax = 0;
        int cnt = 0;
        double rate;
        if (objItemMaster.getTax() != null && !objItemMaster.getTax().equals("")) {
            ArrayList<String> alTax = new ArrayList<>(Arrays.asList(objItemMaster.getTax().split(",")));
            for (String tax : alTax) {
                if (isDuplicate) {
                    if (objItemMaster.getTaxRate() == 0) {
                        totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1(objOrderItemMaster.getTax1() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2(objOrderItemMaster.getTax2() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3(objOrderItemMaster.getTax3() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4(objOrderItemMaster.getTax4() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5(objOrderItemMaster.getTax5() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                            objOrderItemMaster.setIsRateTaxInclusive(false);
                        }
                    } else {
                        rate = objItemMaster.getActualSellPrice() + objItemMaster.getTaxRate();
                        totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1(objOrderItemMaster.getTax1() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2(objOrderItemMaster.getTax2() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3(objOrderItemMaster.getTax3() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4(objOrderItemMaster.getTax4() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5(objOrderItemMaster.getTax5() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                            objOrderItemMaster.setIsRateTaxInclusive(true);
                        }
                    }
                } else {
                    if (objItemMaster.getTaxRate() == 0) {
                        totalTax = totalTax + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) * Double.valueOf(tax) / 100;
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) * Double.valueOf(tax) / 100);
                            objOrderItemMaster.setIsRateTaxInclusive(false);
                        }
                    } else {
                        rate = objItemMaster.getSellPrice() + objItemMaster.getTaxRate();
                        totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                            objOrderItemMaster.setIsRateTaxInclusive(true);
                        }
                    }
                }
                cnt++;
            }
        }
    }

    private void SetItemRemark() {
        sbOptionValue = new StringBuilder();
        if (alOptionValue != null && alOptionValue.size() > 0) {
            for (OptionMaster objOptionMaster : alOptionValue) {
                if (objOptionMaster.getOptionName() != null) {
                    sbOptionValue.append(objOptionMaster.getOptionName()).append(", ");
                }
            }
        }
    }

    private short CheckSuggestedItemInWishList(int itemMasterId) {
        if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
            for (ItemMaster objMaster : CategoryItemAdapter.alWishItemMaster) {
                if (objMaster.getItemMasterId() == itemMasterId) {
                    if (objMaster.getIsChecked() == 1) {
                        return 1;
                    }
                }
            }

        } else {
            return 0;
        }
        return 0;
    }

    private void SaveWishListInSharePreference() {
        ArrayList<String> alString = new ArrayList<>();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        for (ItemMaster objWishItemMaster : CategoryItemAdapter.alWishItemMaster) {
            if (objWishItemMaster.getIsChecked() != -1 && objWishItemMaster.getIsChecked() != 0) {
                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
            }
        }
        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
    }

    private void CheckDuplicate(String isChecked, ItemMaster objItemMaster) {
        int cnt = 0;
        if (isChecked != null) {
            if (CategoryItemAdapter.alWishItemMaster.size() == 0) {
                ItemMaster objWishItemMaster = new ItemMaster();
                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
                if (isChecked.equals("1")) {
                    objWishItemMaster.setIsChecked((short) 1);
                } else {
                    objWishItemMaster.setIsChecked((short) -1);
                }
                CategoryItemAdapter.alWishItemMaster.add(objWishItemMaster);
            } else {
                isDuplicate = false;
                for (ItemMaster objItem : CategoryItemAdapter.alWishItemMaster) {
                    if (objItem.getItemMasterId() == objItemMaster.getItemMasterId()) {
                        if (isChecked.equals("0")) {
                            if (objItemMaster.getIsChecked() == 1) {
                                CategoryItemAdapter.alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
                                CategoryItemAdapter.alWishItemMaster.get(cnt).setIsChecked((short) -1);
                                isDuplicate = true;
                                break;
                            }
                        } else if (isChecked.equals("1")) {
                            CategoryItemAdapter.alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
                            CategoryItemAdapter.alWishItemMaster.get(cnt).setIsChecked((short) 1);
                            isDuplicate = true;
                            break;
                        }
                    }
                    cnt++;
                }
                if (!isDuplicate) {
                    ItemMaster objWishItemMaster = new ItemMaster();
                    objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
                    objWishItemMaster.setIsChecked((short) 1);
                    CategoryItemAdapter.alWishItemMaster.add(objWishItemMaster);
                }
            }
        }
//        else {
//            if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
//                for (ItemMaster objItem : CategoryItemAdapter.alWishItemMaster) {
//                    if (String.valueOf(objItem.getItemMasterId()).equals(String.valueOf(objItemMaster.getItemMasterId()))) {
//                        if (objItem.getIsChecked() == 1) {
//                            objItemMaster.setIsChecked((short) 1);
//                        } else if (objItem.getIsChecked() == -1) {
//                            objItemMaster.setIsChecked((short) -1);
//                        }
//                    }
//                }
//            }
//        }
    }


    interface ResponseListener {
        void ShowMessage(String itemName, boolean isWishListUpdate, ItemMaster objItemMaster);
    }
    //endregion

    //region LoadingTask
    class RemarkLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            ItemRemarkJSONParser objItemRemarkJSONParser = new ItemRemarkJSONParser();
            alString = objItemRemarkJSONParser.SelectAllItemRemarkMaster(Globals.businessMasterId);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alString != null && alString.size() != 0) {

                alStringFilter = new ArrayList<>();

                actRemark.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                SetArrayListAdapter(alString);

                actRemark.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            if (actRemark.getText().toString().isEmpty()) {
                                SetArrayListAdapter(alString);
                                isDeleted = false;
                            } else {
                                isDeleted = true;
                            }
                        }
                        return false;
                    }
                });

                actRemark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Globals.HideKeyBoard(getActivity(), textInputLayout);
                        UpdateArrayListAdapter((String) parent.getAdapter().getItem(position));
                    }
                });
            }
        }
    }

    class ModifierLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            ItemJSONParser objItemMasterJSONParser = new ItemJSONParser();
            alItemMasterModifier = objItemMasterJSONParser.SelectAllItemMasterModifier(Globals.businessMasterId, objItemMaster.getItemMasterId());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            SetItemModifierRecyclerView();
        }
    }

    class OptionValueLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lstOptionValueTran = new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OptionValueJSONParser objOptionValueJSONParser = new OptionValueJSONParser();
            lstOptionValue = objOptionValueJSONParser.SelectAllItemOptionValue(String.valueOf(objItemMaster.getItemMasterId()));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (lstOptionValue != null && lstOptionValue.size() != 0) {
                alOptionMaster = new ArrayList<>();
                lstOptionValueTran = new ArrayList<>();
                strOptionName = null;
                OptionMaster objOptionMaster = new OptionMaster();
                for (OptionValueTran objOptionValueTran : lstOptionValue) {
                    if (strOptionName == null) {
                        strOptionName = objOptionValueTran.getOptionName();
                        objOptionMaster = new OptionMaster();
                        objOptionMaster.setOptionRowId(-1);
                        objOptionMaster.setOptionName(objOptionValueTran.getOptionName());
                        objOptionMaster.setOptionMasterId(objOptionValueTran.getlinktoOptionMasterId());
                        lstOptionValueTran.add(objOptionValueTran);
                        if (lstOptionValue.indexOf(objOptionValueTran) == lstOptionValue.size() - 1) {
                            objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                            alOptionMaster.add(objOptionMaster);
                        }
                    } else {
                        if (strOptionName.equals(objOptionValueTran.getOptionName())) {
                            lstOptionValueTran.add(objOptionValueTran);
                            if (lstOptionValue.indexOf(objOptionValueTran) == lstOptionValue.size() - 1) {
                                objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                                alOptionMaster.add(objOptionMaster);
                            }
                        } else {
                            objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                            alOptionMaster.add(objOptionMaster);
                            strOptionName = objOptionValueTran.getOptionName();
                            objOptionMaster = new OptionMaster();
                            lstOptionValueTran = new ArrayList<>();
                            lstOptionValueTran.add(objOptionValueTran);
                            objOptionMaster.setOptionRowId(-1);
                            objOptionMaster.setOptionName(objOptionValueTran.getOptionName());
                            objOptionMaster.setOptionMasterId(objOptionValueTran.getlinktoOptionMasterId());
                            if (lstOptionValue.indexOf(objOptionValueTran) == lstOptionValue.size() - 1) {
                                objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                                alOptionMaster.add(objOptionMaster);
                            }
                        }
                    }
                }

                if (isItemSuggestedClick) {
                    alSubItemOptionValue = new ArrayList<>();
                    if (alOptionMaster.size() > 0) {
                        for (OptionMaster objFilterOptionMaster : alOptionMaster) {
                            objOptionMaster = new OptionMaster();
                            objOptionMaster.setOptionRowId(-1);
                            objOptionMaster.setOptionName(null);
                            objOptionMaster.setOptionMasterId(objFilterOptionMaster.getOptionMasterId());
                            alSubItemOptionValue.add(objOptionMaster);
                        }
                    }
                } else {
                    alOptionValue = new ArrayList<>();
                    if (alOptionMaster.size() > 0) {
                        for (OptionMaster objFilterOptionMaster : alOptionMaster) {
                            objOptionMaster = new OptionMaster();
                            objOptionMaster.setOptionRowId(-1);
                            objOptionMaster.setOptionName(null);
                            objOptionMaster.setOptionMasterId(objFilterOptionMaster.getOptionMasterId());
                            alOptionValue.add(objOptionMaster);
                        }
                    }
                }
                rvOptionValue.setVisibility(View.VISIBLE);
                itemOptionValueAdapter = new ItemOptionValueAdapter(getActivity(), alOptionMaster, isItemSuggestedClick);
                rvOptionValue.setAdapter(itemOptionValueAdapter);
                rvOptionValue.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                rvOptionValue.setVisibility(View.GONE);
            }
        }
    }

    class ItemSuggestionLoadingTask extends AsyncTask {
        ArrayList<ItemMaster> alItemSuggestion;

        @Override
        protected Object doInBackground(Object[] objects) {

            ItemJSONParser objItemMasterJSONParser = new ItemJSONParser();
            alItemSuggestion = objItemMasterJSONParser.SelectAllItemSuggested(Globals.businessMasterId, objItemMaster.getItemMasterId(), objItemMaster.getRateIndex(), isDineIn ? 1 : 0);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            SetItemSuggestion(alItemSuggestion);
        }
    }

    //endregion
}
