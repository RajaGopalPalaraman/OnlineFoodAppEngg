package com.edot.onlinefoodappengg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.edot.models.LinearViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class ManagerLinearViewModel extends LinearViewModel {

    public static final List<String> ATTR_LIST = Arrays.asList(
            "table",
            "time",
            "cost",
            "items",
            "userId",
            "timeStamp",
            "id"// Not used here
    );
    public static final String FIELD = "tables";

    protected ManagerLinearViewModel(Context context) {
        super(context);
    }

    @Override
    public View renderChildMap(HashMap<String, String> hashMap) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_order_view,null,false);

        TextView textView = view.findViewById(R.id.orderHotelNameView);
        textView.setText(hashMap.get(ATTR_LIST.get(0)));
        textView = view.findViewById(R.id.orderHotelCityView);
        textView.setText(hashMap.get(ATTR_LIST.get(1)));
        textView = view.findViewById(R.id.orderNetCostView);
        textView.setText(hashMap.get(ATTR_LIST.get(2)));
        textView = view.findViewById(R.id.orderItemView);
        textView.setText(hashMap.get(ATTR_LIST.get(3)));
        textView = view.findViewById(R.id.orderUserIDView);
        textView.setVisibility(View.VISIBLE);
        textView.setText(hashMap.get(ATTR_LIST.get(4)));

        return view;
    }
}

