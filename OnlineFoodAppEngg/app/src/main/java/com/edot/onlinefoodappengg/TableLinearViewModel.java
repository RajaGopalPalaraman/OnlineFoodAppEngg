package com.edot.onlinefoodappengg;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.edot.models.LinearViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class TableLinearViewModel extends LinearViewModel {

    static final String FIELD = "tables";
    public static final List<String> ATTR_LIST = Arrays.asList(
            "tableID",
            "tableName",
            "description",
            "time"
    );

    private String hotelName;
    private String hotelID;
    private String hotelComment;

    public TableLinearViewModel(Context context, String hotelName, String hotelID, String hotelComment) {
        super(context);
        this.hotelName = hotelName;
        this.hotelID = hotelID;
        this.hotelComment = hotelComment;
    }

    @Override
    public View renderChildMap(final HashMap<String, String> childMap) {
        if(childMap == null)
        {
            return null;
        }
        String text = childMap.get(ATTR_LIST.get(1));
        if (text == null || childMap.get(ATTR_LIST.get(3)) == null)
        {
            return null;
        }
        text = text +"\nTime: "+childMap.get(ATTR_LIST.get(3));
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(20.0f);
        textView.setPadding(0,10,0,10);
        textView.setBackgroundResource(R.drawable.touch_background);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ItemPage.class);
                intent.putExtra(ATTR_LIST.get(0),childMap.get(ATTR_LIST.get(0)));
                intent.putExtra(HotelLinearViewModel.ATTR_LIST.get(0),hotelID);
                intent.putExtra(HotelLinearViewModel.ATTR_LIST.get(1),hotelName);
                intent.putExtra(HotelLinearViewModel.ATTR_LIST.get(2),hotelComment);
                context.startActivity(intent);
            }
        });
        return textView;
    }
}

