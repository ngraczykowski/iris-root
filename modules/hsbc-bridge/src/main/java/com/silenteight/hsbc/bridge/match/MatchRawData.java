package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.datasource.datamodel.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import static java.util.Objects.nonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
class MatchRawData implements MatchData {

  @JsonDeserialize(as = com.silenteight.hsbc.bridge.json.internal.model.CaseInformation.class)
  private CaseInformation caseInformation;

  @JsonDeserialize(as = com.silenteight.hsbc.bridge.json.internal.model.CustomerEntity.class)
  private CustomerEntity customerEntity;

  @JsonDeserialize(as = com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual.class)
  private CustomerIndividual customerIndividual;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CtrpScreening.class)
  private List<CtrpScreening> ctrpScreeningEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.PrivateListEntity.class)
  private List<PrivateListEntity> privateListEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.WorldCheckEntity.class)
  private List<WorldCheckEntity> worldCheckEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CtrpScreening.class)
  private List<CtrpScreening> ctrpScreeningIndividuals;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual.class)
  private List<PrivateListIndividual> privateListIndividuals;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual.class)
  private List<WorldCheckIndividual> worldCheckIndividuals;

  @JsonIgnore
  public boolean isIndividual() {
    return nonNull(getCustomerIndividual());
  }

  @JsonIgnore
  public Integer getCaseId() {
    return caseInformation.getId();
  }
}
