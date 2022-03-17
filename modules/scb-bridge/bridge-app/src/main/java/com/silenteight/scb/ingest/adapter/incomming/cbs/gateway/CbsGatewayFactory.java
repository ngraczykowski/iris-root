package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CbsGatewayFactory {

  public static CbsHitDetailsHelperFetcher getHitDetailsHelperFetcher(
      FetcherConfiguration configuration) {
    return useCbsHitDetailsHelperView(configuration) ?
           new HitDetailsHelperFetcher(configuration) :
           new EmptyCbsHitDetailsHelperFetcher();
  }

  private static boolean useCbsHitDetailsHelperView(FetcherConfiguration configuration) {
    return isNotBlank(configuration.getDbRelationName());
  }
}
