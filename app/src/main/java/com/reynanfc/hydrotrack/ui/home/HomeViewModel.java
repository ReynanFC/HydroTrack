package com.reynanfc.hydrotrack.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.reynanfc.hydrotrack.data.local.HydrationRepository;
import com.reynanfc.hydrotrack.model.HomeUiState;

public class HomeViewModel extends AndroidViewModel {

    private final HydrationRepository repository;
    private final MutableLiveData<HomeUiState> uiState = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application app) {
        super(app);
        repository = new HydrationRepository(app);
        publish();
    }

    public LiveData<HomeUiState> getUiState() {
        return uiState;
    }

    public void refresh() {
        publish();
    }

    public void addWater(int ml) {
        repository.addConsumed(ml);
        publish();
    }

    public void removeRecord(int position) {
        repository.removeRecord(position);
        publish();
    }

    public void updateDailyGoal(int ml) {
        repository.setGoal(ml);
        publish();
    }

    public int getGoal() {
        return repository.getGoal();
    }

    private void publish() {
        uiState.setValue(new HomeUiState(
                repository.getConsumed(),
                repository.getGoal(),
                repository.getRecords()
        ));
    }
}
