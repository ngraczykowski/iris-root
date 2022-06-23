package com.silenteight.serp.governance.qa.sampling.generator.dto;

public class DistributionDtoFixture {

  public static DistributionDto getDistributionDto(String fieldName, String fieldValue) {
    return DistributionDto.builder()
        .fieldName(fieldName)
        .fieldValue(fieldValue)
        .build();
  }
}
