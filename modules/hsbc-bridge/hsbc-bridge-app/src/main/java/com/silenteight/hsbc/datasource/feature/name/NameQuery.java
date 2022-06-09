package com.silenteight.hsbc.datasource.feature.name;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.extractors.name.Party;

import java.util.Collection;
import java.util.stream.Stream;

public interface NameQuery {

  Stream<String> apIndividualExtractProfileFullName();

  Stream<String> apIndividualExtractNames();

  Stream<String> apIndividualExtractOtherNames();

  Stream<String> apEntityExtractEntityNameOriginal();

  Stream<String> apEntityExtractOtherNames();

  Stream<String> mpWorldCheckIndividualsExtractNames();

  Stream<String> nnsIndividualsExtractNames();

  Stream<String> mpWorldCheckIndividualsExtractOtherNames();

  Stream<String> nnsIndividualsExtractOtherNames();

  Stream<String> mpWorldCheckIndividualsExtractXmlNamesWithCountries();

  public Stream<String> nnsIndividualsExtractXmlNamesWithCountries();

  Stream<String> mpWorldCheckEntitiesExtractNames();

  Stream<String> nnsEntitiesExtractNames();

  Stream<String> mpWorldCheckEntitiesExtractOtherNames();

  Stream<String> nnsEntitiesExtractOtherNames();

  Stream<String> mpWorldCheckEntitiesExtractXmlNamesWithCountries();

  Stream<String> nnsEntitiesExtractXmlNamesWithCountries();

  Stream<String> mpPrivateListIndividualsExtractNames();

  Stream<String> mpPrivateListIndividualsExtractOtherNames();

  Stream<String> mpPrivateListEntitiesExtractNames();

  Collection<String> applyOriginalScriptEnhancementsForIndividualNamesWithAliases();

  Party applyOriginalScriptEnhancementsForIndividualNames();

  Party applyOriginalScriptEnhancementsForIndividualNamesAll();

  interface Factory {

    NameQuery create(MatchData matchData, NameInformationServiceClient client);
  }
}
