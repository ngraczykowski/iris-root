package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class HsbcMatch implements Serializable {

  private static final long serialVersionUID = -4825776220026119797L;

  private CaseInformation caseInformation;
  private List<CustomerEntity> customerEntities = new ArrayList<>();
  private List<CustomerIndividual> customerIndividuals = new ArrayList<>();
  private List<WorldCheckEntity> worldCheckEntities = new ArrayList<>();
  private List<WorldCheckIndividual> worldCheckIndividuals = new ArrayList<>();
  private List<PrivateListEntity> privateListEntities = new ArrayList<>();
  private List<PrivateListIndividual> privateListIndividuals = new ArrayList<>();
  private List<CtrpScreeningEntity> ctrpScreeningEntities = new ArrayList<>();
  private List<CtrpScreeningIndividual> ctrpScreeningIndividuals = new ArrayList<>();
  private List<CaseComment> caseComments = new ArrayList<>();
}
