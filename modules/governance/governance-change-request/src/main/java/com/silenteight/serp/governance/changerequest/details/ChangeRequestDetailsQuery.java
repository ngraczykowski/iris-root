package com.silenteight.serp.governance.changerequest.details;

import lombok.NonNull;

import com.silenteight.serp.governance.changerequest.details.dto.ChangeRequestDetailsDto;

import java.util.UUID;

public interface ChangeRequestDetailsQuery {

  ChangeRequestDetailsDto details(@NonNull UUID changeRequestId);
}
