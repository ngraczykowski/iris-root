package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.serp.governance.qa.sampling.generator.exception.InvalidTotalAlertCountException;

class DistributionCalculator {

  private final Long totalSampleCount;
  private final long totalOverallCount;

  DistributionCalculator(long totalSampleCount, long totalOverallCount) {
    assertTotalOverallCountIsGreaterThanZero(totalOverallCount);

    this.totalSampleCount = totalSampleCount;
    this.totalOverallCount = totalOverallCount;
  }

  private static void assertTotalOverallCountIsGreaterThanZero(long totalOverallCount) {
    if (totalOverallCount < 1)
      throw new InvalidTotalAlertCountException(totalOverallCount);
  }

  public long calculateAmount(long countRiskType) {
    if (!assertNumbersGreaterThanZero(countRiskType))
      return 0;
    return ((totalSampleCount * countRiskType) / totalOverallCount) + 1;
  }

  private boolean assertNumbersGreaterThanZero(long countRiskType) {
    return (countRiskType > 0 && totalSampleCount > 0 && totalOverallCount > 0);
  }
}
