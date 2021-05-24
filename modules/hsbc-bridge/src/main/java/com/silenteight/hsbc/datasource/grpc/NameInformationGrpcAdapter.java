package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.extractors.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.worldcheck.api.v1.ForeignAlias;
import com.silenteight.worldcheck.api.v1.GetNameInformationRequest;
import com.silenteight.worldcheck.api.v1.NamesInformationServiceGrpc.NamesInformationServiceBlockingStub;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class NameInformationGrpcAdapter implements NameInformationServiceClient {

  private final NamesInformationServiceBlockingStub namesInformationServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public GetNameInformationResponseDto getNameInformation(GetNameInformationRequestDto request) {
    var grpcRequest =
        GetNameInformationRequest.newBuilder().setWatchlistUuid(request.getWatchlistUuid()).build();

    var response = getStub().getNameInformation(grpcRequest);

    return GetNameInformationResponseDto.builder()
        .firstName(response.getFirstName())
        .lastName(response.getLastName())
        .aliases(response.getAliasesList())
        .foreignAliases(mapToForeignAliases(response.getForeignAliasesList()))
        .build();
  }

  private NamesInformationServiceBlockingStub getStub() {
    return namesInformationServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }

  private List<ForeignAliasDto> mapToForeignAliases(List<ForeignAlias> foreignAliases) {
    return foreignAliases.stream()
        .map(
            foreignAlias -> new ForeignAliasDto(
                foreignAlias.getName(),
                foreignAlias.getLanguage()))
        .collect(toList());
  }
}
