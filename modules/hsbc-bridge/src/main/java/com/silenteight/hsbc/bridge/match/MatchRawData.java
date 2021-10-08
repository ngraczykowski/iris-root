package com.silenteight.hsbc.bridge.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
class MatchRawData implements MatchData {

  @JsonDeserialize(as = com.silenteight.hsbc.bridge.json.internal.model.CaseInformation.class)
  private CaseInformation caseInformation;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CustomerEntity.class)
  private List<CustomerEntity> customerEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual.class)
  private List<CustomerIndividual> customerIndividuals;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.WorldCheckEntity.class)
  private List<WorldCheckEntity> worldCheckEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual.class)
  private List<WorldCheckIndividual> worldCheckIndividuals;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.PrivateListEntity.class)
  private List<PrivateListEntity> privateListEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual.class)
  private List<PrivateListIndividual> privateListIndividuals;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CtrpScreening.class)
  private List<CtrpScreening> ctrpScreeningEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CtrpScreening.class)
  private List<CtrpScreening> ctrpScreeningIndividuals;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.CaseComment.class)
  private List<CaseComment> caseComments;

  @Override
  @JsonIgnore
  public boolean isIndividual() {
    return isNotEmpty(getCustomerIndividuals());
  }
}
