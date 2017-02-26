package com.zhanggb.game2048.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhanggb.game2048.R;

/**
 * @author zhanggaobo
 * @since 02/04/2015
 */
public class ResultDialog extends LinearLayout {

    private Context context;
    private View view;
    private TextView titleText;
    private ImageView closeImage;
    private LinearLayout contentLinear;
    private TextView okText;
    private TextView cancelText;


    public ResultDialog(Context context) {
        super(context);
        this.context = context;
        initView();
        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public ResultDialog(Context context, int width) {
        super(context);
        this.context = context;
        initView();
        addView(view, new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.result_dialog, null);
        titleText = (TextView) view.findViewById(R.id.result_title_text);
        closeImage = (ImageView) view.findViewById(R.id.result_close);
        contentLinear = (LinearLayout) view.findViewById(R.id.result_content_linear);
        okText = (TextView) view.findViewById(R.id.result_ok);
        cancelText = (TextView) view.findViewById(R.id.result_cancel);
    }

    public TextView getTitleText() {
        return titleText;
    }

    public ImageView getCloseImage() {
        return closeImage;
    }

    public LinearLayout getContentLinear() {
        return contentLinear;
    }

    public TextView getOkText() {
        return okText;
    }

    public TextView getCancelText() {
        return cancelText;
    }
}

