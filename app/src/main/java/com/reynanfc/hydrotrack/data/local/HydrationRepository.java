package com.reynanfc.hydrotrack.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class HydrationRepository {

    private static final String KEY_CONSUMED = "consumed";
    private static final String KEY_GOAL = "goal";
    private final SharedPreferences prefs;

    public HydrationRepository(Context context) {
        prefs = context.getSharedPreferences("hydration", Context.MODE_PRIVATE);
    }

    public int getConsumed() { return prefs.getInt(KEY_CONSUMED, 0); }
    public int getGoal() { return prefs.getInt(KEY_GOAL, 2000); }

    public void addConsumed(int ml) {
        prefs.edit().putInt(KEY_CONSUMED, getConsumed() + ml).apply();
    }

    public void setGoal(int ml) {
        prefs.edit().putInt(KEY_GOAL, ml).apply();
    }
}
