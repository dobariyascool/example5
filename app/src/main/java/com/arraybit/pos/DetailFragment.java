package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.arraybit.adapter.ItemOptionValueAdapter;
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

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment implements View.OnClickListener, ModifierSelectionFragmentDialog.ModifierResponseListener, ModifierAdapter.ModifierCheckedChangeListener {

    public static ArrayList<OptionMaster> alOptionValue;
    ImageView ivItemImage, ivTest, ivJain;
    TextView txtItemName, txtDescription, txtItemPrice;
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
    StringBuilder sbModifier, sbModifierName;
    double totalModifierAmount, totalAmount, totalTax;
    ResponseListener objResponseListener;
    ArrayList<OptionMaster> alOptionMaster;
    String itemName, strOptionName;
    boolean isVeg, isNonVeg, isJain;
    RecyclerView rvModifier, rvOptionValue;
    ArrayList<OptionValueTran> lstOptionValueTran, lstOptionValue;
    ArrayList<ItemMaster> alCheckedModifier = new ArrayList<>();

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
            if (objItemMaster.getItemName() != null) {
                app_bar.setTitle(objItemMaster.getCategory());
            } else {
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_detail));
            }
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        ivItemImage = (ImageView) view.findViewById(R.id.ivItemImage);
        ivTest = (ImageView) view.findViewById(R.id.ivTest);
        ivJain = (ImageView) view.findViewById(R.id.ivJain);

        rvModifier = (RecyclerView) view.findViewById(R.id.rvModifier);
        rvOptionValue = (RecyclerView) view.findViewById(R.id.rvOptionValue);

        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);

        //Button
        ImageButton ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        ImageButton ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOrder = (Button) view.findViewById(R.id.btnOrder);

        actRemark = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actRemark);

        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);

        ibPlus.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        textInputLayout.setOnClickListener(this);
        actRemark.setOnClickListener(this);


        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }

        setHasOptionsMenu(true);
        SetLoadingTask(container);
        SetDetail();

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
        if (MenuActivity.parentActivity) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Globals.HideKeyBoard(getActivity(), getView());
        if (item.getItemId() == android.R.id.home) {
            ModifierSelectionFragmentDialog.alFinalCheckedModifier = new ArrayList<>();
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);

                } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName() != null && getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {

                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                }
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {

                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        }
        return super.onOptionsItemSelected(item);
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

        } else if (v.getId() == R.id.btnCancel) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            ModifierSelectionFragmentDialog.alFinalCheckedModifier = new ArrayList<>();
        } else if (v.getId() == R.id.btnOrder) {
            SetOrderItemModifierTran();
            SetOrderItemTran();
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_detail))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            if (isShow) {
                if (getTargetFragment() != null) {
                    objResponseListener = (ResponseListener) getTargetFragment();
                    objResponseListener.ShowMessage(itemName);
                }
            }
            ModifierSelectionFragmentDialog.alFinalCheckedModifier = new ArrayList<>();
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
    public void ModifierResponse(boolean isChange) {
        //ModifierSelectionFragmentDialog response
//        SetTextModifier(ModifierSelectionFragmentDialog.alFinalCheckedModifier);
//        if (!sbModifierName.toString().equals("")) {
//            txtModifier.setVisibility(View.VISIBLE);
//            txtModifier.setText(sbModifierName.toString());
//        } else {
//            txtModifier.setVisibility(View.GONE);
//            txtModifier.setText("");
//        }
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
        if (objItemMaster.getItemModifierIds().equals("")) {
            if (Service.CheckNet(getActivity())) {
                new RemarkLoadingTask().execute();
            } else {
                Globals.ShowSnackBar(container, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else {
            if (Service.CheckNet(getActivity())) {
                new RemarkLoadingTask().execute();
                if (!objItemMaster.getItemModifierIds().equals("")) {
                    new ModifierLoadingTask().execute();
                }else{
                    rvModifier.setVisibility(View.GONE);
                }
                if (!objItemMaster.getOptionValueTranIds().equals("")) {
                    new OptionValueLoadingTask().execute();
                }else {
                    rvOptionValue.setVisibility(View.GONE);
                }

            } else {
                Globals.ShowSnackBar(container, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        }
    }

    private void SetItemModifierRecyclerView() {
        if (alItemMasterModifier != null && alItemMasterModifier.size() != 0) {
            rvModifier.setVisibility(View.VISIBLE);
            alItemMasterModifierFilter = new ArrayList<>();
            ModifierAdapter modifierAdapter = new ModifierAdapter(getActivity(), alItemMasterModifier, this);
            rvModifier.setAdapter(modifierAdapter);
            rvModifier.setLayoutManager(new LinearLayoutManager(getActivity()));
//            String[] strModifier = objItemMaster.getItemModifierIds().split(",");
//            for (String strModifierFilter : strModifier) {
//                for (int i = 0; i < alItemMasterModifier.size(); i++) {
//                    if (strModifierFilter.equals(String.valueOf(alItemMasterModifier.get(i).getItemMasterId()))) {
//                        alItemMasterModifierFilter.add(alItemMasterModifier.get(i));
//                        break;
//                    }
//                }
//            }
        } else {
            rvModifier.setVisibility(View.GONE);
        }

    }

    private void SetTextModifier(ArrayList<ItemMaster> alItemMasterCheckedModifier) {
        sbModifierName = new StringBuilder();
        if (alItemMasterCheckedModifier.size() > 0) {
            for (int i = 0; i < alItemMasterCheckedModifier.size(); i++) {
                sbModifierName.append(alItemMasterCheckedModifier.get(i).getItemName()).append(", ");
            }
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

    private void AddNumber(String number) {
        textInputLayout.clearFocus();
        etQuantity.requestFocus();
        etQuantity.setText(number);
    }

    private void SetOrderItemTran() {
        objOrderItemTran = new ItemMaster();
        try {
            if (Globals.alOrderItemTran.size() > 0) {
                CheckDuplicateRemarkAndModifier();
                if (!isDuplicate) {
                    itemName = objItemMaster.getItemName();
                    objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                    objOrderItemTran.setItemName(objItemMaster.getItemName());
                    objOrderItemTran.setActualSellPrice(objItemMaster.getSellPrice());
                    objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                    objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                    objOrderItemTran.setRemark(actRemark.getText().toString());
                    objOrderItemTran.setItemRemark(actRemark.getText().toString());
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
                objOrderItemTran.setRemark(actRemark.getText().toString());
                objOrderItemTran.setItemRemark(actRemark.getText().toString());
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
        int cnt, cntModifier;
        try {
            for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                cnt = 0;
                cntModifier = 0;

                if (!actRemark.getText().toString().isEmpty() && !Globals.alOrderItemTran.get(i).getRemark().isEmpty()) {

                    if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                        strNewRemark = String.valueOf(actRemark.getText().subSequence(0, actRemark.length()) + " ").split(", ");
                    } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                        strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                    } else {
                        strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                    }

                    String listRemark = Globals.alOrderItemTran.get(i).getRemark();
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
                if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran() != null && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() != 0) {
                    if (objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId() && alOrderItemModifierTran.size() != 0) {
                        ArrayList<ItemMaster> alOldOrderItemTran = Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran();
                        if (alOrderItemModifierTran.size() != 0) {
                            if (alOrderItemModifierTran.size() == alOldOrderItemTran.size()) {
                                for (int j = 0; j < alOrderItemModifierTran.size(); j++) {
                                    for (int k = 0; k < alOldOrderItemTran.size(); k++) {
                                        if (alOrderItemModifierTran.get(j).getItemModifierIds().equals(alOldOrderItemTran.get(k).getItemModifierIds())) {
                                            cntModifier = cntModifier + 1;
                                        }
                                    }
                                }
                            }
                        }
                        if (cntModifier == alOrderItemModifierTran.size() && ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt) || (actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty()))) {
                            isDuplicate = true;
                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            if (alOrderItemModifierTran.size() > 0) {
                                Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                            }
                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
                                SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
                            }
                            CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                            Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                            break;
                        } else if (actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty() && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0) {
                            isDuplicate = true;
                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            if (alOrderItemModifierTran.size() > 0) {
                                Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                            }
                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
                                SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
                            }
                            CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                            Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                            break;
                        }
                    }
                } else {
                    if ((objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId())
                            && ((actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty() && alOrderItemModifierTran.size() == 0 && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0)
                            || (strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt && alOrderItemModifierTran.size() == 0 && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0))) {
                        isDuplicate = true;
                        Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
                        if (alOrderItemModifierTran.size() > 0) {
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()) + (alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                        }
                        Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
                            SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
                        }
                        CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                        Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetOrderItemModifierTran() {
        ItemMaster objOrderItemModifier;
        alOrderItemModifierTran = new ArrayList<>();
        sbModifier = new StringBuilder();
        try {
            for (int i = 0; i < ModifierSelectionFragmentDialog.alFinalCheckedModifier.size(); i++) {

                objOrderItemModifier = new ItemMaster();
                objOrderItemModifier.setItemName(ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getItemName());
                objOrderItemModifier.setItemModifierIds(String.valueOf(ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getItemMasterId()));
                objOrderItemModifier.setActualSellPrice(ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getMRP());
                objOrderItemModifier.setMRP(ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getMRP());
                totalModifierAmount = totalModifierAmount + ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getMRP();
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

    interface ResponseListener {
        void ShowMessage(String itemName);
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
            alItemMasterModifier = objItemMasterJSONParser.SelectAllItemMasterModifier(Globals.businessMasterId);
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
                rvOptionValue.setVisibility(View.VISIBLE);
                rvOptionValue.setAdapter(new ItemOptionValueAdapter(getActivity(), alOptionMaster));
                rvOptionValue.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                rvOptionValue.setVisibility(View.GONE);
            }
        }
    }
    //endregion
}
