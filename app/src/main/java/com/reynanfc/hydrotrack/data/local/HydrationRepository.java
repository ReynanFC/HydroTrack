package com.reynanfc.hydrotrack.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.reynanfc.hydrotrack.model.RegisterWater;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HydrationRepository {

    private static final String KEY_CONSUMED = "consumed";
    private static final String KEY_GOAL = "goal";
    private static final String KEY_HYDRATION = "hydration";
    private static final String KEY_RECORDS = "records";
    private static final String KEY_CYCLE_DATE = "cycle_date";

    private final SharedPreferences prefs;

    public HydrationRepository(Context context) {
        prefs = context.getSharedPreferences(KEY_HYDRATION, Context.MODE_PRIVATE);
    }

    public int getConsumed() {
        resetIfNewDay();
        return prefs.getInt(KEY_CONSUMED, 0);
    }

    public int getGoal() { return prefs.getInt(KEY_GOAL, 2000); }

    public List<RegisterWater> getRecords() {
        resetIfNewDay();
        return readRecords();
    }

    public void addConsumed(int ml) {
        List<RegisterWater> records = getRecords();
        records.add(0, new RegisterWater(ml, LocalDateTime.now()));
        saveRecords(records);
    }

    public void removeRecord(int position) {
        List<RegisterWater> records = getRecords();
        if (position < 0 || position >= records.size()) return;

        records.remove(position);
        saveRecords(records);
    }

    public void setGoal(int ml) {
        prefs.edit().putInt(KEY_GOAL, ml).apply();
    }

    private void resetIfNewDay() {
        String today = LocalDate.now().toString();
        if (today.equals(prefs.getString(KEY_CYCLE_DATE, null))) return;

        prefs.edit()
                .putString(KEY_CYCLE_DATE, today)
                .remove(KEY_RECORDS)
                .putInt(KEY_CONSUMED, 0)
                .apply();
    }

    private List<RegisterWater> readRecords() {
        return RegisterWaterJsonMapper.fromJson(prefs.getString(KEY_RECORDS, "[]"));
    }

    private void saveRecords(List<RegisterWater> records) {
        int consumed = 0;
        for (RegisterWater record : records) {
            consumed += record.consumeMl();
        }

        prefs.edit()
                .putString(KEY_RECORDS, RegisterWaterJsonMapper.toJson(records))
                .putInt(KEY_CONSUMED, consumed)
                .apply();
    }
}
