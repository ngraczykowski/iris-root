package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class HsbcMatch implements Serializable {

  private CaseInformation caseInformation;
  private CustomerEntity customerEntity;
  private CustomerIndividual customerIndividual;
  private List<CtrpScreeningIndividual> ctrpScreeningIndividuals = new ArrayList<>();
  private List<WorldCheckEntity> worldCheckEntities = new ArrayList<>();
  private List<WorldCheckIndividual> worldCheckIndividuals = new ArrayList<>();
  private List<PrivateListIndividual> privateListIndividuals = new ArrayList<>();
  private List<PrivateListEntity> privateListEntities = new ArrayList<>();
  private List<CtrpScreeningIndividual> ctrpScreeningEntities = new ArrayList<>();

}
