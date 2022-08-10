package net.artemkv.journey3;

import android.content.Context;
import android.util.Log;

import java.time.Instant;
import java.util.List;

import static net.artemkv.journey3.DateTimeUtil.isSameDay;
import static net.artemkv.journey3.DateTimeUtil.isSameHour;
import static net.artemkv.journey3.DateTimeUtil.isSameMonth;
import static net.artemkv.journey3.DateTimeUtil.isSameYear;

public final class Journey {
    private static final int MAX_SEQ_LENGTH = 100;

    private final String accountId;
    private final String appId;
    private final String version;
    private final boolean isRelease;

    private final SessionStorage sessionStorage = new SessionStorage();

    private Session currentSession;
    private final Object lock = new Object();

    public Journey(String accountId, String appId, String version, boolean isRelease) {
        if (accountId == null)
            throw new IllegalArgumentException("accountId");
        if (appId == null)
            throw new IllegalArgumentException("appId");

        this.accountId = accountId;
        this.appId = appId;
        this.version = version;
        this.isRelease = isRelease;
    }

    public synchronized void startSession(Context context) {
        try {
            // start new session
            SessionHeader header = new SessionHeader(
                    this.accountId, this.appId, this.version, this.isRelease);
            currentSession = new Session(
                    header.getId(), accountId, appId, version, isRelease, header.getStart());
            Log.i("net.artemkv.journey3", "Started new session " + currentSession.getId());

            // report previous session
            final Session session = sessionStorage.getSession(context);
            if (session != null) {
                Log.i("net.artemkv.journey3", "Report the end of the previous session");
                Connector.reportSession(session);
            }

            // update current session based on the previous one
            if (session == null) {
                header.setFirstLaunch(true);
                currentSession.setFirstLaunch(true);

                header.setFirstLaunchThisHour(true);
                header.setFirstLaunchToday(true);
                header.setFirstLaunchThisMonth(true);
                header.setFirstLaunchThisYear(true);
                header.setFirstLaunchThisVersion(true);
            } else {
                Instant today = Instant.now();
                Instant lastSessionStart = session.getStart();

                if (!isSameHour(lastSessionStart, today)) {
                    header.setFirstLaunchThisHour(true);
                }
                if (!isSameDay(lastSessionStart, today)) {
                    header.setFirstLaunchToday(true);
                }
                if (!isSameMonth(lastSessionStart, today)) {
                    header.setFirstLaunchThisMonth(true);
                }
                if (!isSameYear(lastSessionStart, today)) {
                    header.setFirstLaunchThisYear(true);
                }
                if (!session.getVersion().equals(version)) {
                    header.setFirstLaunchThisVersion(true);
                }

                currentSession.setPrevStage(session.getNewStage());
                currentSession.setNewStage(session.getNewStage());
                header.setPrevStage(session.getNewStage());

                header.setSince(session.getSince());
                currentSession.setSince(session.getSince());
            }

            // save current session
            sessionStorage.saveSession(currentSession, context);

            // report the new session (header)
            Log.i("net.artemkv.journey3", "Report the start of a new session");
            Connector.reportSessionHeader(header);
        } catch (Exception ex) {
            Log.e("net.artemkv.journey3", "Failed to initialize Journey: " + ex.getMessage(), ex);
        }
    }

    public synchronized void reportEvent(String eventName, Context context) {
        reportEvent(eventName, false, false, false, context);
    }

    public synchronized void reportEvent(String eventName, boolean isCollapsible, Context context) {
        reportEvent(eventName, isCollapsible, false, false, context);
    }

    public synchronized void reportError(String eventName, Context context) {
        reportEvent(eventName, false, true, false, context);
    }

    public synchronized void reportCrash(String eventName, Context context) {
        reportEvent(eventName, false, true, true, context);
    }

    private void reportEvent(String eventName, boolean isCollapsible, boolean isError, boolean isCrash, Context context) {
        if (currentSession == null) {
            Log.i("net.artemkv.journey3", "Cannot update session. Journey have not been initialized.");
            return;
        }

        try {
            // count events
            currentSession.getEventCounts().compute(eventName,
                    (k, v) -> (v == null) ? 1 : v + 1);

            // set error
            if (isError) {
                currentSession.setHasError(true);
            }
            if (isCrash) {
                currentSession.setHasCrash(true);
            }

            // sequence events
            String seqEventName = isCollapsible ? '(' + eventName + ')' : eventName;
            synchronized (lock) {
                List<String> seq = currentSession.getEventSequence();
                if (seq.size() < MAX_SEQ_LENGTH) {
                    if (seq.isEmpty() || !seq.get(seq.size() - 1).equals(seqEventName) || !isCollapsible) {
                        seq.add(seqEventName);
                    }
                }
            }

            // update endtime
            currentSession.setEnd(Instant.now());

            // save session
            sessionStorage.saveSession(currentSession, context);
        } catch (Exception ex) {
            Log.e("net.artemkv.journey3", "Cannot update session: " + ex.getMessage(), ex);
        }
    }

    public synchronized void reportStageTransition(int stage, String stageName, Context context) {
        if (currentSession == null) {
            Log.i("net.artemkv.journey3", "Cannot update session. Journey have not been initialized.");
            return;
        }

        if (stage < 1 || stage > 10) {
            throw new IllegalArgumentException(
                    String.format("Invalid value %d for stage, must be between 1 and 10", stage));
        }

        try {
            if (currentSession.getNewStage().getStage() < stage) {
                currentSession.setNewStage(new Stage(stage, stageName));
            }

            // save session
            sessionStorage.saveSession(currentSession, context);
        } catch (Exception ex) {
            Log.e("net.artemkv.journey3", "Cannot update session: " + ex.getMessage(), ex);
        }
    }
}
