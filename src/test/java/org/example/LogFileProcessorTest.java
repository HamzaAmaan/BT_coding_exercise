package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogFileProcessorTest
{
    @Test
    void throwsException_whenNoCommandLineArgumentPassed()
    {
        // WITH
        final String expectedMessage = "Please provide the log file path as an argument.";
        // WHEN
        final IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> LogFileProcessor.main(new String[0]));
        // THEN
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void throwsException_whenNotPassedLogFile()
    {
        // WITH
        final String filePath = "file.ext";
        final String expectedMessage = "The file path provided is not for a log file.";
        // WHEN
        final IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> LogFileProcessor.main(new String[]{filePath}));
        // THEN
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    void throwsException_whenLogFileDoesNotExist()
    {
        // WITH
        final String filePath = ".invalidFile.log";
        final String expectedMessage = "Log file does not exist.";
        // WHEN
        final IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> LogFileProcessor.main(new String[]{filePath}));
        // THEN
        assertEquals(expectedMessage, exception.getMessage());
    }
}