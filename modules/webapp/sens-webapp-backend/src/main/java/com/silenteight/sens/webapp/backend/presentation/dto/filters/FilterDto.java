package com.silenteight.sens.webapp.backend.presentation.dto.filters;

import lombok.Data;

@Data
public class FilterDto {

  private final long id;
  private final String name;
  private final String query;

  public FilterDto(Filter filter) {
    id = filter.getId();
    name = filter.getName();
    query = filter.getQuery();
  }

  @Data
  private static class Filter {

    private final long id;
    private final String name;
    private final String query;
  }
}
