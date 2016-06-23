package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemRemarkJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("ValidFragment")
@SuppressWarnings("unchecked")
public class AddItemQtyDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etQuantity;
    ArrayList<String> alString, alStringFilter, alStringModifierFilter, alStringModifierRateFilter;
    AppCompatMultiAutoCompleteTextView actRemark;
    TextInputLayout textInputLayout;
    ImageButton ibPlus;
    ArrayAdapter<String> adapter;
    String[] selectedValue;
    boolean isDeleted, isKeyClick = false;
    AddToCartListener objAddToCartListener;
    ItemMaster objItemMaster, objOrderItemTran;
    ArrayList<ItemMaster> alOrderItemModifierTran;
    boolean isDuplicate, isEdit, isSelected;
    double totalAmount, totalTax, totalModifierAmount;
    TextView focusText;
    QtyRemarkDialogResponseListener objQtyRemarkDialogResponseListener;

    public AddItemQtyDialogFragment(ItemMaster objItemMaster) {
        this.objItemMaster = objItemMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_qty_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        ImageButton ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etQuantity.setSelectAllOnFocus(true);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);

        Button btnNum1 = (Button) view.findViewById(R.id.btnNum1);
        Button btnNum2 = (Button) view.findViewById(R.id.btnNum2);
        Button btnNum3 = (Button) view.findViewById(R.id.btnNum3);
        Button btnNum4 = (Button) view.findViewById(R.id.btnNum4);
        Button btnNum5 = (Button) view.findViewById(R.id.btnNum5);
        Button btnNum6 = (Button) view.findViewById(R.id.btnNum6);
        Button btnNum7 = (Button) view.findViewById(R.id.btnNum7);
        Button btnNum8 = (Button) view.findViewById(R.id.btnNum8);
        Button btnNum9 = (Button) view.findViewById(R.id.btnNum9);

        actRemark = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actRemark);
        if (Globals.isWishListShow == 0) {
            actRemark.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.arrow_drop_down_vector_drawable), null);
        }

        focusText = (TextView)view.findViewById(R.id.focusText);
        final TextView focusText2 = (TextView)view.findViewById(R.id.focusText2);
        focusText.requestFocus();

        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);

        ibPlus.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        textInputLayout.setOnClickListener(this);
        if (Globals.isWishListShow == 0) {
            actRemark.setOnClickListener(this);
        }

        btnNum1.setOnClickListener(this);
        btnNum2.setOnClickListener(this);
        btnNum3.setOnClickListener(this);
        btnNum4.setOnClickListener(this);
        btnNum5.setOnClickListener(this);
        btnNum6.setOnClickListener(this);
        btnNum7.setOnClickListener(this);
        btnNum8.setOnClickListener(this);
        btnNum9.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isEdit = bundle.getBoolean("IsEdit", false);
        }

        if (Service.CheckNet(getActivity())) {
            if (Globals.isWishListShow == 0) {
                new RemarkLoadingTask().execute();
            } else {
                actRemark.setText(objItemMaster.getItemRemark());
                if (objItemMaster.getItemRemark() != null && !objItemMaster.getItemRemark().equals("")) {
                    actRemark.setSelection(objItemMaster.getItemRemark().length());
                }
            }
        } else {
            Globals.ShowSnackBar(getActivity().getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        if (!isEdit) {
            if (getTargetFragment() != null) {
                objAddToCartListener = (AddToCartListener) getTargetFragment();
            }
        }

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
                        Globals.HideKeyBoard(getActivity(), v);
                        isKeyClick = true;
                        focusText2.requestFocus();
                        etQuantity.clearFocus();
                    }
                }
                return false;
            }
        });

        return view;
    }

    //region Event
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibPlus) {
            Globals.HideKeyBoard(getActivity(), v);
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            //textInputLayout.clearFocus();
            focusText.requestFocus();
            etQuantity.clearFocus();
//            etQuantity.requestFocus();
//            etQuantity.selectAll();

//            if(etQuantity.hasFocus()){
//                etQuantity.clearFocus();
//                etQuantity.selectAll();
//            }else{
//                textInputLayout.clearFocus();
//                etQuantity.requestFocus();
//                etQuantity.selectAll();
//            }

        } else if (v.getId() == R.id.ibMinus) {
            Globals.HideKeyBoard(getActivity(), v);
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            //textInputLayout.clearFocus();
            focusText.requestFocus();
            etQuantity.clearFocus();
            //etQuantity.requestFocus();
            //etQuantity.selectAll();
//            textInputLayout.clearFocus();
//            if(etQuantity.hasFocus()){
//                etQuantity.clearFocus();
//            }else{
//                textInputLayout.clearFocus();
//                etQuantity.requestFocus();
//            }

        } else if (v.getId() == R.id.btnCancel) {
            Globals.HideKeyBoard(getActivity(), v);
            if (!isEdit) {
                objAddToCartListener.AddToCart(false, null);
            }
            dismiss();
        } else if (v.getId() == R.id.btnOk) {
            Globals.HideKeyBoard(getActivity(), v);
            if (isEdit) {
                UpdateOrderItem();
                objQtyRemarkDialogResponseListener = (QtyRemarkDialogResponseListener) getTargetFragment();
                objQtyRemarkDialogResponseListener.UpdateQtyRemarkResponse(objItemMaster);
            } else {
                SetOrderItemTran();
                objAddToCartListener.AddToCart(true, objOrderItemTran);
            }
            dismiss();
        } else if (v.getId() == R.id.textInputLayout) {
            textInputLayout.setFocusable(true);
            focusText.clearFocus();
            etQuantity.clearFocus();
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
                    if (isSelected) {
                        isSelected = false;
                    }
                }
            }
            actRemark.showDropDown();
        } else if (v.getId() == R.id.btnNum1) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("1");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum2) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("2");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum3) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("3");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum4) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("4");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum5) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("5");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum6) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("6");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum7) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("7");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum8) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("8");
            focusText.requestFocus();
            etQuantity.clearFocus();
        } else if (v.getId() == R.id.btnNum9) {
            Globals.HideKeyBoard(getActivity(), v);
            AddNumber("9");
            focusText.requestFocus();
            etQuantity.clearFocus();
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

    private void UpdateArrayListAdapter(String name) {
        int isRemove = -1;
        String str;
        try {
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
                        (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
                actRemark.setAdapter(adapter);
            } else {
                if (alStringFilter.size() == 0) {
                    for (int i = 0; i < alString.size(); i++) {
                        if (!alString.get(i).equals(name)) {
                            alStringFilter.add(alString.get(i));
                        }
                    }
                    adapter = new ArrayAdapter<>
                            (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
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
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
            actRemark.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetArrayListAdapter(ArrayList<String> alString) {
        alStringFilter = new ArrayList<>();
        alStringModifierFilter = new ArrayList<>();
        alStringModifierRateFilter = new ArrayList<>();
        if (alString != null) {
            adapter = new ArrayAdapter<>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alString);
            actRemark.setAdapter(adapter);
        }
    }

    private void AddNumber(String number) {
        textInputLayout.clearFocus();
        //etQuantity.requestFocus();
        focusText.requestFocus();
        etQuantity.setText(number);
    }

    private void SetOrderItemTran() {
        objOrderItemTran = new ItemMaster();
        try {
            if (Globals.alOrderItemTran.size() > 0) {
                CheckDuplicateRemarkAndModifier();
                if (!isDuplicate) {
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
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice();
                    objOrderItemTran.setTotalAmount(totalAmount);
                    alOrderItemModifierTran = new ArrayList<>();
                    objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);

                }
            } else {
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
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice();
                objOrderItemTran.setTotalAmount(totalAmount);
                alOrderItemModifierTran = new ArrayList<>();
                objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void CheckDuplicateRemarkAndModifier() {
        String[] strNewRemark, strOldRemark;
        int cnt;
        try {
            for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                cnt = 0;
                if (objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId()) {

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

                        if ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt)) {
                            isDuplicate = true;
                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                            Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                            break;
                        }
                    } else {
                        if (actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty()) {
                            isDuplicate = true;
                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                            Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                            break;
                        }
                    }
                }
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

    private void UpdateOrderItem() {
        objItemMaster.setItemRemark(actRemark.getText().toString());
        objItemMaster.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
        CountUpdateTax();
        objItemMaster.setTotalTax(totalTax);
        UpdateOrderItemModifier();
        if (alOrderItemModifierTran.size() != 0) {
            objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
        }
        objItemMaster.setSellPrice(objItemMaster.getActualSellPrice() * Integer.parseInt(etQuantity.getText().toString()));
        if (alOrderItemModifierTran.size() != 0) {
            objItemMaster.setTotalAmount((objItemMaster.getActualSellPrice() + totalModifierAmount) * Integer.parseInt(etQuantity.getText().toString()));
        } else {
            objItemMaster.setTotalAmount(objItemMaster.getActualSellPrice() * Integer.parseInt(etQuantity.getText().toString()));
        }
    }

    private void CountUpdateTax() {
        totalTax = 0;
        int cnt = 0;
        double rate;
        if (objItemMaster.getTax() != null && !objItemMaster.getTax().equals("")) {
            ArrayList<String> alTax = new ArrayList<>(Arrays.asList(objItemMaster.getTax().split(",")));
            for (String tax : alTax) {
                if (objItemMaster.getTaxRate() == 0) {
                    totalTax = totalTax + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100;
                    if (cnt == 0) {
                        objItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                    } else if (cnt == 1) {
                        objItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                    } else if (cnt == 2) {
                        objItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                    } else if (cnt == 3) {
                        objItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                    } else {
                        objItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getActualSellPrice()) * Double.valueOf(tax) / 100);
                    }
                } else {
                    rate = objItemMaster.getActualSellPrice() + objItemMaster.getTaxRate();
                    totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    if (cnt == 0) {
                        objItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else if (cnt == 1) {
                        objItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else if (cnt == 2) {
                        objItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else if (cnt == 3) {
                        objItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else {
                        objItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    }
                }

                cnt++;
            }
        }
    }

    private void UpdateOrderItemModifier() {
        alOrderItemModifierTran = new ArrayList<>();
        totalModifierAmount = 0;
        if (objItemMaster.getAlOrderItemModifierTran() != null && objItemMaster.getAlOrderItemModifierTran().size() != 0) {
            for (ItemMaster objItemModifier : objItemMaster.getAlOrderItemModifierTran()) {
                ItemMaster objModifier = new ItemMaster();
                objModifier = objItemModifier;
                objModifier.setActualSellPrice(objItemModifier.getActualSellPrice());
                objModifier.setMRP(objItemModifier.getActualSellPrice() * Integer.parseInt(etQuantity.getText().toString()));
                totalModifierAmount = totalModifierAmount + objModifier.getActualSellPrice();
                alOrderItemModifierTran.add(objModifier);
            }
        }
    }

    public interface QtyRemarkDialogResponseListener {
        void UpdateQtyRemarkResponse(ItemMaster objOrderItem);
    }

    interface AddToCartListener {
        void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran);
    }
    //endregion

    //region Loading Task
    class RemarkLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            ItemRemarkJSONParser objItemRemarkJSONParser = new ItemRemarkJSONParser();
            alString = objItemRemarkJSONParser.SelectAllItemRemarkMaster(Globals.businessMasterId);
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object result) {

            if (alString != null && alString.size() != 0) {
                alStringFilter = new ArrayList<>();

                actRemark.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                SetArrayListAdapter(alString);

                if (isEdit) {
                    etQuantity.setText(String.valueOf(objItemMaster.getQuantity()));
                    if(objItemMaster.getItemRemark()!=null && !objItemMaster.getItemRemark().equals("")) {
                        if (objItemMaster.getItemRemark().subSequence(objItemMaster.getItemRemark().length() - 1, objItemMaster.getItemRemark().length()).toString().equals(",")) {
                            actRemark.setText(objItemMaster.getItemRemark() + " ");
                        } else if (objItemMaster.getItemRemark().subSequence(objItemMaster.getItemRemark().length() - 1, objItemMaster.getItemRemark().length()).toString().equals(" ")) {
                            actRemark.setText(objItemMaster.getItemRemark());
                        } else {
                            actRemark.setText(objItemMaster.getItemRemark() + ", ");
                        }
                    }

                    if (objItemMaster.getItemRemark() != null && !objItemMaster.getItemRemark().equals("")) {
                        actRemark.setSelection(actRemark.getText().length());
                    }
                    isDeleted = true;
                    isSelected = true;
                } else {
                    SetArrayListAdapter(alString);
                }

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
    //endregion
}

