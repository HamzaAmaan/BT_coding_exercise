package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SessionInfo
{
    private int sessionCount;
    private Duration totalDuration;
}
