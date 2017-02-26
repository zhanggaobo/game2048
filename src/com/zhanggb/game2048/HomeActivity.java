package com.zhanggb.game2048;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.feiwo.view.FwBannerManager;
import com.feiwo.view.FwRecevieAdListener;
import com.zhanggb.game2048.view.MyAnimLayer;
import com.zhanggb.game2048.view.MyGameView;

public class HomeActivity extends BaseActivity {


    private int score = 0;
    private TextView tvScore, tvBestScore;
    private LinearLayout root = null;
    private LinearLayout newGame;
    private MyGameView gameView;
    private MyAnimLayer animLayer = null;
    public static String APPKEY_FEIWO = "d660eec0c442bdea55f0ab78d6517fda";

    private static HomeActivity mainActivity = null;

    public static HomeActivity getHomeActivity() {
        return mainActivity;
    }

    public static final String SP_KEY_BEST_SCORE = "bestScore";

    private boolean isHighest = false;

    public HomeActivity() {
        mainActivity = this;
    }

    @Override
    protected void doOnCreate() {
        setContentView(R.layout.home_activity);
        this.context = HomeActivity.this;
        FwBannerManager.init(context, APPKEY_FEIWO);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBanner();
    }

    private void initView() {
        root = (LinearLayout) findViewById(R.id.container);

        tvScore = (TextView) findViewById(R.id.home_current_text);
        tvBestScore = (TextView) findViewById(R.id.home_record_text);

        gameView = (MyGameView) findViewById(R.id.gameView);

        newGame = (LinearLayout) findViewById(R.id.home_reset_linear);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.startGame();
            }
        });

        animLayer = (MyAnimLayer) findViewById(R.id.animLayer);
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    private void initBanner() {
        //启动方式2  能够获取banner的宽度 高度
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.feiwo_banner_ll);
        FwBannerManager.setParentView(layout,
                new FwRecevieAdListener() {

                    @Override
                    public void onSucessedRecevieAd(int arg0, int arg1) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(context, "width = " + arg0 + " height" + arg1, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailedToRecevieAd() {
                        // TODO Auto-generated method stub
                    }
                });

    }


    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();

        if (getBestScore() > 0 && score > getBestScore()) {
            isHighest = true;
        } else {
            isHighest = false;
        }
        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    public void saveBestScore(int s) {
        SharedPreferences.Editor e = getPreferences(MODE_PRIVATE).edit();
        e.putInt(SP_KEY_BEST_SCORE, s);
        e.commit();
    }

    public int getBestScore() {
        return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    public MyAnimLayer getAnimLayer() {
        return animLayer;
    }

    public boolean isHighest() {
        return isHighest;
    }
}
