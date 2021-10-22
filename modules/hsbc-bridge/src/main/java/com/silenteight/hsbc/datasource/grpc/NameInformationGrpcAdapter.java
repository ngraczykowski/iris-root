package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.extractors.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.extractors.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.worldcheck.api.v1.ForeignAlias;
import com.silenteight.worldcheck.api.v1.GetNameInformationRequest;
import com.silenteight.worldcheck.api.v1.NamesInformationServiceGrpc.NamesInformationServiceBlockingStub;

import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class NameInformationGrpcAdapter implements NameInformationServiceClient {

  private final NamesInformationServiceBlockingStub namesInformationServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public Optional<GetNameInformationResponseDto> getNameInformation(
      GetNameInformationRequestDto request) {
    var watchlistUuid = request.getWatchlistUuid();
    var grpcRequest =
        GetNameInformationRequest.newBuilder().setWatchlistUuid(watchlistUuid).build();

    try {
      var response = getStub().getNameInformation(grpcRequest);
      return Optional.of(GetNameInformationResponseDto.builder()
          .firstName(response.getFirstName())
          .lastName(response.getLastName())
          .aliases(response.getAliasesList())
          .foreignAliases(mapToForeignAliases(response.getForeignAliasesList()))
          .build());

    } catch (StatusRuntimeException e) {
      if (isWatchlistIdNotFound(e.getStatus())) {
        log.warn("Watchlist uuid: {} not found in model", watchlistUuid);
        return Optional.empty();
      } else {
        throw e;
      }
    }
  }

  private boolean isWatchlistIdNotFound(Status status) {
    return status.getCode() == Code.NOT_FOUND || status.getCode() == Code.FAILED_PRECONDITION;
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
