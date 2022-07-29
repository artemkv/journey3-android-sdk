package net.artemkv.journey3;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SessionStorage {
    private static class SavedSessionData {
        String id;
        String accountId;
        String appId;
        String version;
        boolean isRelease;

        Instant start;
        Instant end;
        Instant since;

        boolean firstLaunch = false;

        Stage prevStage = Stage.newUser();
        Stage newStage = Stage.newUser();

        boolean hasError = false;
        boolean hasCrash = false;

        Map<String, Integer> eventCounts = new HashMap<>();
        List<String> eventSequence = new ArrayList<>();
    }

    private static final String SessionFileName = "journey.session";
    private static final String SessionVersion = "1.0";

    public SessionStorage() {
    }

    public synchronized Session getSession(Context context) {
        String fileName = SessionFileName + SessionVersion + ".json";
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            return null;
        }

        Gson gson = GsonFactory.Gson();
        InputStream stream = null;
        BufferedReader reader = null;
        try {
            FileInputStream input = context.openFileInput(fileName);
            stream = new BufferedInputStream(input);
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            SavedSessionData savedData = gson.fromJson(json, SavedSessionData.class);
            if (savedData != null) {
                return fromSavedDate(savedData);
            } else {
                // File was empty
                return null;
            }
        } catch (JsonSyntaxException e) {
            // Ignore
            Log.e("net.artemkv.journey3", "Something failed badly. Please report this issue", e);
        } catch (IOException e) {
            // Ignore
            Log.e("net.artemkv.journey3", "Something failed badly. Please report this issue", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing reader", e);
            }

            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing stream", e);
            }
        }

        // Was never saved
        return null;
    }

    public synchronized void saveSession(Session session, Context context) {
        String fileName = SessionFileName + SessionVersion + ".json";

        SavedSessionData savedData = toSavedData(session);

        Gson gson = GsonFactory.Gson();
        String json = gson.toJson(savedData, SavedSessionData.class);

        OutputStream stream = null;
        OutputStreamWriter writer = null;
        try {
            FileOutputStream output = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            stream = new BufferedOutputStream(output);
            writer = new OutputStreamWriter(stream);
            writer.write(json);
            stream.flush();
        } catch (IOException e) {
            // Ignore
            Log.e("net.artemkv.journey3", "Something failed badly. Please report this issue", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing writer", e);
            }

            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing stream", e);
            }
        }
    }

    private static Session fromSavedDate(SavedSessionData savedData) {
        Session session = new Session(
                savedData.id,
                savedData.accountId,
                savedData.appId,
                savedData.version,
                savedData.isRelease,
                savedData.start);
        session.setEnd(savedData.end);
        session.setSince(savedData.since);
        session.setFirstLaunch(savedData.firstLaunch);
        session.setPrevStage(savedData.prevStage);
        session.setNewStage(savedData.newStage);
        session.setHasError(savedData.hasError);
        session.setHasCrash(savedData.hasCrash);
        session.getEventCounts().putAll(savedData.eventCounts);
        session.getEventSequence().addAll(savedData.eventSequence);
        return session;
    }

    private SavedSessionData toSavedData(Session session) {
        SavedSessionData savedData = new SavedSessionData();
        savedData.id = session.getId();
        savedData.accountId = session.getAccountId();
        savedData.appId = session.getAppId();
        savedData.version = session.getVersion();
        savedData.isRelease = session.isRelease();
        savedData.start = session.getStart();
        savedData.end = session.getEnd();
        savedData.since = session.getSince();
        savedData.firstLaunch = session.isFirstLaunch();
        savedData.prevStage = session.getPrevStage();
        savedData.newStage = session.getNewStage();
        savedData.hasError = session.isHasError();
        savedData.hasCrash = session.isHasCrash();
        savedData.eventCounts = session.getEventCounts();
        savedData.eventSequence = session.getEventSequence();
        return savedData;
    }
}
