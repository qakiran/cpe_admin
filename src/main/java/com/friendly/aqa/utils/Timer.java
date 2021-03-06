package com.friendly.aqa.utils;

import com.friendly.aqa.pageobject.BasePage;

public class Timer {
    private final long start;
    private final long delay;

    public Timer() {
        start = System.currentTimeMillis();
        delay = BasePage.IMPLICITLY_WAIT * 1000;
    }

    public Timer(long delayMillis) {
        start = System.currentTimeMillis();
        this.delay = delayMillis;
    }

    public boolean timeout() {
        return System.currentTimeMillis() - start > delay;
    }

    public int stop() {
        return (int) (System.currentTimeMillis() - start);
    }
}
