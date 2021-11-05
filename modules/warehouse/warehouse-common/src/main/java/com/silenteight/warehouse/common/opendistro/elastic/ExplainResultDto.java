package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.*;

import com.silenteight.warehouse.common.opendistro.elastic.exception.EmptyExplainRequestException;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ExplainResultDto {

  @NonNull
  private Root root;

  String getRequest() {
    Child child = root.getChildren().get(0);
    String request = child.getDescription().getRequest();
    while (isEmpty(request) && !child.getChildren().isEmpty()) {
      child = child.getChildren().get(0);
      request = child.getDescription().getRequest();
    }

    if (isEmpty(request))
      throw new EmptyExplainRequestException();

    return request;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class Root {

    @NonNull
    private String name;
    @NonNull
    private RootDescription description;
    @NonNull
    private List<Child> children;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class RootDescription {

    @NonNull
    private String fields;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class Child {

    private List<Child> children;
    private String name;
    private ChildDescription description;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class ChildDescription {

    private int limit;
    private int offset;
    private String request;
  }
}
