package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.ModifierSelectionFragmentDialog;
import com.arraybit.pos.R;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class ModifierAdapter extends RecyclerView.Adapter<ModifierAdapter.ModifierViewHolder> {

    Context context;
    ArrayList<ItemMaster> alItemModifier;
    LayoutInflater layoutInflater;
    View view;
    boolean isDuplicate = false;
    ModifierCheckedChangeListener objModifierCheckedChangeListener;

    // Constructor
    public ModifierAdapter(Context context, ArrayList<ItemMaster> result, ModifierCheckedChangeListener objModifierCheckedChangeListener) {
        this.context = context;
        this.alItemModifier = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.objModifierCheckedChangeListener = objModifierCheckedChangeListener;
    }

    @Override
    public ModifierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_modifier, parent, false);
        return new ModifierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModifierViewHolder holder, int position) {
        ItemMaster objItemModifier = alItemModifier.get(position);
        holder.chkModifier.setId(position);
        holder.chkModifier.setText(objItemModifier.getItemName());
        holder.txtRate.setText(Globals.dfWithPrecision.format(objItemModifier.getMRP()));
        if (ModifierSelectionFragmentDialog.alFinalCheckedModifier.size() != 0) {
            CheckedModifier(objItemModifier, holder);
        }
    }

    @Override
    public int getItemCount() {
        return alItemModifier.size();
    }

    private void CheckedModifier(ItemMaster objItemMaster, ModifierViewHolder holder) {
        for (int i = 0; i < ModifierSelectionFragmentDialog.alFinalCheckedModifier.size(); i++) {
            if (objItemMaster.getItemMasterId() == ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getItemMasterId()) {
                holder.chkModifier.setChecked(true);
                break;
            }
        }
    }

    public interface ModifierCheckedChangeListener {
        void ModifierCheckedChange(boolean isChecked, ItemMaster objItemModifier, boolean isDuplicate);
    }

    //region ViewHolder
    class ModifierViewHolder extends RecyclerView.ViewHolder {

        TextView txtRate;
        CheckBox chkModifier;

        public ModifierViewHolder(View itemView) {
            super(itemView);

            txtRate = (TextView) itemView.findViewById(R.id.txtRate);

            Globals.TextViewFontTypeFace(txtRate,context);

            chkModifier = (CheckBox) itemView.findViewById(R.id.chkModifier);
            chkModifier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    objModifierCheckedChangeListener.ModifierCheckedChange(isChecked, alItemModifier.get(chkModifier.getId()), isDuplicate);
                }
            });
        }
    }
    //endregion
}
