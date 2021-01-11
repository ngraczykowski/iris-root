package com.silenteight.serp.governance.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.FeatureGroup;
import com.silenteight.serp.governance.featuregroup.FeatureGroupService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.silenteight.serp.governance.grpc.MessagingConstants.QUEUE_GOVERNANCE_PIPELINE_FEATURE_GROUPS;

@RequiredArgsConstructor
@Slf4j
class FeatureGroupReceiver {

  private final FeatureGroupService service;

  @RabbitListener(queues = QUEUE_GOVERNANCE_PIPELINE_FEATURE_GROUPS)
  public void process(FeatureGroup featureGroup) {
    log.info("Received feature group");
    service.storeVectors(featureGroup);
  }
}
