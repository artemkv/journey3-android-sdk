package com.artemkv.journey3.app;

import android.content.Context;

import com.artemkv.journey3.Journey;

public final class JourneyConnectorProvider {
    private static Journey journey;

    private JourneyConnectorProvider() {
    }

    public static Journey getInstance() {
        return journey;
    }

    public static synchronized void initialize(Context context) {
        if (journey == null) {
            journey = new Journey(
                    "e04b43c9-69c1-4172-9dfd-a3ef1aa17d5e",
                    "b83c45c3-9c4f-4792-b2f3-b0c59a66c744",
                    BuildConfig.VERSION_NAME,
                    !BuildConfig.DEBUG);
            journey.startSession(context);
        }
    }
}
