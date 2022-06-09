package com.silenteight.hsbc.datasource.datamodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WatchlistType {

  CTRPPRHB_LIST_ENTITIES("CTRPPRHBListEntities"),
  CTRPPRHB_LIST_INDIVIDUALS("CTRPPRHBListIndividuals"),
  WORLDCHECK_INDIVIDUALS("WorldCheckIndividuals"),
  WORLDCHECK_ENTITIES("WorldCheckEntities"),
  PRIVATE_LIST_INDIVIDUALS("PrivateListIndividuals"),
  PRIVATE_LIST_ENTITIES("PrivateListEntities"),
  NNS_LIST_ENTITIES("NNSEntities"),
  NNS_LIST_INDIVIDUALS("NNSIndividuals");

  @Getter
  private final String label;
}
