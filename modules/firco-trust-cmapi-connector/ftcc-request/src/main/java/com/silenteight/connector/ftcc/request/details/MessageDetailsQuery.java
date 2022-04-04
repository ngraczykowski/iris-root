package com.silenteight.connector.ftcc.request.details;

import lombok.NonNull;

import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;

import java.util.List;
import java.util.UUID;

public interface MessageDetailsQuery {

  List<MessageDetailsDto> details(@NonNull UUID batchId);
}
