package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
@Data
public class RecommendationDto {

  private final String externalId;
  private final String discriminator;
  private final String decision;
  private final String comment;
  private final OffsetDateTime date;
}
