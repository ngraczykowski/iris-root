package com.silenteight.sens.webapp.backend.changerequest.pending;

import java.util.List;

public interface PendingChangeRequestQuery {

  List<PendingChangeRequestDto> listPending();
}
