package org.example.utils;

import org.example.model.SessionInfo;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class SessionInfoUtils
{
    public static LocalTime getEarliestTime(List<LocalTime> starts, List<LocalTime> ends)
    {
        return Stream.of(starts, ends).flatMap(Collection::stream).min(LocalTime::compareTo).orElse(LocalTime.MIN);
    }

    public static LocalTime getLatestTime(List<LocalTime> starts, List<LocalTime> ends)
    {
        return Stream.of(starts, ends).flatMap(Collection::stream).max(LocalTime::compareTo).orElse(LocalTime.MIN);
    }

    public static void appendToSessionInfo(final LocalTime start, final LocalTime end, final SessionInfo sessionInfo)
    {
        final Duration totalDuration = sessionInfo.getTotalDuration() == null ? Duration.ZERO : sessionInfo.getTotalDuration();
        sessionInfo.setTotalDuration(totalDuration.plus(Duration.between(start, end)));
        sessionInfo.setSessionCount(sessionInfo.getSessionCount() + 1);
    }
}
