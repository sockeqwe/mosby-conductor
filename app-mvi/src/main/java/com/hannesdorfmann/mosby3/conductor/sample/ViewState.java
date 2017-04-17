package com.hannesdorfmann.mosby3.conductor.sample;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * Created by Michael on 12.02.2017.
 */

@AutoValue
public abstract class ViewState {

    public abstract boolean loadingData();
    @Nullable public abstract String data();

    public abstract Builder toBuilder();

    public static ViewState create(boolean loadingData, String data) {
        return builder()
                .loadingData(loadingData)
                .data(data)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewState.Builder()
                .loadingData(false)
                .data(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder loadingData(boolean loadingData);

        public abstract Builder data(String data);

        public abstract ViewState build();
    }
}
