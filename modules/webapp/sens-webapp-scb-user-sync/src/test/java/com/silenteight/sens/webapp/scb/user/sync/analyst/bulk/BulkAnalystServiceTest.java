package com.silenteight.sens.webapp.scb.user.sync.analyst.bulk;

import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkCreateAnalystsRequest.NewAnalyst;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.BulkDeleteAnalystsRequest;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase.RegisterExternalUserCommand;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase;
import com.silenteight.sep.usermanagement.api.error.UserDomainError;

import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase.Success;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkAnalystServiceTest {

  @Mock
  private RegisterExternalUserUseCase registerExternalUserUseCase;
  @Mock
  private UnlockUserUseCase unlockUserUseCase;
  @Mock
  private AddRolesToUserUseCase addRolesToUserUseCase;
  @Mock
  private UpdateUserDisplayNameUseCase updateUserDisplayNameUseCase;
  @Mock
  private RemoveUserUseCase removeUserUseCase;

  @InjectMocks
  private BulkAnalystService bulkAnalystService;

  @Captor
  private ArgumentCaptor<RegisterExternalUserCommand> registerExternalUserCommandCaptor;

  @Test
  void invokeCommandOnUseCase_create() {
    when(registerExternalUserUseCase.apply(any(RegisterExternalUserCommand.class)))
        .thenReturn(right());

    String name1 = "nameA";
    String name2 = "nameB";
    String displayName1 = "display name AA";
    String displayName2 = "display name BB";

    BulkCreateAnalystsRequest request = new BulkCreateAnalystsRequest(
        List.of(
            new NewAnalyst(name1, displayName1),
            new NewAnalyst(name2, displayName2)));

    bulkAnalystService.create(request);

    verify(registerExternalUserUseCase, times(2))
        .apply(registerExternalUserCommandCaptor.capture());

    final List<RegisterExternalUserCommand> invokedCommands =
        registerExternalUserCommandCaptor.getAllValues();

    assertThat(invokedCommands.get(0).getUsername()).isEqualTo(name1);
    assertThat(invokedCommands.get(0).getDisplayName()).isEqualTo(displayName1);

    assertThat(invokedCommands.get(1).getUsername()).isEqualTo(name2);
    assertThat(invokedCommands.get(1).getDisplayName()).isEqualTo(displayName2);
  }

  @Test
  void returnSuccessResponseIfUseCaseReturnsRight_create() {
    when(registerExternalUserUseCase.apply(any(RegisterExternalUserCommand.class)))
        .thenReturn(right());

    final BulkResult result = bulkAnalystService.create(requestWithAnalystsNumberOf(1));

    assertThat(result.asMessage()).isEqualTo("1 / 1");
  }

  @Test
  void returnSuccessResponseIfUseCaseReturnsLeft_create() {
    String errorMsg = "error importing John Doe";
    when(registerExternalUserUseCase.apply(any(RegisterExternalUserCommand.class)))
        .thenReturn(leftWithMessage(errorMsg));

    final BulkResult result = bulkAnalystService.create(requestWithAnalystsNumberOf(1));

    assertThat(result.asMessage()).isEqualTo("0 / 1");
    assertThat(result.errorMessagesWithMaxSizeOf(10)).contains(errorMsg);
  }

  @Test
  void returnTwoRequestsSucceedingOutOfFour_create() {
    String errorMsg1 = "some error occurred";
    String errorMsg2 = "some other error occurred";
    when(registerExternalUserUseCase.apply(any(RegisterExternalUserCommand.class))).thenReturn(
        right(),
        leftWithMessage(errorMsg1), right(),
        leftWithMessage(errorMsg2));

    final BulkResult result = bulkAnalystService.create(requestWithAnalystsNumberOf(4));

    assertThat(result.asMessage()).isEqualTo("2 / 4");
    assertThat(result.errorMessagesWithMaxSizeOf(10)).contains(errorMsg1, errorMsg2);
  }

  @Test
  void invokeRemoveCommand() {
    String user1 = "user1";
    String user2 = "user2";
    bulkAnalystService.delete(new BulkDeleteAnalystsRequest(List.of(user1, user2)));

    ArgumentCaptor<RemoveUserCommand> commandCaptor =
        ArgumentCaptor.forClass(RemoveUserCommand.class);

    verify(removeUserUseCase, times(2)).apply(commandCaptor.capture());

    List<RemoveUserCommand> removeUserCommand = commandCaptor.getAllValues();
    assertThat(removeUserCommand.get(0).getUsername()).isEqualTo(user1);
  }

  private BulkCreateAnalystsRequest requestWithAnalystsNumberOf(int numberOfAnalysts) {
    List<NewAnalyst> analysts = IntStream.range(0, numberOfAnalysts)
        .mapToObj(i -> new NewAnalyst("name" + i, "display name" + i))
        .collect(Collectors.toList());

    return new BulkCreateAnalystsRequest(analysts);
  }

  private Either<UserDomainError, Success> right() {
    return Either.right(() -> "");
  }

  private Either<UserDomainError, Success> leftWithMessage(String errorMsg) {
    return Either.left(() -> errorMsg);
  }
}
