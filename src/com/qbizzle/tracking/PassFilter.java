package com.qbizzle.tracking;

// duration is going to be minutes not days
public class PassFilter {

    public enum FilterParameter {
        MINIMUM_HEIGHT, MINIMUM_DURATION, MINIMUM_HEIGHT_DURATION
    }

    private double minimumHeight;
    private double minimumDuration;
    private FilterParameter filterType;


    public PassFilter() {
        this.minimumHeight = 0;
        this.minimumDuration = 0;
        this.filterType = FilterParameter.MINIMUM_HEIGHT_DURATION;
    }

    public double getMinimumHeight() {
        return minimumHeight;
    }

    public PassFilter setMinimumHeight(double minimumHeight) {
        this.minimumHeight = minimumHeight;
        return this;
    }

    public double getMinimumDuration() {
        return minimumDuration;
    }

    public PassFilter setMinimumDuration(double minimumDuration) {
        this.minimumDuration = minimumDuration;
        return this;
    }

    public FilterParameter getFilterType() {
        return filterType;
    }

    public PassFilter setFilterType(FilterParameter filterType) {
        this.filterType = filterType;
        return this;
    }

}
