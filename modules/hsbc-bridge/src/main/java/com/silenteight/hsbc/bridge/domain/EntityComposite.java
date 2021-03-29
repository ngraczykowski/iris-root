package com.silenteight.hsbc.bridge.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class EntityComposite {

  CustomerEntities customerEntities;
  List<WorldCheckEntities> worldCheckEntities;
  List<PrivateListEntities> privateListEntities;
  List<CountryCtrpScreening> countryCtrpScreeningEntities;
}
