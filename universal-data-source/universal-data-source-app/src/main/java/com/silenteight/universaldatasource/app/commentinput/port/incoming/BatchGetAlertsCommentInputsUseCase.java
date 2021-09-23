package com.silenteight.universaldatasource.app.commentinput.port.incoming;

import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsResponse;

import java.util.List;

public interface BatchGetAlertsCommentInputsUseCase {

  BatchGetAlertsCommentInputsResponse batchGetAlertsCommentInputs(List<String> alertList);

}
