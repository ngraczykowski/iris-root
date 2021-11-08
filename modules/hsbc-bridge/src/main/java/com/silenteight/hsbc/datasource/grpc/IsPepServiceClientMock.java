package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.dto.ispep.ReasonDto;
import com.silenteight.hsbc.datasource.extractors.ispep.*;

import java.util.List;

class IsPepServiceClientMock implements IsPepServiceClient {

  @Override
  public IsPepResponseDto verifyIfIsPep(IsPepRequestDto isPepRequest) {
    return IsPepResponseDto.builder()
        .solution("")
        .reason(createReason())
        .build();
  }

  @Override
  public GetModelFieldNamesResponseDto getModelFieldNames(
      GetModelFieldNamesRequestDto getModelFieldNamesRequest) {
    return GetModelFieldNamesResponseDto.builder()
        .fieldNames(List.of(""))
        .version("")
        .regionName("")
        .build();
  }

  private ReasonDto createReason() {
    return ReasonDto.builder()
        .message("")
        .noPepPositions(List.of(""))
        .notMatchedPositions(List.of(""))
        .pepPositions(List.of(""))
        .linkedPepsUids(List.of(""))
        .numberOfNotPepDecisions(0)
        .numberOfPepDecisions(0)
        .version("")
        .regionName("")
        .build();
  }
}
