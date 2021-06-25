package com.silenteight.hsbc.datasource.feature.ispep;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.ispep.GetModelFieldNamesResponseDto;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepResponseDto;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepServiceClient;

import java.util.List;
import java.util.stream.Stream;

public interface IsPepQuery {

  Stream<String> apIndividualExtractLobCountry();

  GetModelFieldNamesResponseDto provideRequiredModelFieldNames();

  List<IsPepResponseDto> verifyIsPep(
      List<String> apFields, GetModelFieldNamesResponseDto fieldNamesResponse);

  interface Factory {

    IsPepQuery create(MatchData matchData, IsPepServiceClient client);
  }
}
