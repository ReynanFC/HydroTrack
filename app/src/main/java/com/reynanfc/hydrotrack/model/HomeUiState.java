package com.reynanfc.hydrotrack.model;

import java.util.List;

public class HomeUiState {

    public final int consumedMl;
    public final int goalMl;
    public final List<RegisterWater> records;

    public HomeUiState(int consumedMl, int goalMl, List<RegisterWater> records) {
        this.consumedMl = consumedMl;
        this.goalMl = goalMl;
        this.records = records;
    }

    public int percent() {
        if (goalMl == 0) return 0;
        return consumedMl * 100 / goalMl;
    }

    public boolean isGoalReached() {
        return goalMl > 0 && consumedMl >= goalMl;
    }
}
