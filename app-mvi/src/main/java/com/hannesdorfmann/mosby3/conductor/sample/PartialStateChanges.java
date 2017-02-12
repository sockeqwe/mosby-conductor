package com.hannesdorfmann.mosby3.conductor.sample;

/**
 * Created by flisar on 08.02.2017.
 */

public interface PartialStateChanges
{
    ViewState computeNewState(ViewState previousState);

    final class LoadingData implements PartialStateChanges
    {
        @Override
        public String toString()
        {
            return "LoadingData{}";
        }

        @Override
        public ViewState computeNewState(ViewState previousState)
        {
            // Merge der States
            return previousState.toBuilder()
                    .loadingData(true)
                    .data(null)
                    .build();
        }
    }

    final class DataLoaded implements PartialStateChanges
    {
        private String mData;

        public DataLoaded(String data)
        {
            mData = data;
        }

        @Override
        public String toString()
        {
            return "DataLoaded{}";
        }

        @Override
        public ViewState computeNewState(ViewState previousState)
        {
            // Merge der States
            return previousState.toBuilder()
                    .loadingData(false)
                    .data(mData)
                    .build();
        }
    }
}
