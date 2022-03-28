package com.silenteight.scb.ingest.adapter.incomming.cbs.quartz;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@ConfigurationProperties("silenteight.scb-bridge.queuing")
@Component
@Data
@Validated
public class QueuingJobsProperties {

  private final List<QueuingJobProperties> jobs;
}
