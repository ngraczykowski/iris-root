package com.silenteight.sens.webapp.backend.changerequest.rest;

import com.silenteight.sens.webapp.backend.changerequest.rest.dto.ChangeRequestDto;

import java.util.List;

public interface ChangeRequestQuery {

  List<ChangeRequestDto> pending();
}
