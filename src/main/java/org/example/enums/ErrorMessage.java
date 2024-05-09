package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage
{
    MISSING_COMMAND_LINE_ARGUMENT("Please provide the log file path as an argument."),
    INVALID_FILE_EXTENSION("The file path provided is not for a log file."),
    MISSING_FILE("Log file does not exist."),
    ;

    private final String message;
}
