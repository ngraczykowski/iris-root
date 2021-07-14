package com.silenteight.searpayments.scb.request;

import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.aspects.logging.LogContext.LogContextAction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.messaging.MessageChannel;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.silenteight.searpayments.scb.util.MessagingUtil.toPayload;

@Slf4j
@RequiredArgsConstructor
class PublishAlerts {

  private static final long ZERO_TIMEOUT = 0L;
  private final MessageChannel output;

  @NotNull ResponseDto invoke(@NonNull RequestDto requestDto) {
    log.debug("invoke");

    List<Alert> completed = new ArrayList<>();
    List<Alert> failed = new ArrayList<>();

    for (Alert alert : requestDto.getAlerts()) {
      if (publish(alert)) {
        completed.add(alert);
      } else {
        failed.add(alert);
      }
    }

    return new ResponseDto(completed, failed);
  }

  private boolean publish(Alert alert) {
    var published = output.send(toPayload(alert), ZERO_TIMEOUT);

    if (log.isDebugEnabled())
      logAlertPublishResult(alert.getId(), alert.getSystemId(), published);

    return published;
  }

  @LogContext(LogContextAction.CLEAR_PRESERVE)
  private void logAlertPublishResult(Long alertId, @NonNull String systemId, boolean published) {
    MDC.put("alertId", String.valueOf(alertId));
    MDC.put("systemId", systemId);
    log.debug("Alert published: {}", published);
  }

  @Value
  static class RequestDto {
    List<Alert> alerts;
  }

  @Value
  static class ResponseDto {
    @NonNull List<Alert> completedAlerts;
    @NonNull List<Alert> failedAlerts;
  }
}
