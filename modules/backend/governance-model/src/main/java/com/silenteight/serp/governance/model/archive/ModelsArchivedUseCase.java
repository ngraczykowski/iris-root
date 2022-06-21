package com.silenteight.serp.governance.model.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelsArchived;
import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestCommand;
import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestUseCase;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;
import com.silenteight.serp.governance.model.archive.amqp.listener.ModelsArchivedMessageHandler;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Set.copyOf;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class ModelsArchivedUseCase implements ModelsArchivedMessageHandler {

  static final String CANCELLED_BY = "archived_model";

  @NonNull
  private final ListChangeRequestsQuery listChangeRequestsQuery;
  @NonNull
  private final CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @Override
  public void handle(@NonNull ModelsArchived message) {
    Collection<String> modelNames = message.getModelsList();
    log.info("Archived models: modelNames={}" + modelNames);

    List<ChangeRequestDto> changeRequests = listChangeRequestsToCancel(copyOf(modelNames));
    log.debug("Change requests to be cancelled: changeRequests={}" + changeRequests);

    changeRequests
        .stream()
        .map(ModelsArchivedUseCase::toCancelChangeRequestCommand)
        .forEach(cancelChangeRequestUseCase::activate);

    log.info("Change requests cancelled: changeRequests={}" + changeRequests);
  }

  private List<ChangeRequestDto> listChangeRequestsToCancel(Set<String> modelNames) {
    return listChangeRequestsQuery
        .listByModelNames(modelNames)
        .stream()
        .peek(changeRequest -> log.debug("Found changeRequest {} in state {} related with {}",
            changeRequest.getId(), changeRequest.getState(), changeRequest.getModelName()))
        .filter(ChangeRequestDto::isPending)
        .collect(toList());
  }

  private static CancelChangeRequestCommand toCancelChangeRequestCommand(
      ChangeRequestDto changeRequest) {

    return CancelChangeRequestCommand.builder()
        .id(changeRequest.getId())
        .cancellerUsername(CANCELLED_BY)
        .build();
  }
}
