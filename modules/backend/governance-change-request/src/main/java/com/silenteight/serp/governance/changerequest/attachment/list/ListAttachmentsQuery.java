package com.silenteight.serp.governance.changerequest.attachment.list;

import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface ListAttachmentsQuery {

  List<String> list(@NonNull UUID changeRequestId);
}
