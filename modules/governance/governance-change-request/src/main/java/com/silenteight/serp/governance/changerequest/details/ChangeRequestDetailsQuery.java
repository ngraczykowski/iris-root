package com.silenteight.serp.governance.changerequest.details;

import lombok.NonNull;

import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import java.util.UUID;

public interface ChangeRequestDetailsQuery {

  ChangeRequestDto details(@NonNull UUID changeRequestId);
}
