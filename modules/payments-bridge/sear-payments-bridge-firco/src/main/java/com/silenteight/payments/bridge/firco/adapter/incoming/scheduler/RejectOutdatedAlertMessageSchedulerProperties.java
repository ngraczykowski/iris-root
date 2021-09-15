package com.silenteight.payments.bridge.firco.adapter.incoming.scheduler;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("pb.firco.alertmessage.outdated")
@Data
@Validated
class RejectOutdatedAlertMessageSchedulerProperties {

  private int chunkSize = 100;

}
