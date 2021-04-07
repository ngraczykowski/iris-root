package com.silenteight.hsbc.bridge.domain;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityComposite {

  private CustomerEntities customerEntities;
  private List<WorldCheckEntities> worldCheckEntities;
  private List<PrivateListEntities> privateListEntities;
  private List<CountryCtrpScreening> countryCtrpScreeningEntities;
}
