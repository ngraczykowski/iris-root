package com.silenteight.qco.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.ChangeCondition;
import com.silenteight.qco.domain.model.QcoParams;
import com.silenteight.qco.domain.model.QcoPolicyStepSolutionOverrideConfiguration;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class QcoConfigurationHolder {

  private final SolutionConfigurationProvider configurationProvider;
  @Getter
  private Map<ChangeCondition, QcoParams> configuration;

  @PostConstruct
  void init() {
    configuration = configurationProvider.getSolutionConfigurations().stream()
        .collect(Collectors.toUnmodifiableMap(
            this::createKeyMap,
            this::createValueMap,
            this::duplicationKeyResolver));
    log.info(
        "The following data was loaded {} values to the configuration map:", configuration.size());
    configuration.forEach((key, param) ->
        log.info("Configuration QCO map: key: {}, param: {} and keyHashCode: {}",
            key.toString(), param.toString(), key.hashCode()));
  }

  @NotNull
  private QcoParams duplicationKeyResolver(QcoParams existedQcoParam, QcoParams newQcoParam) {
    log.warn("New solution configuration {} was ignored because of existing configuration {} "
        + "under the same key.", newQcoParam.toString(), existedQcoParam.toString());
    return existedQcoParam;
  }

  @NotNull
  private QcoParams createValueMap(QcoPolicyStepSolutionOverrideConfiguration config) {
    return new QcoParams(config.getMatchThreshold(), config.getSolutionOverride());
  }

  @NotNull
  private ChangeCondition createKeyMap(QcoPolicyStepSolutionOverrideConfiguration config) {
    return new ChangeCondition(config.getPolicyId(), config.getStepId(), config.getSolution());
  }
}