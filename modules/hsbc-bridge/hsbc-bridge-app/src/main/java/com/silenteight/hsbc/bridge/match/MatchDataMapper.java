package com.silenteight.hsbc.bridge.match;

import com.silenteight.hsbc.bridge.json.external.model.*;
import com.silenteight.hsbc.bridge.json.internal.model.CtrpScreening;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface MatchDataMapper {

  MatchRawData map(HsbcMatch hsbcMatch);

  default com.silenteight.hsbc.datasource.datamodel.CaseInformation castCaseInformation(
      com.silenteight.hsbc.bridge.json.internal.model.CaseInformation caseInformation) {
    return caseInformation;
  }

  com.silenteight.hsbc.bridge.json.internal.model.CaseInformation mapCaseInformation(
      CaseInformation caseInformation);

  com.silenteight.hsbc.bridge.json.internal.model.CustomerEntity mapCustomerEntity(
      CustomerEntity customerEntity);

  com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual mapCustomerIndividual(
      CustomerIndividual customerIndividual);

  com.silenteight.hsbc.bridge.json.internal.model.WorldCheckEntity mapWorldCheckEntity(
      WorldCheckEntity worldCheckEntity);

  com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual mapWorldCheckIndividual(
      WorldCheckIndividual worldCheckIndividual);

  com.silenteight.hsbc.bridge.json.internal.model.PrivateListEntity mapPrivateListEntity(
      PrivateListEntity privateListEntity);

  com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual mapPrivateListIndividual(
      PrivateListIndividual privateListIndividual);

  CtrpScreening mapCtrpScreeningEntity(CtrpScreeningEntity ctrpScreeningEntity);

  CtrpScreening mapCtrpScreeningIndividual(
      CtrpScreeningIndividual ctrpScreeningIndividual);

  com.silenteight.hsbc.bridge.json.internal.model.CaseComment mapCaseComment(
      CaseComment caseComment);

  com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningIndividuals
      mapNegativeNewsScreeningIndividuals(
          NegativeNewsScreeningIndividuals negativeNewsScreeningIndividuals);

  com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningEntities
      mapNegativeNewsScreeningEntities(NegativeNewsScreeningEntities negativeNewsScreeningEntities);
}
