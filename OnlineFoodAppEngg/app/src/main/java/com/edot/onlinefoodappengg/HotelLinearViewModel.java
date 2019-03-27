package com.edot.onlinefoodappengg;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edot.models.LinearViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HotelLinearViewModel extends LinearViewModel {

    static final String FIELD = "hotels";
    public static final List<String> ATTR_LIST = Arrays.asList(
            "id",
            "name",
            "extracomment"
    );

    HotelLinearViewModel(Context context) {
        super(context);
    }

    @Override
    public View renderChildMap(final HashMap<String, String> childMap) {
        if(childMap == null)
        {
            return null;
        }
        String name = childMap.get(ATTR_LIST.get(1));
        String comment = childMap.get(ATTR_LIST.get(2));
        if (name == null || comment == null)
        {
            return null;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(0,10,0,10);
        linearLayout.setBackgroundResource(R.drawable.touch_background);
        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppConstants.LOG_TAG,"Hotel selected : "+childMap.toString());
                Intent intent = new Intent(context,TablesActivity.class);
                intent.putExtra(ATTR_LIST.get(0),childMap.get(ATTR_LIST.get(0)));
                intent.putExtra(ATTR_LIST.get(1),childMap.get(ATTR_LIST.get(1)));
                intent.putExtra(ATTR_LIST.get(2),childMap.get(ATTR_LIST.get(2)));
                context.startActivity(intent);
            }
        });

        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setTextSize(20.0f);
        linearLayout.addView(textView);

        textView = new TextView(context);
        textView.setText(comment);
        linearLayout.addView(textView);

        return linearLayout;
    }

}
