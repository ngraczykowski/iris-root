/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.infrastructure.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.SolutionConfigurationProvider;
import com.silenteight.iris.qco.domain.model.QcoPolicyStepSolutionOverrideConfiguration;

import io.vavr.control.Try;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class CsvSolutionConfigurationProvider implements SolutionConfigurationProvider {

  private final CsvStreamParser<QcoPolicyStepSolutionOverrideConfiguration> csvStreamParser;
  private final Resource resourceFile;

  @Override
  public List<QcoPolicyStepSolutionOverrideConfiguration> getSolutionConfigurations() {
    return Try.of(() ->
            csvStreamParser.parse(
                resourceFile.getInputStream(), QcoPolicyStepSolutionOverrideConfiguration.class))
        .onFailure(e -> log.error("Encountered error parsing QCO configuration file", e))
        .getOrElseThrow(SolutionConfigurationFileParserException::new);
  }
}
