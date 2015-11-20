package com.arraybit.global;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    public static boolean isFirstScreen = false;
    int firstVisibleItem, visibleItemCount, totalItemCount ,lastVisibleItem;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    private int current_page = 1;
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            if (firstVisibleItem == 0 && visibleItemCount == 10 && totalItemCount == 10) {
                current_page++;
                onLoadMore(current_page);
                loading = true;
            }
            else if(visibleItemCount == totalItemCount){
                loading = false;
            }
            else{
                current_page++;
                onLoadMore(current_page);
                loading = true;
            }

//            if (((firstVisibleItem == 0) && (totalItemCount >= 10 && visibleItemCount >= 10))) {
//                current_page++;
//                onLoadMore(current_page);
//                loading = true;
//            }
//
//            if((firstVisibleItem==0) && (totalItemCount >= 10 && visibleItemCount >= 10) || current_page >= 1){
//                current_page++;
//                onLoadMore(current_page);
//                loading = true;
//            }
//            else if (visibleItemCount == totalItemCount) {
//                loading = false;
//            }

//            if(((firstVisibleItem==0) && (totalItemCount == 10 && visibleItemCount == 10))||((firstVisibleItem !=0 && current_page > 1) && (totalItemCount >=10 && visibleItemCount>=10 && visibleItemCount == totalItemCount))){
//                current_page++;
//                onLoadMore(current_page);
//                loading = true;
//            }
//            else if (visibleItemCount == totalItemCount) {
//                loading = false;
//            }
//            else {
//                current_page++;
//                onLoadMore(current_page);
//                loading = true;
//            }
        }
    }

    public abstract void onLoadMore(int current_page);
}