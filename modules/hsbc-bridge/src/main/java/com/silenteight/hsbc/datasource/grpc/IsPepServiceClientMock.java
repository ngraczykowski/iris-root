package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.dto.ispep.ReasonDto;
import com.silenteight.hsbc.datasource.extractors.ispep.*;

import static java.util.List.of;

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
        .fieldNames(of(""))
        .version("")
        .regionName("")
        .build();
  }

  private ReasonDto createReason() {
    return ReasonDto.builder()
        .message("")
        .noPepPositions(of(""))
        .notMatchedPositions(of(""))
        .pepPositions(of(""))
        .linkedPepsUids(of(""))
        .numberOfNotPepDecisions(0)
        .numberOfPepDecisions(0)
        .version("")
        .regionName("")
        .build();
  }
}
