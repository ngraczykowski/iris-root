package com.silenteight.universaldatasource.app.commentinput.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.DeleteCommentInputsUseCase;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.universaldatasource.common.messaging.RabbitQueueCommons.UDS_COMMENT_INPUT_RETENTION_QUEUE_NAME;

@Slf4j
@RequiredArgsConstructor
@Configuration
class CommentInputRetentionInboundAmqpConfiguration {

  private final DeleteCommentInputsUseCase deleteCommentInputsUseCase;

  @RabbitListener(queues = UDS_COMMENT_INPUT_RETENTION_QUEUE_NAME)
  public void commentInputRetentionReceive(AlertsExpired alertsExpired) {
    log.info("Comment input retention message received");
    deleteCommentInputsUseCase.delete(alertsExpired.getAlertsList());
  }
}
