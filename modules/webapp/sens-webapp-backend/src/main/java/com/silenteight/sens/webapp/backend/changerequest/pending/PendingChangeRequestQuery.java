package com.silenteight.sens.webapp.backend.changerequest.pending;

import com.silenteight.sens.webapp.backend.changerequest.domain.dto.ChangeRequestDto;

import java.util.List;

public interface PendingChangeRequestQuery {

  List<ChangeRequestDto> listPending();
}
