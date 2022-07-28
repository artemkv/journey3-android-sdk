package com.artemkv.journey3;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

final class Stage {
    @SerializedName("ts")
    private final Instant ts;
    @SerializedName("stage")
    private final int stage;
    @SerializedName("name")
    private final String name;

    public Stage(int stage, String name) {
        this.ts = Instant.now();
        this.stage = stage;
        this.name = name;
    }

    public static Stage newUser() {
        return new Stage(1, "new_user");
    }

    public Instant getTs() {
        return ts;
    }

    public int getStage() {
        return stage;
    }

    public String getName() {
        return name;
    }
}
