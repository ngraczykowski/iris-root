package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.dto.ispep.ReasonDto;
import com.silenteight.hsbc.datasource.extractors.ispep.*;
import com.silenteight.worldcheck.api.v1.*;
import com.silenteight.worldcheck.api.v1.IsPepServiceGrpc.IsPepServiceBlockingStub;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
public class IsPepGrpcAdapter implements IsPepServiceClient {

  private final IsPepServiceBlockingStub isPepServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public IsPepResponseDto verifyIfIsPep(IsPepRequestDto request) {
    var grpcRequest = IsPepRequest.newBuilder()
        .addAllFieldNames(request.getFieldNames())
        .putAllApFields(request.getApFields())
        .setBankRegion(request.getBankRegion())
        .setUid(request.getUid())
        .build();

    var response = getStub().verifyIfIsPep(grpcRequest);

    return mapToIsPepResponse(response);
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public GetModelFieldNamesResponseDto getModelFieldNames(
      GetModelFieldNamesRequestDto request) {
    var grpcRequest =
        GetModelFieldNamesRequest.newBuilder().setBankRegion(request.getBankRegion()).build();

    var response = getStub().getModelFieldNames(grpcRequest);

    return mapToGetModelFieldNames(response);
  }

  private IsPepResponseDto mapToIsPepResponse(IsPepResponse response) {
    return IsPepResponseDto.builder()
        .solution(response.getSolution())
        .reason(mapToReason(response.getReason()))
        .build();
  }

  private ReasonDto mapToReason(Reason reason) {
    return ReasonDto.builder()
        .message(reason.getMessage())
        .noPepPositions(reason.getNoPepPositionsList())
        .notMatchedPositions(reason.getNotMatchedPositionsList())
        .pepPositions(reason.getPepPositionsList())
        .linkedPepsUids(reason.getLinkedPepsUidsList())
        .numberOfNotPepDecisions(reason.getNumberOfNotPepDecisions())
        .numberOfPepDecisions(reason.getNumberOfPepDecisions())
        .version(reason.getModelDetails().getVersion())
        .regionName(reason.getModelDetails().getRegionName())
        .build();
  }

  private GetModelFieldNamesResponseDto mapToGetModelFieldNames(
      GetModelFieldNamesResponse response) {
    return GetModelFieldNamesResponseDto.builder()
        .fieldNames(response.getModelFieldNames().getFieldNamesList())
        .version(response.getModelFieldNames().getModelDetails().getVersion())
        .regionName(response.getModelFieldNames().getModelDetails().getRegionName())
        .build();
  }

  private IsPepServiceBlockingStub getStub() {
    return isPepServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
