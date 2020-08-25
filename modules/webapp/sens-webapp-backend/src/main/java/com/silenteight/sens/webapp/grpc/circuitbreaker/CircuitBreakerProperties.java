package com.silenteight.sens.webapp.grpc.circuitbreaker;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConfigurationProperties("sens.webapp.discrepant-branches.max-archived")
@ConstructorBinding
public class CircuitBreakerProperties {

  Integer limitArchivedDiscrepantBranches;
}
