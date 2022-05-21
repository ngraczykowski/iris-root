package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.*;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceImplBase;
import com.silenteight.hsbc.datasource.category.command.GetMatchCategoryValuesCommand;
import com.silenteight.hsbc.datasource.category.dto.CategoryMatchesDto;
import com.silenteight.hsbc.datasource.category.dto.CategoryValueDto;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
@Slf4j
class CategoryGrpcServiceValues extends CategoryValueServiceImplBase {

  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;

  @Override
  public void batchGetMatchesCategoryValues(
      BatchGetMatchesCategoryValuesRequest request,
      StreamObserver<BatchGetMatchesCategoryValuesResponse> responseObserver) {
    responseObserver.onNext(getMatchCategoryValues(request));

    log.info("Category values has been sent.");
    responseObserver.onCompleted();
  }

  private BatchGetMatchesCategoryValuesResponse getMatchCategoryValues(BatchGetMatchesCategoryValuesRequest request) {
    log.info("Received getMatchCategoryValues request: " + request.getCategoryMatchesList());

    var command = GetMatchCategoryValuesCommand.builder()
        .categoryMatches(mapToCategoryMatchesDto(request.getCategoryMatchesList()))
        .build();
    var matchCategoryValues = getMatchCategoryValuesUseCase.activate(command);

    return BatchGetMatchesCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(mapMatchCategoryValues(matchCategoryValues))
        .build();
  }

  private List<CategoryMatchesDto> mapToCategoryMatchesDto(List<CategoryMatches> categoryMatches) {
    return categoryMatches.stream()
        .map(categoryMatch -> CategoryMatchesDto.builder()
            .category(categoryMatch.getCategory())
            .matches(categoryMatch.getMatchesList())
            .build())
        .collect(Collectors.toList());
  }

  private List<CategoryValue> mapMatchCategoryValues(List<CategoryValueDto> categoryValues) {
    return categoryValues.stream()
        .map(this::mapCategoryValue)
        .collect(Collectors.toList());
  }

  private CategoryValue mapCategoryValue(CategoryValueDto categoryValue) {
    var builder = CategoryValue.newBuilder()
        .setName(categoryValue.getName())
        .setMatch(categoryValue.getMatch());

    var values = categoryValue.getValues();
    if (categoryValue.isMultiValue()) {
      builder.setMultiValue(MultiValue.newBuilder()
          .addAllValues(values)
          .build());
    } else {
      builder.setSingleValue(values.stream().findFirst().orElse(""));
    }

    return builder.build();
  }
}
