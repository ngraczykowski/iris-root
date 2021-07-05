package com.silenteight.hsbc.bridge.match;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.HsbcMatch;
import com.silenteight.hsbc.datasource.datamodel.*;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Slf4j
class OrikaMatchDataMapper implements MatchDataMapper {

  private final MapperFacade mapper;

  OrikaMatchDataMapper() {
    var mapperFactory = new DefaultMapperFactory.Builder().build();
    registerConcreteClasses(mapperFactory);

    mapper = mapperFactory.getMapperFacade();
  }

  private void registerConcreteClasses(DefaultMapperFactory mapperFactory) {
    mapperFactory.registerConcreteType(
        CaseInformation.class,
        com.silenteight.hsbc.bridge.json.internal.model.CaseInformation.class);
    mapperFactory.registerConcreteType(
        CtrpScreening.class,
        com.silenteight.hsbc.bridge.json.internal.model.CtrpScreening.class);
    mapperFactory.registerConcreteType(
        CustomerEntity.class,
        com.silenteight.hsbc.bridge.json.internal.model.CustomerEntity.class);
    mapperFactory.registerConcreteType(
        CustomerIndividual.class,
        com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual.class);
    mapperFactory.registerConcreteType(
        PrivateListEntity.class,
        com.silenteight.hsbc.bridge.json.internal.model.PrivateListEntity.class);
    mapperFactory.registerConcreteType(
        PrivateListIndividual.class,
        com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual.class);
    mapperFactory.registerConcreteType(
        WorldCheckEntity.class,
        com.silenteight.hsbc.bridge.json.internal.model.WorldCheckEntity.class);
    mapperFactory.registerConcreteType(
        WorldCheckIndividual.class,
        com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual.class);
    mapperFactory.registerConcreteType(
        CaseComment.class,
        com.silenteight.hsbc.bridge.json.internal.model.CaseComment.class);
  }

  @Override
  public MatchRawData map(HsbcMatch hsbcMatch) {
    try {
      return mapper.map(hsbcMatch, MatchRawData.class);
    } catch (RuntimeException ex) {
      log.error("Error on mapping from HsbcMatch", ex);
      throw ex;
    }
  }
}
