package net.sunniwell.app.linktaro.tools;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {
    public static final int RECALLED_START_TIMER_ERRO = 1;
    public static final int TIMER_NOT_INIT_ERRO = 0;
    private int delay = 1000;
    /* access modifiers changed from: private */
    public int endTime = -1;
    /* access modifiers changed from: private */
    public boolean isPauseTimer;
    private boolean isStartTimer;
    private Timer mTimer = new Timer(true);
    /* access modifiers changed from: private */
    public TimerCallBack mTimerCallBack;
    /* access modifiers changed from: private */
    public int preTime = -1;
    private int starTime = -1;
    TimerTask task = new TimerTask() {
        public void run() {
            if (!TimerUtil.this.isPauseTimer) {
                TimerUtil timerUtil = TimerUtil.this;
                timerUtil.preTime = timerUtil.preTime + 1;
            }
            if (TimerUtil.this.preTime == TimerUtil.this.endTime) {
                if (TimerUtil.this.mTimerCallBack != null) {
                    TimerUtil.this.mTimerCallBack.noticeIdEndTime();
                }
                TimerUtil.this.stopTimer();
            }
        }
    };

    public interface TimerCallBack {
        void noticeIdEndTime();

        void onErro(int i);

        void onPaues(int i, int i2, int i3);

        void onStartTimer();

        void onStopTimer();

        void onTimeChanged(int i, int i2, int i3);
    }

    public TimerUtil() {
    }

    public TimerUtil(int startTime, int endTime2) {
        this.starTime = startTime;
        this.endTime = endTime2;
        this.preTime = this.starTime;
    }

    public void setTime(int startT, int endT) {
        if (startT < endT) {
            this.starTime = startT;
            this.preTime = startT;
            this.endTime = endT;
            if (this.mTimerCallBack != null) {
                this.mTimerCallBack.onTimeChanged(this.starTime, this.endTime, this.preTime);
            }
        } else if (this.mTimerCallBack != null) {
            this.mTimerCallBack.onErro(0);
        }
    }

    public boolean startTimer() {
        if (this.mTimerCallBack != null) {
            this.mTimerCallBack.onStartTimer();
        }
        if (this.starTime == -1 || this.endTime == -1) {
            if (this.mTimerCallBack == null) {
                return false;
            }
            this.mTimerCallBack.onErro(0);
            return false;
        } else if (!this.isStartTimer) {
            if (this.mTimer == null) {
                this.mTimer = new Timer(true);
            }
            this.isStartTimer = true;
            this.mTimer.schedule(this.task, (long) this.delay);
            return true;
        } else if (this.mTimerCallBack == null) {
            return false;
        } else {
            this.mTimerCallBack.onErro(1);
            return false;
        }
    }

    public void stopTimer() {
        if (this.isStartTimer) {
            if (this.mTimerCallBack != null) {
                this.mTimerCallBack.onStopTimer();
            }
            this.mTimer.cancel();
            this.isStartTimer = false;
            this.isPauseTimer = false;
            this.delay = 1000;
            this.starTime = -1;
            this.endTime = -1;
            this.preTime = -1;
        }
    }

    public void pauseTime() {
        if (this.isStartTimer) {
            this.isPauseTimer = true;
            if (this.mTimerCallBack != null) {
                this.mTimerCallBack.onPaues(this.starTime, this.endTime, this.preTime);
            }
        }
    }

    public void resumeTime() {
        this.isPauseTimer = false;
    }

    public int getCurTime() {
        return this.preTime;
    }

    public void setTimerCadllBackListener(TimerCallBack callBack) {
        if (this.mTimerCallBack == null) {
            this.mTimerCallBack = callBack;
        }
    }
}
