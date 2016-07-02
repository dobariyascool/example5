package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.TableMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.TableViewHolder> {

    public boolean isItemAnimate;
    Context context;
    ArrayList<TableMaster> alTableMaster;
    LayoutInflater layoutInflater;
    View view;
    boolean isClickEnable;
    LayoutClickListener objLayoutClickListener;
    FragmentManager fragmentManager;
    Boolean isAll, isWaitingMode, isVacantClick;
    int previousPosition;

    // Constructor
    public TablesAdapter(Context context, ArrayList<TableMaster> result, boolean isClickEnable, LayoutClickListener objLayoutClickListener, FragmentManager fragmentManager, Boolean isAll, boolean isItemAnimate, boolean isWaitingMode, boolean isVacantClick) {
        this.context = context;
        alTableMaster = result;
        this.isClickEnable = isClickEnable;
        this.layoutInflater = LayoutInflater.from(context);
        this.objLayoutClickListener = objLayoutClickListener;
        this.fragmentManager = fragmentManager;
        this.isAll = isAll;
        this.isItemAnimate = isItemAnimate;
        this.isWaitingMode = isWaitingMode;
        this.isVacantClick = isVacantClick;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_tables, parent, false);
        return new TableViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableMaster objTableMaster = alTableMaster.get(position);

        holder.cvTable.setId(position);
        if (objTableMaster.getTableColor() == null) {
            holder.cvTable.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_orange));
        } else {
            holder.cvTable.setCardBackgroundColor(Color.parseColor("#" + objTableMaster.getTableColor()));
        }
        holder.txtTableName.setText(objTableMaster.getShortName());
        holder.txtPersons.setText(String.valueOf(objTableMaster.getMaxPerson()));
        if (objTableMaster.getTableStatus() != null && objTableMaster.getTableStatus().equals(Globals.TableStatus.Block.toString())) {
            holder.txtTableStatus.setText(context.getResources().getString(R.string.tsBlocked));
        } else {
            holder.txtTableStatus.setText(objTableMaster.getTableStatus());
        }
        holder.txtTableStatus.setTextColor(Color.parseColor("#" + objTableMaster.getStatusColor()));

        if (isWaitingMode) {
            if (objTableMaster.getStatusUpdateDateTime() != null && !objTableMaster.getStatusUpdateDateTime().equals("") && objTableMaster.getlinktoTableStatusMasterId() == Globals.TableStatus.Occupied.getValue()) {
                Calendar calendar = Calendar.getInstance();
                String[] strArray = objTableMaster.getStatusUpdateDateTime().split("_");
                String currentDate = new SimpleDateFormat(Globals.DateFormat,Locale.US).format(new Date());
                if(currentDate.equals(strArray[0])) {
                    try {
                        Date currentTime = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).parse(new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(calendar.getTime()));
                        Date statusUpdateTime = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).parse(strArray[1]);
                        long timeDifference = currentTime.getTime() - statusUpdateTime.getTime();
                        int hour = (int) timeDifference / (60 * 60 * 1000) % 24;
                        int min = (int) (timeDifference / (60 * 1000)) % 60;
                        int sec = (int) (timeDifference / 1000 % 60);
                        holder.txtTableStatusTime.setVisibility(View.VISIBLE);
                        holder.txtTableStatusTime.setText(String.format("%02d:%02d", hour, min));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                holder.txtTableStatusTime.setVisibility(View.INVISIBLE);
                holder.txtTableStatusTime.setText(objTableMaster.getStatusUpdateDateTime());
            }
        } else {
            holder.txtTableStatusTime.setVisibility(View.INVISIBLE);
            if(objTableMaster.getlinktoOrderTypeMasterId()==Globals.OrderType.DineIn.getValue()){
                if(objTableMaster.getWaitingPersonName()!=null && !objTableMaster.getWaitingPersonName().equals("")){
                    String strPersonName = objTableMaster.getWaitingPersonName().substring(0,objTableMaster.getWaitingPersonName().lastIndexOf("^"));
                    String strNoOfPersons = objTableMaster.getWaitingPersonName().substring(objTableMaster.getWaitingPersonName().lastIndexOf("^")+1,objTableMaster.getWaitingPersonName().length());
                    holder.txtWaitingPersons.setVisibility(View.VISIBLE);
                    if(strPersonName.length() >= 8){
                        holder.txtWaitingPersons.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                        holder.txtWaitingPersons.setText(strPersonName.substring(0,6)+"..("+strNoOfPersons+")");
                    }else{
                        holder.txtWaitingPersons.setText(strPersonName+"("+strNoOfPersons+")");
                    }
                }else{
                    holder.txtWaitingPersons.setVisibility(View.INVISIBLE);
                }

            }else{
                holder.txtWaitingPersons.setVisibility(View.INVISIBLE);
            }
        }



        //holder animation
        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alTableMaster.size();
    }

    public void UpdateData(int position, TableMaster objTableMaster) {
        isItemAnimate = false;
        alTableMaster.get(position).setTableStatus(objTableMaster.getTableStatus());
        alTableMaster.get(position).setStatusColor(objTableMaster.getStatusColor());
        notifyDataSetChanged();
    }

    public void SetSearchFilter(ArrayList<TableMaster> result) {
        isItemAnimate = false;
        alTableMaster = new ArrayList<>();
        alTableMaster.addAll(result);
        notifyDataSetChanged();
    }

    //region interface
    public interface LayoutClickListener {
        void ChangeTableStatusClick(TableMaster objTableMaster, int position);
    }
    //endregion

    class TableViewHolder extends RecyclerView.ViewHolder {

        TextView txtTableName, txtPersons, txtTableStatus, txtTableStatusTime, txtWaitingPersons;
        CardView cvTable;

        public TableViewHolder(View itemView) {
            super(itemView);

            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtPersons = (TextView) itemView.findViewById(R.id.txtPersons);
            txtTableStatus = (TextView) itemView.findViewById(R.id.txtTableStatus);
            txtTableStatusTime = (TextView) itemView.findViewById(R.id.txtTableStatusTime);
            txtWaitingPersons = (TextView) itemView.findViewById(R.id.txtWaitingPersons);
            cvTable = (CardView) itemView.findViewById(R.id.cvTable);

            if (isClickEnable) {

                cvTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                                && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(context.getResources().getString(R.string.title_fragment_all_tables))) {

                            Globals.HideKeyBoard(context, v);
                            objLayoutClickListener.ChangeTableStatusClick(alTableMaster.get(v.getId()), v.getId());
                        }
                    }
                });
            } else {
                if (isVacantClick) {
                    cvTable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (alTableMaster.get(getAdapterPosition()).getlinktoTableStatusMasterId() == Globals.TableStatus.Vacant.getValue()) {
                                Globals.HideKeyBoard(context, v);
                                objLayoutClickListener.ChangeTableStatusClick(alTableMaster.get(v.getId()), v.getId());
                            }
                        }
                    });
                }
            }
        }
    }
}
