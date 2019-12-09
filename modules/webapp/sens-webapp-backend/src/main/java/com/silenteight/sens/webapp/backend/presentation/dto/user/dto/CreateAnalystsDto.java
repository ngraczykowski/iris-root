package com.silenteight.sens.webapp.backend.presentation.dto.user.dto;

import lombok.*;

import com.silenteight.sens.webapp.users.bulk.dto.Analyst;
import com.silenteight.sens.webapp.users.bulk.dto.BulkCreateAnalystsRequest;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAnalystsDto {

  private List<AnalystDto> analysts;

  public BulkCreateAnalystsRequest toDomainRequest() {
    return new BulkCreateAnalystsRequest(mapToAnalysts());
  }

  private List<Analyst> mapToAnalysts() {
    return analysts
        .stream()
        .map(AnalystDto::mapToAnalyst)
        .collect(toList());
  }
}
