package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.datasource.categories.api.v2.ListCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;
import com.silenteight.hsbc.datasource.category.dto.CategoryDto;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
@Slf4j
class CategoryGrpcService extends CategoryServiceImplBase {

  private final ListCategoriesUseCase listCategoriesUseCase;

  @Override
  public void listCategories(ListCategoriesRequest request, StreamObserver<ListCategoriesResponse> responseObserver) {
    responseObserver.onNext(getCategoriesResponse());
    responseObserver.onCompleted();
  }

  private ListCategoriesResponse getCategoriesResponse() {
    var categories = listCategoriesUseCase.getCategories();

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
        .ifPresent(v -> builder.setType(CategoryType.valueOf(v.name())));

    return builder.build();
  }
}
