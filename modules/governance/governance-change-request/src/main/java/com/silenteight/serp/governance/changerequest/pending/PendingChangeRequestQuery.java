package com.silenteight.serp.governance.changerequest.pending;

import com.silenteight.serp.governance.changerequest.pending.dto.PendingChangeRequestDto;

import java.util.List;

public interface PendingChangeRequestQuery {

  List<PendingChangeRequestDto> listPending();
}
