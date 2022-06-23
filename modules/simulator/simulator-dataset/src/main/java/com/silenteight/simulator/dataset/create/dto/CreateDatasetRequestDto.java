package com.silenteight.simulator.dataset.create.dto;

import lombok.*;

import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;

import java.util.UUID;
import javax.validation.constraints.Pattern;

import static com.silenteight.serp.governance.common.web.rest.RestValidationConstants.FIELD_REGEX;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatasetRequestDto {

  @NonNull
  private UUID id;
  @NonNull
  @Pattern(regexp = FIELD_REGEX)
  private String datasetName;
  @Pattern(regexp = FIELD_REGEX)
  private String description;
  @NonNull
  private AlertSelectionCriteriaDto query;

  public boolean useMultiHitAlerts() {
    return query.getUseMultiHitAlerts();
  }
}
