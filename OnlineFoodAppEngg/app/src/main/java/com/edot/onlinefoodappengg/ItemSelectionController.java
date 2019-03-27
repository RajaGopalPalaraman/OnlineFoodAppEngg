package com.edot.onlinefoodappengg;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemSelectionController {

    public static final String LIST_OF_ITEMS = "ListofItems";

    private ArrayList<ItemDataModel> listOfSelectedItems = new ArrayList<>();
    private ArrayList<View> listOfSelectedItemsViews = new ArrayList<>();

    private boolean addData = true;
    private Activity context;
    private ViewGroup rootView;
    private ViewGroup rootViewGroup;

    private int total = 0;


    public ItemSelectionController(Activity context) {
        this.context = context;
    }

    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
        rootViewGroup = rootView.findViewById(R.id.itemPageSelectedItemListView);
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public ViewGroup getRootViewGroup() {
        return rootViewGroup;
    }

    public void setRootViewGroup(ViewGroup rootViewGroup) {
        this.rootViewGroup = rootViewGroup;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<ItemDataModel> getListOfSelectedItems() {
        addData = false;
        return listOfSelectedItems;
    }

    public void removeItem(@NonNull ItemDataModel itemDataModel)
    {
        int remainingCount = itemDataModel.decrementQuantity(1);
        total = total - itemDataModel.getCost();
        int i = listOfSelectedItems.indexOf(itemDataModel);
        View view = listOfSelectedItemsViews.get(i);
        if(remainingCount == 0)
        {
            rootViewGroup.removeView(view);

            listOfSelectedItemsViews.remove(i);
            listOfSelectedItems.remove(i);
        }
        else
        {
            updateItemViewGroup(itemDataModel,view);
        }
        TextView textView = rootView.findViewById(R.id.itemPageFooterLayout).findViewById(R.id.netCostView);
        textView.setText(context.getResources().getString(R.string.totalCost,total));
        if (total == 0)
        {
            rootView.findViewById(R.id.itemPageFooterLayout).setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.itemPageEmptyCartView).setVisibility(View.VISIBLE);

        }
    }

    private void removeView(int i) {
        View view = listOfSelectedItemsViews.get(i);
        rootViewGroup.removeView(view);
        listOfSelectedItemsViews.remove(i);
    }

    public void addItem(@NonNull ItemDataModel itemDataModel)
    {
        if (addData) {
            try {
                itemDataModel.incrementQuantity(1);

                if (total == 0) {
                    rootView.findViewById(R.id.itemPageFooterLayout).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.itemPageEmptyCartView).setVisibility(View.GONE);
                }

                total = total + itemDataModel.getCost();
                int indexOfItem = listOfSelectedItems.indexOf(itemDataModel);
                if (indexOfItem == -1) {
                    View view = generateSelectedItemView(itemDataModel);
                    rootViewGroup.addView(view);

                    listOfSelectedItems.add(itemDataModel);
                    listOfSelectedItemsViews.add(view);
                } else {
                    View view = listOfSelectedItemsViews.get(indexOfItem);
                    updateItemViewGroup(itemDataModel, view);
                }
                TextView textView = rootView.findViewById(R.id.itemPageFooterLayout).findViewById(R.id.netCostView);
                textView.setText(context.getResources().getString(R.string.totalCost, total));
            } catch (Exception e) {
                Log.d(AppConstants.LOG_TAG, "Exception in ItemSelectionController::addItem : " + e.getLocalizedMessage());
            }
        }
    }

    private View generateSelectedItemView(@NonNull final ItemDataModel itemDataModel) {
        try {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.layout_selected_items, null, false);
            updateItemViewGroup(itemDataModel,view);
            TextView textView = view.findViewById(R.id.decrementButton);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItem(itemDataModel);
                }
            });
            textView = view.findViewById(R.id.incrementButton);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(itemDataModel);
                }
            });
            return view;
        } catch (Exception e) {
            Log.d(AppConstants.LOG_TAG,"Exception in ItemSelectionController::generateSelectedItemView : "+e.getLocalizedMessage());
            return null;
        }
    }

    private void updateItemViewGroup(@NonNull ItemDataModel itemDataModel,@NonNull View view)
    {
        TextView textView = view.findViewById(R.id.itemNameView);
        textView.setText(itemDataModel.getName());
        textView = view.findViewById(R.id.countView);
        textView.setText(String.valueOf(itemDataModel.getQuantity()));
        int cost = itemDataModel.getQuantity() * itemDataModel.getCost();
        textView = view.findViewById(R.id.amountView);
        textView.setText(context.getResources().getString(R.string.selectedItemCost,cost));
    }

}
