package com.silenteight.hsbc.bridge.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchRawData implements MatchData {

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

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningEntities.class)
  private List<NegativeNewsScreeningEntities> negativeNewsScreeningEntities;

  @JsonDeserialize(contentAs = com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningIndividuals.class)
  private List<NegativeNewsScreeningIndividuals> negativeNewsScreeningIndividuals;

  @Override
  @JsonIgnore
  public boolean isIndividual() {
    return CollectionUtils.isNotEmpty(getCustomerIndividuals());
  }

  @Override
  @JsonIgnore
  public List<NegativeNewsScreeningEntities> getNnsEntities() {
    return negativeNewsScreeningEntities;
  }

  @Override
  @JsonIgnore
  public List<NegativeNewsScreeningIndividuals> getNnsIndividuals() {
    return negativeNewsScreeningIndividuals;
  }
}
