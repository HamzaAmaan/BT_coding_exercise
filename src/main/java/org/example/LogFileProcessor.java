package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static org.example.enums.ErrorMessage.*;

public class LogFileProcessor
{
    private static final String LOG_EXTENSION = ".log";
    public static void main(String[] args)
    {
        if (args.length == 0 || Objects.equals(args[0], ""))
        {
            throw new IllegalArgumentException(MISSING_COMMAND_LINE_ARGUMENT.getMessage());
        }
        final String filePath = args[0];
        if (!filePath.toLowerCase().endsWith(LOG_EXTENSION))
        {
            throw new IllegalArgumentException(INVALID_FILE_EXTENSION.getMessage());
        }

        processLogFile(filePath);
    }

    private static void processLogFile(final String filePath)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {

        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(MISSING_FILE.getMessage());
        }
    }
}