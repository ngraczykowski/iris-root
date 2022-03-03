package com.silenteight.customerbridge.cbs.quartz;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@ConfigurationProperties("serp.scb.bridge.queuing")
@Component
@Data
@Validated
public class QueuingJobsProperties {

  private final List<QueuingJobProperties> jobs;
}
