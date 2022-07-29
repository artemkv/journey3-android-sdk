package net.artemkv.journey3;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.UUID;

final class SessionHeader {
    @SerializedName("t")
    private final String t = "shead";
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
    @SerializedName("since")
    private Instant since;

    @SerializedName("fst_launch")
    private boolean firstLaunch = false;
    @SerializedName("fst_launch_hour")
    private boolean firstLaunchThisHour = false;
    @SerializedName("fst_launch_day")
    private boolean firstLaunchToday = false;
    @SerializedName("fst_launch_month")
    private boolean firstLaunchThisMonth = false;
    @SerializedName("fst_launch_year")
    private boolean firstLaunchThisYear = false;
    @SerializedName("fst_launch_version")
    private boolean firstLaunchThisVersion = false;

    @SerializedName("prev_stage")
    private Stage prevStage = Stage.newUser();

    public SessionHeader(
            String accountId,
            String appId,
            String version,
            boolean isRelease) {
        this.id = UUID.randomUUID().toString();

        if (accountId == null)
            throw new IllegalArgumentException("accountId");
        if (appId == null)
            throw new IllegalArgumentException("appId");

        this.accountId = accountId;
        this.appId = appId;
        this.version = version;
        this.isRelease = isRelease;

        this.start = Instant.now();
        this.since = Instant.now();
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

    public boolean isFirstLaunchThisHour() {
        return firstLaunchThisHour;
    }

    public void setFirstLaunchThisHour(boolean firstLaunchThisHour) {
        this.firstLaunchThisHour = firstLaunchThisHour;
    }

    public boolean isFirstLaunchToday() {
        return firstLaunchToday;
    }

    public void setFirstLaunchToday(boolean firstLaunchToday) {
        this.firstLaunchToday = firstLaunchToday;
    }

    public boolean isFirstLaunchThisMonth() {
        return firstLaunchThisMonth;
    }

    public void setFirstLaunchThisMonth(boolean firstLaunchThisMonth) {
        this.firstLaunchThisMonth = firstLaunchThisMonth;
    }

    public boolean isFirstLaunchThisYear() {
        return firstLaunchThisYear;
    }

    public void setFirstLaunchThisYear(boolean firstLaunchThisYear) {
        this.firstLaunchThisYear = firstLaunchThisYear;
    }

    public boolean isFirstLaunchThisVersion() {
        return firstLaunchThisVersion;
    }

    public void setFirstLaunchThisVersion(boolean firstLaunchThisVersion) {
        this.firstLaunchThisVersion = firstLaunchThisVersion;
    }

    public Stage getPrevStage() {
        return prevStage;
    }

    public void setPrevStage(Stage prevStage) {
        this.prevStage = prevStage;
    }
}
