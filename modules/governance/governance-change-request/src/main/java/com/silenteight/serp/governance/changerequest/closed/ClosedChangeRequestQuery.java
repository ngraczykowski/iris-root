package com.silenteight.serp.governance.changerequest.closed;

import com.silenteight.serp.governance.changerequest.closed.dto.ClosedChangeRequestDto;

import java.util.List;

public interface ClosedChangeRequestQuery {

  List<ClosedChangeRequestDto> listClosed();
}
