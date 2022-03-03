package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.Data;

import com.silenteight.proto.serp.v1.alert.AnalystSolution;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("serp.scb.bridge.gns.solution")
@Component
@Data
@Validated
class GnsSolutionProperties {

  private Map<AnalystSolution, List<Integer>> states;
}
