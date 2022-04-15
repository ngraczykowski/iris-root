package com.silenteight.serp.governance.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.approval.ModelApprovalQuery;
import com.silenteight.serp.governance.changerequest.approval.dto.ModelApprovalDto;
import com.silenteight.serp.governance.changerequest.approve.ChangeRequestModelQuery;
import com.silenteight.serp.governance.changerequest.details.ChangeRequestDetailsQuery;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestNotFoundException;
import com.silenteight.serp.governance.changerequest.domain.exception.ModelApprovalNotFoundException;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ChangeRequestQuery implements
    ListChangeRequestsQuery,
    ChangeRequestDetailsQuery,
    ChangeRequestModelQuery,
    ModelApprovalQuery {

  @NonNull
  private final ChangeRequestRepository repository;

  @Override
  public Collection<ChangeRequestDto> listByStates(Set<ChangeRequestState> states) {
    return repository
        .findAllByStateInOrderByDecidedAtDesc(states)
        .stream()
        .map(ChangeRequest::toDto)
        .collect(toList());
  }

  @Override
  public Collection<ChangeRequestDto> listByModelNames(Set<String> modelNames) {
    return repository
        .findAllByModelNameIn(modelNames)
        .stream()
        .map(ChangeRequest::toDto)
        .collect(toList());
  }

  @Override
  public ChangeRequestDto details(@NonNull UUID changeRequestId) {
    return repository
        .findByChangeRequestId(changeRequestId)
        .map(ChangeRequest::toDto)
        .orElseThrow(() -> new ChangeRequestNotFoundException(changeRequestId));
  }


  @Override
  public String getModel(@NonNull UUID changeRequestId) {
    return details(changeRequestId).getModelName();
  }

  @Override
  public ModelApprovalDto getApproval(@NonNull String modelName) {
    return repository
        .findAllByModelName(modelName)
        .stream()
        .filter(ChangeRequest::isApproved)
        .max(Comparator.comparing(ChangeRequest::getDecidedAt))
        .map(ChangeRequest::toModelApprovalDto)
        .orElseThrow(() -> new ModelApprovalNotFoundException(modelName));
  }
}
