package com.silenteight.sens.webapp.audit.slf4j;

import com.silenteight.sens.webapp.audit.api.AuditMarker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Slf4jBasedAuditLogTest {

  @Mock
  private Appender<ILoggingEvent> mockAppender;

  private Slf4jBasedAuditLog slf4jBasedAuditLog = new Slf4jBasedAuditLog();

  @BeforeEach
  public void setUp() {
    Logger logger = (Logger) LoggerFactory.getLogger(Slf4jBasedAuditLog.class.getName());
    logger.addAppender(mockAppender);
  }

  @Test
  void logsToSlf4jOnInfoLevel() {
    String msgTemplate = "Ala ma {}";
    String arg = "kota";
    slf4jBasedAuditLog.logInfo(AuditMarker.INTERNAL, msgTemplate, arg);

    verify(mockAppender).doAppend(
        argThat(argument -> {
          assertThat(argument.getMarker().getName()).isEqualTo("INTERNAL");
          assertThat(argument.getMessage()).isEqualTo(msgTemplate);
          assertThat(argument.getArgumentArray()).containsExactly(arg);
          assertThat(argument.getLevel()).isEqualTo(Level.INFO);
          return true;
        }));
  }

  @Test
  void logsToSlf4jOnErrorLevel() {
    String msgTemplate = "Kot {} {}";
    String arg1 = "Ali";
    String arg2 = "uciekl";
    slf4jBasedAuditLog.logError(AuditMarker.KEYCLOAK_MIGRATION, msgTemplate, arg1, arg2);

    verify(mockAppender).doAppend(
        argThat(argument -> {
          assertThat(argument.getMarker().getName()).isEqualTo("KEYCLOAK_MIGRATION");
          assertThat(argument.getMessage()).isEqualTo(msgTemplate);
          assertThat(argument.getArgumentArray()).containsExactly(arg1, arg2);
          assertThat(argument.getLevel()).isEqualTo(Level.ERROR);
          return true;
        }));
  }
}
