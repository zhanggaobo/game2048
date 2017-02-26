package com.zhanggb.game2048.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import com.zhanggb.game2048.MyConfig;

import java.util.ArrayList;
import java.util.List;

public class MyAnimLayer extends FrameLayout {

    public MyAnimLayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayer();
    }

    public MyAnimLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayer();
    }

    public MyAnimLayer(Context context) {
        super(context);
        initLayer();
    }

    private void initLayer() {
    }

    public void createMoveAnim(final MyCardView from, final MyCardView to, int fromX, int toX, int fromY, int toY) {

        final MyCardView c = getCard(from.getNum());

        LayoutParams lp = new LayoutParams(MyConfig.CARD_WIDTH, MyConfig.CARD_WIDTH);
        lp.leftMargin = fromX * MyConfig.CARD_WIDTH;
        lp.topMargin = fromY * MyConfig.CARD_WIDTH;
        c.setLayoutParams(lp);

        if (to.getNum() <= 0) {
            to.getLabel().setVisibility(View.INVISIBLE);
        }
        TranslateAnimation ta = new TranslateAnimation(0, MyConfig.CARD_WIDTH * (toX - fromX), 0, MyConfig.CARD_WIDTH * (toY - fromY));
        ta.setDuration(100);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                to.getLabel().setVisibility(View.VISIBLE);
                recycleCard(c);
            }
        });
        c.startAnimation(ta);
    }

    private MyCardView getCard(int num) {
        MyCardView c;
        if (cards.size() > 0) {
            c = cards.remove(0);
        } else {
            c = new MyCardView(getContext());
            addView(c);
        }
        c.setVisibility(View.VISIBLE);
        c.setNum(num);
        return c;
    }

    private void recycleCard(MyCardView c) {
        c.setVisibility(View.INVISIBLE);
        c.setAnimation(null);
        cards.add(c);
    }

    private List<MyCardView> cards = new ArrayList<MyCardView>();

    public void createScaleTo1(MyCardView target) {
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(100);
        target.setAnimation(null);
        target.getLabel().startAnimation(sa);
    }

}
