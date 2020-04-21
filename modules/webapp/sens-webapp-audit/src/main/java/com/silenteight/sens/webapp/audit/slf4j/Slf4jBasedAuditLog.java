package com.silenteight.sens.webapp.audit.slf4j;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.audit.api.AuditMarker;
import com.silenteight.sens.webapp.audit.api.SeverityLevel;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Map;

import static com.silenteight.sens.webapp.audit.api.SeverityLevel.ERROR;
import static com.silenteight.sens.webapp.audit.api.SeverityLevel.INFO;

@Slf4j
public class Slf4jBasedAuditLog implements AuditLog {

  private static final Map<SeverityLevel, TriConsumer<Marker, String, Object[]>> LOGGERS =
      Map.of(INFO, log::info, ERROR, log::error);

  @Override
  public void log(
      SeverityLevel severityLevel, AuditMarker auditMarker, String format, Object... args) {

    LOGGERS.get(severityLevel).accept(logMarkerOf(auditMarker), format, args);
  }

  private static Marker logMarkerOf(AuditMarker auditMarker) {
    return MarkerFactory.getMarker(auditMarker.name());
  }

  @FunctionalInterface
  public interface TriConsumer<K, V, S> {

    void accept(K k, V v, S s);
  }
}
