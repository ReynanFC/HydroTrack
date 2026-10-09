package com.reynanfc.hydrotrack.model;

public class HomeUiState {

    public final int consumedMl;
    public final int goalMl;

    public HomeUiState(int consumedMl, int goalMl) {
        this.consumedMl = consumedMl;
        this.goalMl = goalMl;
    }

    public int percent() {
        if (goalMl == 0) return 0;
        return Math.min(100, consumedMl * 100 / goalMl);
    }
}
