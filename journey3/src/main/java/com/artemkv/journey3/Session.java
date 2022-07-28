package com.artemkv.journey3;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Session {
    @SerializedName("t")
    private final String t = "stail";
    @SerializedName("v")
    private final String v = "1.1.0";

    @SerializedName("id")
    private final String id;
    @SerializedName("acc")
    private final String accountId;
    @SerializedName("aid")
    private final String appId;
    @SerializedName("version")
    private final String version;
    @SerializedName("is_release")
    private final boolean isRelease;

    @SerializedName("start")
    private final Instant start;
    @SerializedName("end")
    private Instant end;
    @SerializedName("since")
    private Instant since;

    @SerializedName("fst_launch")
    private boolean firstLaunch = false;

    @SerializedName("prev_stage")
    private Stage prevStage = Stage.newUser();
    @SerializedName("new_stage")
    private Stage newStage = Stage.newUser();

    @SerializedName("has_error")
    private boolean hasError = false;
    @SerializedName("has_crash")
    private boolean hasCrash = false;

    @SerializedName("evts")
    private final Map<String, Integer> eventCounts = new HashMap<>();
    @SerializedName("evt_seq")
    private final List<String> eventSequence = new ArrayList<>();

    public Session(
            String id,
            String accountId,
            String appId,
            String version,
            boolean isRelease,
            Instant start) {
        if (id == null)
            throw new IllegalArgumentException("id");
        if (accountId == null)
            throw new IllegalArgumentException("accountId");
        if (appId == null)
            throw new IllegalArgumentException("appId");

        this.id = id;
        this.accountId = accountId;
        this.appId = appId;
        this.version = version;
        this.isRelease = isRelease;

        this.start = start;
        this.end = start;
        this.since = start;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAppId() {
        return appId;
    }

    public String getVersion() {
        return version;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Instant getSince() {
        return since;
    }

    public void setSince(Instant since) {
        this.since = since;
    }

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch) {
        this.firstLaunch = firstLaunch;
    }

    public Stage getPrevStage() {
        return prevStage;
    }

    public void setPrevStage(Stage prevStage) {
        this.prevStage = prevStage;
    }

    public Stage getNewStage() {
        return newStage;
    }

    public void setNewStage(Stage newStage) {
        this.newStage = newStage;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasCrash() {
        return hasCrash;
    }

    public void setHasCrash(boolean hasCrash) {
        this.hasCrash = hasCrash;
    }

    public Map<String, Integer> getEventCounts() {
        return eventCounts;
    }

    public List<String> getEventSequence() {
        return eventSequence;
    }
}
