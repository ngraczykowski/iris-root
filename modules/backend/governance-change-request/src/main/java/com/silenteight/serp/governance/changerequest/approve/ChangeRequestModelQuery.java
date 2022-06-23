package com.silenteight.serp.governance.changerequest.approve;

import lombok.NonNull;

import java.util.UUID;

public interface ChangeRequestModelQuery {

  String getModel(@NonNull UUID id);
}
