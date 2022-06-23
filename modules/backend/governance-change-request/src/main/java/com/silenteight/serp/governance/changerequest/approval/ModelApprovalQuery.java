package com.silenteight.serp.governance.changerequest.approval;

import lombok.NonNull;

import com.silenteight.serp.governance.changerequest.approval.dto.ModelApprovalDto;

public interface ModelApprovalQuery {

  ModelApprovalDto getApproval(@NonNull String modelName);
}
