package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlertData {

  @JsonProperty("CTRPPRHBListIndividuals")
  private final List<CtrpScreeningIndividual> ctrpScreeningIndividuals = new ArrayList<>();
  @JsonProperty("CustomerEntities")
  private final List<CustomerEntity> customerEntities = new ArrayList<>();
  @JsonProperty("WorldCheckEntities")
  private final List<WorldCheckEntity> worldCheckEntities = new ArrayList<>();
  @JsonProperty("CustomerIndividuals")
  private final List<CustomerIndividual> customerIndividuals = new ArrayList<>();
  @JsonProperty("WorldCheckIndividuals")
  private final List<WorldCheckIndividual> worldCheckIndividuals = new ArrayList<>();
  @JsonProperty("Relationships")
  private final List<Relationship> relationships = new ArrayList<>();
  @JsonProperty("PrivateListIndividuals")
  private final List<PrivateListIndividual> privateListIndividuals = new ArrayList<>();
  @JsonProperty("PrivateListEntities")
  private final List<PrivateListEntity> privateListEntities = new ArrayList<>();
  @JsonProperty("CTRPPRHBListEntities")
  private final List<CtrpScreeningEntity> ctrpScreeningEntities = new ArrayList<>();
  @JsonProperty("DN_CASE")
  private CaseInformation caseInformation;
  @JsonProperty("DN_CASECOMMENT")
  private final List<CaseComment> caseComments = new ArrayList<>();
  @JsonProperty("DN_CASEHISTORY")
  private final List<CaseHistory> caseHistory = new ArrayList<>();

  @JsonIgnore
  public String getId() {
    return caseInformation.getKeyLabel();
  }

  @JsonIgnore
  public String getFlagKey() {
    return caseInformation.getFlagKey();
  }

  @JsonIgnore
  public String getCaseId() {
    return caseInformation.getId() + "";
  }
}
