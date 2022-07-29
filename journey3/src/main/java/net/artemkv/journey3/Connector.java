package net.artemkv.journey3;

import android.util.Log;

import com.google.gson.Gson;

final class Connector {
    private final static String JOURNEY_BASE_URL = "https://journey3-ingest.artemkv.net:8060";

    public static void reportSessionHeader(SessionHeader header) {
        OnSuccess<Boolean> onSuccess = result -> {
            if (result) {
                Log.d("net.artemkv.journey3", "Session header sent");
                // Do nothing, everything went well
            } else {
                Log.d("net.artemkv.journey3", "Error sending session header to Journey");
                // Could not send the session header
                // There is no retry in this case - this is a current limitation
            }
        };

        OnError onError = e -> Log.e("net.artemkv.journey3", "Something failed badly. Please report this issue", e);

        Gson gson = GsonFactory.Gson();
        // TODO: try to stream
        String json = gson.toJson(header, SessionHeader.class);

        Log.v("net.artemkv.journey3", json);

        AsyncWorkerTask.Worker<Boolean> worker = () ->
                HttpClient.trySend(JOURNEY_BASE_URL + "/session_head", json);

        new AsyncWorkerTask<>(worker, onSuccess, onError).execute();
    }

    public static void reportSession(Session session) {
        OnSuccess<Boolean> onSuccess = result -> {
            if (result) {
                Log.d("net.artemkv.journey3", "Session sent");
                // Do nothing, everything went well
            } else {
                Log.d("net.artemkv.journey3", "Error sending session to Journey");
                // Could not send the session header
                // There is no retry in this case - this is a current limitation
            }
        };

        OnError onError = e -> Log.e("net.artemkv.journey3", "Something failed badly. Please report this issue", e);

        Gson gson = GsonFactory.Gson();
        // TODO: try to stream
        String json = gson.toJson(session, Session.class);

        Log.v("net.artemkv.journey3", json);

        AsyncWorkerTask.Worker<Boolean> worker = () ->
                HttpClient.trySend(JOURNEY_BASE_URL + "/session_tail", json);

        new AsyncWorkerTask<>(worker, onSuccess, onError).execute();
    }
}
