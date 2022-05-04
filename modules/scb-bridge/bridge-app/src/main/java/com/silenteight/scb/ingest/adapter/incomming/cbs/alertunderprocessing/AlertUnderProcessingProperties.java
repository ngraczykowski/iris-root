package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.solving.alert-processor")
public record AlertUnderProcessingProperties(int readChunkSize) {
}
