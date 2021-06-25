package com.silenteight.hsbc.datasource.extractors.ispep;

public interface IsPepServiceClient {

  IsPepResponseDto verifyIfIsPep(IsPepRequestDto request);

  GetModelFieldNamesResponseDto getModelFieldNames(GetModelFieldNamesRequestDto request);
}
