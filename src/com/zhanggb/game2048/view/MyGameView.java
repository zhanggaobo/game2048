package com.zhanggb.game2048.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhanggb.game2048.MyConfig;
import com.zhanggb.game2048.HomeActivity;
import com.zhanggb.game2048.R;

import java.util.ArrayList;
import java.util.List;

public class MyGameView extends LinearLayout {

    private Context context;
    private Handler handler;

    public MyGameView(Context context) {
        super(context);
        this.context = context;
        handler = new Handler();
        initGameView();
    }

    public MyGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        handler = new Handler();
        initGameView();
    }

    private void initGameView() {
        setOrientation(LinearLayout.VERTICAL);
//        setBackgroundResource(R.drawable.shape_title_bg);
        setBackgroundColor(getResources().getColor(R.color.game_view_bg));


        setOnTouchListener(new View.OnTouchListener() {

            private float startX
                    ,
                    startY
                    ,
                    offsetX
                    ,
                    offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;


                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        MyConfig.CARD_WIDTH = (Math.min(w, h) - 10) / MyConfig.LINES;

        addCards(MyConfig.CARD_WIDTH, MyConfig.CARD_WIDTH);

        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {

        MyCardView c;

        LinearLayout line;
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < MyConfig.LINES; y++) {
            line = new LinearLayout(getContext());
            lineLp = new LinearLayout.LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < MyConfig.LINES; x++) {
                c = new MyCardView(getContext());
                line.addView(c, cardWidth, cardHeight);
                cardsMap[x][y] = c;
            }
        }
    }

    public void startGame() {
        HomeActivity aty = HomeActivity.getHomeActivity();
        aty.clearScore();
        aty.showBestScore(aty.getBestScore());

        for (int y = 0; y < MyConfig.LINES; y++) {
            for (int x = 0; x < MyConfig.LINES; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {

        emptyPoints.clear();

        for (int y = 0; y < MyConfig.LINES; y++) {
            for (int x = 0; x < MyConfig.LINES; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {

            Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            HomeActivity.getHomeActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
        }
    }


    private void swipeLeft() {

        boolean merge = false;

        for (int y = 0; y < MyConfig.LINES; y++) {
            for (int x = 0; x < MyConfig.LINES; x++) {

                for (int x1 = x + 1; x1 < MyConfig.LINES; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            HomeActivity.getHomeActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeRight() {

        boolean merge = false;

        for (int y = 0; y < MyConfig.LINES; y++) {
            for (int x = MyConfig.LINES - 1; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            HomeActivity.getHomeActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeUp() {

        boolean merge = false;

        for (int x = 0; x < MyConfig.LINES; x++) {
            for (int y = 0; y < MyConfig.LINES; y++) {

                for (int y1 = y + 1; y1 < MyConfig.LINES; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            HomeActivity.getHomeActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeDown() {

        boolean merge = false;

        for (int x = 0; x < MyConfig.LINES; x++) {
            for (int y = MyConfig.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            HomeActivity.getHomeActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            HomeActivity.getHomeActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete() {
        boolean complete = true;
        ALL:
        for (int y = 0; y < MyConfig.LINES; y++) {
            for (int x = 0; x < MyConfig.LINES; x++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < MyConfig.LINES - 1 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < MyConfig.LINES - 1 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (HomeActivity.getHomeActivity().isHighest()) {
//                        createShareDialog();
//                    } else {
//                        createGameOver();
//                    }
//                }
//            }, 100);
        }
    }

    private void createGameOver() {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        ResultDialog resultDialog = new ResultDialog(context);
        TextView textView = new TextView(context);
        textView.setText("游戏结束！");
        textView.setPadding(0, getPx(context, 5), 0, getPx(context, 5));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textView.setGravity(Gravity.CENTER);

        resultDialog.getContentLinear().addView(textView,
                new LayoutParams(getPx(context, 150), ViewGroup.LayoutParams.WRAP_CONTENT));
        resultDialog.getOkText().setText("重新开始");
        resultDialog.getOkText().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame();
            }
        });
        resultDialog.getCancelText().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(resultDialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.shape_dialog_bg);
        dialog.show();
    }

    private void createShareDialog() {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        ResultDialog resultDialog = new ResultDialog(context);
        TextView textView = new TextView(context);
        textView.setText("您破解了当前最高纪录，是否分享给你的好友！");
        textView.setPadding(0, getPx(context, 5), 0, getPx(context, 5));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textView.setGravity(Gravity.CENTER);

        resultDialog.getContentLinear().addView(textView,
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        resultDialog.getOkText().setText("立即分享");
        resultDialog.getOkText().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(createShareIntent("分享", " 您2048当前的最高纪录为" + HomeActivity.getHomeActivity().getBestScore() + "\n" + "http://www.anzhi.com/soft_2086497.html#"));
            }
        });
        resultDialog.getCancelText().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(resultDialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.shape_dialog_bg);
        dialog.show();
    }

    public static Intent createShareIntent(String title, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return intent;
    }

    public static int getPx(Context context, int dp) {
        float deviceDs = 0;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        deviceDs = dm.density;
        return (int) (dp * deviceDs);
    }

    private MyCardView[][] cardsMap = new MyCardView[MyConfig.LINES][MyConfig.LINES];
    private List<Point> emptyPoints = new ArrayList<Point>();
}
