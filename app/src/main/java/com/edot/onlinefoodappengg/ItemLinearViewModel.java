package com.edot.onlinefoodappengg;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edot.models.LinearViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemLinearViewModel extends LinearViewModel {

    public static final String FIELD = "items";
    public static final List<String> ATTR_LIST = Arrays.asList(
            "name",
            "extracomment",
            "cost",
            "category"
    );

    private static final String BREAKFAST = "breakfast";
    private static final String LUNCH = "lunch";
    private static final String DINNER = "dinner";

    private final LinearLayout breakFastLayout;
    private final LinearLayout lunchLayout;
    private final LinearLayout dinnerLayout;

    //Duplicate entry check lists
    private List<ItemDataModel> breakfastItems = new ArrayList<>();
    private List<ItemDataModel> lunchItems = new ArrayList<>();
    private List<ItemDataModel> dinnerItems = new ArrayList<>();

    private boolean showBreakFast;
    private boolean showLunch;
    private boolean showDinner;

    private ItemSelectionController itemSelectionController;

    ItemLinearViewModel(Context context,ItemSelectionController itemSelectionController,boolean b1,boolean b2,boolean b3) {
        super(context);
        this.itemSelectionController = itemSelectionController;

        TextView textView = new TextView(context);
        textView.setText(R.string.breakfast);
        breakFastLayout = new LinearLayout(context);
        breakFastLayout.setOrientation(LinearLayout.VERTICAL);
        breakFastLayout.addView(textView);

        textView = new TextView(context);
        textView.setText(R.string.lunch);
        lunchLayout = new LinearLayout(context);
        lunchLayout.setOrientation(LinearLayout.VERTICAL);
        lunchLayout.addView(textView);

        textView = new TextView(context);
        textView.setText(R.string.dinner);
        dinnerLayout = new LinearLayout(context);
        dinnerLayout.setOrientation(LinearLayout.VERTICAL);
        dinnerLayout.addView(textView);

        showBreakFast = b1;
        showLunch = b2;
        showDinner = b3;
    }

    public ItemSelectionController getItemSelectionController() {
        return itemSelectionController;
    }

    public void setItemSelectionController(ItemSelectionController itemSelectionController) {
        this.itemSelectionController = itemSelectionController;
    }

    @Override
    public View renderChildMap(HashMap<String, String> childMap) {
        try {

            if (childMap == null) {
                return null;
            }
            String name = childMap.get(ATTR_LIST.get(0));
            String comment = childMap.get(ATTR_LIST.get(1));
            String temp = childMap.get(ATTR_LIST.get(2));
            String category = childMap.get(ATTR_LIST.get(3));
            if(temp == null || category == null || category.isEmpty() || temp.isEmpty() || !temp.matches("\\d+"))
            {
                return null;
            }
            int cost = Integer.parseInt(temp);

            String[] categories = category.split(",");

            ItemDataModel item = null;
            for (String s : categories)
            {
                if (BREAKFAST.equals(s))
                {
                    if (item == null)
                    {
                        item = new ItemDataModel(name,cost);
                        if (breakfastItems.contains(item))
                        {
                            Log.d(AppConstants.LOG_TAG,"Duplicate Display name \'" + name +"\' detected.Item ignored");
                            return null;
                        }
                        breakfastItems.add(item);
                    }
                    breakFastLayout.addView(generateItemView(name,comment,cost,item));
                }
                else if (LUNCH.equals(s))
                {
                    if (item == null)
                    {
                        item = new ItemDataModel(name,cost);
                        if (lunchItems.contains(item))
                        {
                            Log.d(AppConstants.LOG_TAG,"Duplicate Display name \'" + name +"\' detected.Item ignored");
                            return null;
                        }
                        lunchItems.add(item);
                    }
                    lunchLayout.addView(generateItemView(name,comment,cost,item));
                }
                else if (DINNER.equals(s))
                {
                    if (item == null)
                    {
                        item = new ItemDataModel(name,cost);
                        if (dinnerItems.contains(item))
                        {
                            Log.d(AppConstants.LOG_TAG,"Duplicate Display name \'" + name +"\' detected.Item ignored");
                            return null;
                        }
                        dinnerItems.add(item);
                    }
                    dinnerLayout.addView(generateItemView(name,comment,cost,item));
                }
            }
        } catch (Exception e)
        {
            Log.d(AppConstants.LOG_TAG,"Exception while rendering Item View : " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public View renderMap(HashMap<String, HashMap<String, String>> map) {
        LinearLayout linearLayout = (LinearLayout) super.renderMap(map);
        View view = new View(context);
        view.setBackgroundResource(R.color.colorLineBlue);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        layoutParams.topMargin = 10;
        layoutParams.bottomMargin = 10;
        view.setLayoutParams(layoutParams);
        if (showBreakFast) {
            linearLayout.addView(breakFastLayout);
            linearLayout.addView(view);
        }
        if (showLunch) {
            linearLayout.addView(lunchLayout);
            view = new View(context);
            view.setBackgroundResource(R.color.colorLineBlue);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            layoutParams.topMargin = 10;
            layoutParams.bottomMargin = 10;
            view.setLayoutParams(layoutParams);
            linearLayout.addView(view);
        }
        if (showDinner) {
            linearLayout.addView(dinnerLayout);
        }
        return linearLayout;
    }

    private View generateItemView(String name,String comment, int cost,final ItemDataModel item)
    {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(0,10,0,10);
        linearLayout.setBackgroundResource(R.drawable.touch_background);
        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppConstants.LOG_TAG,"Item clicked : "+item.getName());
                itemSelectionController.addItem(item);
            }
        });

        name = name + " - Rs." + cost;
        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);

        textView = new TextView(context);
        textView.setText(comment);
        linearLayout.addView(textView);

        return linearLayout;
    }

}
