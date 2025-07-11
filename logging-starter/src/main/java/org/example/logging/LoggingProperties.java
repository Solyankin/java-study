package org.example.logging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "logging.starter")
public class LoggingProperties {

    private boolean enabled = true;
    private boolean restControllerEnabled = true;
    private boolean serviceEnabled = true;
    private boolean logMethodArgs = true;
    private boolean logMethodResult = true;
    private boolean logExecutionTime = true;
}