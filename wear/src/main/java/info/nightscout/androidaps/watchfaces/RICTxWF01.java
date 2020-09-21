package info.nightscout.androidaps.watchfaces;

import android.content.Intent;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;

import com.ustwo.clockwise.common.WatchMode;

import info.nightscout.androidaps.R;
import info.nightscout.androidaps.interaction.menus.MainMenuActivity;

public class RICTxWF01 extends BaseWatchFace {
    private long chartTapTime = 0;
    private long sgvTapTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutView = inflater.inflate(R.layout.activity_rictxwf01, null);
        performViewSetup();
    }

    @Override
    protected void onTapCommand(int tapType, int x, int y, long eventTime) {
        //if (x < 10) myTest();

        int extra = mSgv != null ? (mSgv.getRight() - mSgv.getLeft()) / 2 : 0;

        if (tapType == TAP_TYPE_TAP &&
                x >= chart.getLeft() &&
                x <= chart.getRight() &&
                y >= chart.getTop() &&
                y <= chart.getBottom()) {
            if (eventTime - chartTapTime < 800) {
                changeChartTimeframe();
            }
            chartTapTime = eventTime;
        } else if (tapType == TAP_TYPE_TAP &&
                x + extra >= mSgv.getLeft() &&
                x - extra <= mSgv.getRight() &&
                y >= mSgv.getTop() &&
                y <= mSgv.getBottom()) {
            if (eventTime - sgvTapTime < 800) {
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            sgvTapTime = eventTime;
        }


    }

    private void changeChartTimeframe() {
        int timeframe = Integer.parseInt(sharedPrefs.getString("chart_timeframe", "3"));
        timeframe = (timeframe % 5) + 1;
        sharedPrefs.edit().putString("chart_timeframe", "" + timeframe).commit();
    }

    @Override
    protected WatchFaceStyle getWatchFaceStyle() {
        return new WatchFaceStyle.Builder(this)
                .setAcceptsTapEvents(true)
                .setHideNotificationIndicator(false)
                .setShowUnreadCountIndicator(true)
                .build();
    }

    protected void setColorDark() {
        if (rawData.sgvLevel == 1) {
            mSgv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_highColor));
            mDirection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_highColor));
        } else if (rawData.sgvLevel == 0) {
            mSgv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_midColor));
            mDirection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_midColor));
        } else if (rawData.sgvLevel == -1) {
            mSgv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_lowColor));
            mDirection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_lowColor));
        }

        if (ageLevel == 1) {
            mTimestamp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_midColor));
        } else {
            mTimestamp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_TimestampOld));
        }

        if (rawData.batteryLevel == 1) {
            mUploaderBattery.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_midColor));
        } else {
            mUploaderBattery.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_uploaderBatteryEmpty));
        }

        /* not be implement
        if (loopLevel == 1) {
            mLoop.setBackgroundResource(R.drawable.loop_green_25);
        } else {
            mLoop.setBackgroundResource(R.drawable.loop_red_25);
        }
        */

        if (chart != null) {
            highColor = ContextCompat.getColor(getApplicationContext(), R.color.dark_highColor);
            lowColor = ContextCompat.getColor(getApplicationContext(), R.color.dark_lowColor);
            midColor = ContextCompat.getColor(getApplicationContext(), R.color.dark_midColor);
            gridColor = ContextCompat.getColor(getApplicationContext(), R.color.dark_gridColor);
            basalBackgroundColor = ContextCompat.getColor(getApplicationContext(), R.color.basal_dark);
            basalCenterColor = ContextCompat.getColor(getApplicationContext(), R.color.basal_light);
            pointSize = 1;
            setupCharts();
        }


    }

    protected void setColorLowRes() {
        setColorDark();
    }

    protected void setColorBright() {
        if (getCurrentWatchMode() == WatchMode.INTERACTIVE) {
            setColorDark();
        } else {
            setColorDark();
        }
    }

}
