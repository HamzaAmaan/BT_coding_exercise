package org.example;

import lombok.extern.log4j.Log4j2;
import org.example.enumAndConsts.Action;
import org.example.model.SessionInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.example.enumAndConsts.ErrorMessage.*;
import static org.example.utils.LineValidationUtils.*;
import static org.example.utils.SessionInfoUtils.*;

@Log4j2
public class LogFileProcessor
{
    private static final String FILE_EXTENSION = ".log";

    public static void main(String[] args)
    {
        if (args.length == 0 || Objects.equals(args[0], ""))
        {
            throw new IllegalArgumentException(MISSING_COMMAND_LINE_ARGUMENT);
        }
        final String filePath = args[0];
        if (!filePath.toLowerCase().endsWith(FILE_EXTENSION))
        {
            throw new IllegalArgumentException(INVALID_FILE_EXTENSION);
        }

        final Map<String, SessionInfo> userSessions = processLogFile(filePath);
        for (Map.Entry<String, SessionInfo> entry : userSessions.entrySet())
        {
            final String user = entry.getKey();
            final SessionInfo sessionInfo = entry.getValue();
            System.out.printf("%s %d %d%n", user, sessionInfo.getSessionCount(), (int) sessionInfo.getTotalDuration().toSeconds());
        }
    }

    private static Map<String, SessionInfo> processLogFile(final String filePath)
    {
        final Map<String, SessionInfo> userSessions = new HashMap<>();
        final Map<String, LinkedList<LocalTime>> startTimes = new HashMap<>();
        final Map<String, LinkedList<LocalTime>> endTimes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> processLogLine(line, userSessions, startTimes, endTimes));
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(MISSING_FILE);
        }

        final List<LocalTime> allStartTimes = startTimes.values().stream().flatMap(Collection::stream).toList();
        final List<LocalTime> allEndTimes = endTimes.values().stream().flatMap(Collection::stream).toList();

        final LocalTime earliestTime = getEarliestTime(allStartTimes, allEndTimes);
        final LocalTime latestTime = getLatestTime(allStartTimes, allEndTimes);

        userSessions.forEach((user, sessionInfo) ->
                updateSessionInfo(startTimes.getOrDefault(user, new LinkedList<>()),
                        endTimes.getOrDefault(user, new LinkedList<>()),
                        earliestTime, latestTime, sessionInfo));

        return userSessions;
    }

    private static void processLogLine(String line,
                                       Map<String, SessionInfo> userSessions,
                                       Map<String, LinkedList<LocalTime>> startTimes,
                                       Map<String, LinkedList<LocalTime>> endTimes)
    {
        final String[] parts = line.split(LINE_SPLITTER);
        if (parts.length != 3)
        {
            log.debug(String.format(VALIDATION_ERROR, line, "Exactly 3 components required"));
            return;
        }
        final String timeStr = parts[0];
        final String user = parts[1];
        final String action = parts[2];
        if (!isAlphanumeric(user))
        {
            log.debug(String.format(VALIDATION_ERROR, line, "Invalid username"));
            return;
        }

        try
        {
            final LocalTime time = parseTime(timeStr);
            switch (Action.valueOf(action.toUpperCase()))
            {
                case START -> startTimes.computeIfAbsent(user, k -> new LinkedList<>()).add(time);
                case END -> endTimes.computeIfAbsent(user, k -> new LinkedList<>()).add(time);
            }
        }
        catch (DateTimeParseException e)
        {
            log.debug(String.format(VALIDATION_ERROR, line, "Invalid time format. " + e.getMessage()));
            return;
        }
        catch (IllegalArgumentException e)
        {
            log.debug(String.format(VALIDATION_ERROR, line, "Invalid Start or End marker"));
            return;
        }

        if (!userSessions.containsKey(user))
        {
            userSessions.put(user, new SessionInfo());
        }
    }

    private static void updateSessionInfo(final LinkedList<LocalTime> startTimes,
                                          final LinkedList<LocalTime> endTimes,
                                          final LocalTime earliestTime,
                                          final LocalTime latestTime,
                                          final SessionInfo sessionInfo)
    {
        while (!startTimes.isEmpty() && !endTimes.isEmpty())
        {
            final LocalTime start = startTimes.pollFirst();
            LocalTime end = endTimes.pollFirst();
            while (end != null && end.isBefore(start))
            {
                appendToSessionInfo(earliestTime, end, sessionInfo);
                end = endTimes.pollFirst();
            }
            appendToSessionInfo(start, end, sessionInfo);
        }

        while (!startTimes.isEmpty())
        {
            final LocalTime start = startTimes.pollFirst();
            appendToSessionInfo(start, latestTime, sessionInfo);
        }

        while (!endTimes.isEmpty())
        {
            final LocalTime end = endTimes.pollFirst();
            appendToSessionInfo(earliestTime, end, sessionInfo);
        }
    }
}