package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.*;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.hsbc.datasource.category.command.GetMatchCategoryValuesCommand;
import com.silenteight.hsbc.datasource.category.dto.CategoryDto;
import com.silenteight.hsbc.datasource.category.dto.CategoryValueDto;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GRpcService
@RequiredArgsConstructor
class CategoryGrpcService extends CategoryServiceImplBase {

  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;
  private final ListCategoriesUseCase listCategoriesUseCase;

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {
    responseObserver.onNext(getMatchCategoryValues(request));
    responseObserver.onCompleted();
  }

  @Override
  public void listCategories(
      ListCategoriesRequest request, StreamObserver<ListCategoriesResponse> responseObserver) {
    responseObserver.onNext(getCategoriesResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchCategoryValuesResponse getMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {

    var command = GetMatchCategoryValuesCommand.builder()
        .matchValues(request.getMatchValuesList())
        .build();
    var matchCategoryValues = getMatchCategoryValuesUseCase.activate(command);

    return BatchGetMatchCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(mapMatchCategoryValues(matchCategoryValues))
        .build();
  }

  private List<CategoryValue> mapMatchCategoryValues(List<CategoryValueDto> categoryValues) {
    return categoryValues.stream()
        .map(this::mapCategoryValue)
        .collect(Collectors.toList());
  }

  private CategoryValue mapCategoryValue(CategoryValueDto categoryValue) {
    var builder = CategoryValue.newBuilder()
        .setName(categoryValue.getName());

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

  private ListCategoriesResponse getCategoriesResponse() {
    var categories = listCategoriesUseCase.activate();

    return ListCategoriesResponse.newBuilder()
        .addAllCategories(mapCategories(categories))
        .build();
  }

  private List<Category> mapCategories(List<CategoryDto> categories) {
    return categories.stream()
        .map(this::mapCategory)
        .collect(Collectors.toList());
  }

  private Category mapCategory(CategoryDto category) {
    var builder = Category.newBuilder()
        .setName(category.getName())
        .setDisplayName(category.getDisplayName())
        .addAllAllowedValues(category.getAllowedValues())
        .setMultiValue(category.isMultiValue());

    Optional
        .of(category.getCategoryType())
        .ifPresent(v -> builder.setType(
            com.silenteight.datasource.categories.api.v1.CategoryType.valueOf(v.name())));

    return builder.build();
  }
}
