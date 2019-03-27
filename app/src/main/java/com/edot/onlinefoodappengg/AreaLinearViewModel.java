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

public class AreaLinearViewModel extends LinearViewModel {

    static final String FIELD = "areas";
    public static final List<String> ATTR_LIST = Arrays.asList(
            "id",
            "name"
    );

    AreaLinearViewModel(Context context)
    {
        super(context);
    }

    @Override
    public View renderChildMap(final HashMap<String, String> childMap) {
        if(childMap == null)
        {
            return null;
        }
        String text = childMap.get(ATTR_LIST.get(1));
        if (text == null)
        {
            return null;
        }
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(20.0f);
        textView.setPadding(0,10,0,10);
        textView.setBackgroundResource(R.drawable.touch_background);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppConstants.LOG_TAG,"Area selected : "+childMap.toString());
                Intent intent = new Intent(context,HotelsPage.class);
                intent.putExtra(ATTR_LIST.get(0),childMap.get(ATTR_LIST.get(0)));
                intent.putExtra(ATTR_LIST.get(1),childMap.get(ATTR_LIST.get(1)));
                context.startActivity(intent);
            }
        });
        return textView;
    }

}
