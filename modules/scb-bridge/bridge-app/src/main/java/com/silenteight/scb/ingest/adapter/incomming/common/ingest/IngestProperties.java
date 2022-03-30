package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.ingest")
@Component
@Validated
@Data
class IngestProperties {

}
