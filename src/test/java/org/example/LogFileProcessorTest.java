package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LogFileProcessorTest
{
    @Nested
    class TestFileValidation
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
            final String filePath = "invalidFile.log";
            final String expectedMessage = "Log file does not exist.";
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class, () -> LogFileProcessor.main(new String[]{filePath}));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    class TestOutputs
    {
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        private final PrintStream originalOut = System.out;
        private final PrintStream originalErr = System.err;

        @BeforeEach
        public void setUpStreams()
        {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
        }

        @AfterEach
        public void restoreStreams()
        {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        @Test
        void usesEarliestTime_whenNoStart()
        {
            // WITH
            final String filePath = "src/test/resources/no_start.log";
            final String expectedOutput = formatReport("ALICE99", 1, 31)
                    + formatReport("CHARLIE", 1, 2);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertEquals(expectedOutput, outContent.toString());
        }

        @Test
        void usesLatestTime_whenNoEnd()
        {
            // WITH
            final String filePath = "src/test/resources/no_end.log";
            final String expectedOutput = formatReport("ALICE99", 1, 18)
                    + formatReport("CHARLIE", 1, 0);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertEquals(expectedOutput, outContent.toString());
        }

        @Test
        void ignoresLines_whenLessThan3Components()
        {
            // WITH
            final String filePath = "src/test/resources/less_than_3_components.log";
            final String expectedOutput = formatReport("ALICE99", 1, 0)
                    + formatReport("CHARLIE", 1, 0);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertTrue(outContent.toString().endsWith(expectedOutput));
        }

        @Test
        void ignoresLines_whenMoreThan3Components()
        {
            // WITH
            final String filePath = "src/test/resources/more_than_3_components.log";
            final String expectedOutput = formatReport("ALICE99", 1, 0)
                    + formatReport("CHARLIE", 1, 0);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertTrue(outContent.toString().endsWith(expectedOutput));
        }

        @Test
        void ignoresLines_whenUserNotAlphaNumeric()
        {
            // WITH
            final String filePath = "src/test/resources/non_alpha_numeric.log";
            final String expectedOutput = formatReport("ALICE99", 1, 0)
                    + formatReport("CHARLIE", 1, 0);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertTrue(outContent.toString().endsWith(expectedOutput));
        }

        @Test
        void ignoresLines_whenUserInvalidTimeStamp()
        {
            // WITH
            final String filePath = "src/test/resources/invalid_time_stamp.log";
            final String expectedOutput = formatReport("ALICE99", 1, 0)
                    + formatReport("CHARLIE", 1, 0);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertTrue(outContent.toString().endsWith(expectedOutput));
        }

        @Test
        void ignoresLines_whenUserInvalidStartOrEndMarker()
        {
            // WITH
            final String filePath = "src/test/resources/invalid_start_or_end_marker.log";
            final String expectedOutput = formatReport("ALICE99", 1, 0)
                    + formatReport("CHARLIE", 1, 0);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertTrue(outContent.toString().endsWith(expectedOutput));
        }

        @Test
        void success()
        {
            // WITH
            final String filePath = "src/test/resources/example.log";
            final String expectedOutput = formatReport("ALICE99", 4, 240)
                    + formatReport("CHARLIE", 3, 37);
            // WHEN
            LogFileProcessor.main(new String[]{filePath});
            // THEN
            assertEquals(expectedOutput, outContent.toString());
        }

        private String formatReport(final String user, final int sessionCount, final int duration)
        {
            return String.format("%s %d %d%n", user, sessionCount, duration);
        }
    }
}