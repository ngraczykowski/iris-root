package com.silenteight.bridge.core.reports.adapter.incoming.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.reports.domain.ReportsFacade;
import com.silenteight.bridge.core.reports.domain.commands.SendReportsCommand;
import com.silenteight.proto.registration.api.v1.MessageBatchDelivered;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "reports.enabled", havingValue = "true")
class BatchDeliveredAmqpListener {

  private final ReportsFacade reportsFacade;

  @RabbitListener(
      queues = "${amqp.reports.incoming.batch-delivered.queue-name}",
      errorHandler = "reportsAmqpErrorHandler"
  )
  public void batchDelivered(MessageBatchDelivered message) {
    log.info("Received BatchDelivered (reports) message for batch {}", message.getBatchId());
    var command = new SendReportsCommand(message.getBatchId(), message.getAnalysisName());
    reportsFacade.sendReports(command);
  }
}
