package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.scb.domain.Alert;

import java.util.Optional;

public interface CreateAlert {

  Optional<Alert> create();
}
