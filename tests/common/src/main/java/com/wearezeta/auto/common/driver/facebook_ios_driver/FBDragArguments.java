package com.wearezeta.auto.common.driver.facebook_ios_driver;


public class FBDragArguments {
    private double fromX;
    private double fromY;
    private double toX;
    private double toY;
    private double durationSeconds;

    public FBDragArguments(double fromX, double fromY, double toX, double toY, double durationSeconds) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        DurationValidator.validate(durationSeconds);
        this.durationSeconds = durationSeconds;
    }

    public double getFromX() {
        return fromX;
    }

    public double getFromY() {
        return fromY;
    }

    public double getToX() {
        return toX;
    }

    public double getToY() {
        return toY;
    }

    public double getDurationSeconds() {
        return durationSeconds;
    }
}