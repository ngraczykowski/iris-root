package com.silenteight.scb.ingest.adapter.incomming.common.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@ConfigurationProperties("silenteight.global-healthcheck")
public record GlobalHealthEndpointProperties(List<Service> services) {

  record Service(String id, String contextPath) {}

}
