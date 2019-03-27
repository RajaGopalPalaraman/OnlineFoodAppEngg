package com.edot.onlinefoodappengg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.edot.models.LinearViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class UserHistoryLinearViewModel extends LinearViewModel {

    public static final List<String> ATTR_LIST = Arrays.asList(
            "name",
            "city",
            "cost",
            "items",
            "userId",
            "timeStamp"
    );

    protected UserHistoryLinearViewModel(Context context) {
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
        textView = view.findViewById(R.id.orderTimeView);
        textView.setVisibility(View.VISIBLE);
        textView.setText(context.getResources().getString(R.string.timeStampText,hashMap.get(ATTR_LIST.get(5))));

        return view;
    }

}
