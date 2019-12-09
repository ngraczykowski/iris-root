package com.silenteight.sens.webapp.users.bulk;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.bulk.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.users.bulk.dto.Analyst;
import com.silenteight.sens.webapp.users.bulk.dto.UpdatedUser;
import com.silenteight.sens.webapp.users.user.User;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_ANALYST;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_DECISION_TREE_VIEWER;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnalystSynchronizerTest {

  private static final String USER_NAME_1 = "username_1";
  private static final String USER_NAME_2 = "username_2";
  private static final String USER_NAME_3 = "username_3";
  private static final String DESCRIPTION_1 = "description_1";
  private static final String DESCRIPTION_2 = "description_2";
  private static final String DESCRIPTION_3 = "description_3";

  private static final long USER_ID_1 = 1;
  private static final long USER_ID_2 = 2;
  private static final long USER_ID_3 = 3;

  private AnalystSynchronizer classUnderTest;

  @Before
  public void setUp() {
    classUnderTest = new AnalystSynchronizer();
  }

  @Test
  public void givenNoUsers_synchronizeAnalysts() {
    // when
    List<Analyst> analysts = asList(
        new Analyst(USER_NAME_1, DESCRIPTION_1),
        new Analyst(USER_NAME_2, DESCRIPTION_2),
        new Analyst(USER_NAME_3, DESCRIPTION_3));

    SynchronizedAnalysts result = classUnderTest.synchronize(emptyList(), analysts);

    // then
    assertThat(result.getAdded()).isEqualTo(analysts);
    assertThat(result.getMissingRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  public void givenAnalyst_synchronizeAnalysts() {
    // when
    List<Analyst> analysts = asList(
        new Analyst(USER_NAME_1, DESCRIPTION_1),
        new Analyst(USER_NAME_2, DESCRIPTION_2),
        new Analyst(USER_NAME_3, DESCRIPTION_3));

    SynchronizedAnalysts result = classUnderTest.synchronize(
        singletonList(makeAnalyst(USER_ID_1, USER_NAME_1, DESCRIPTION_1)), analysts);

    // then
    assertThat(result.getAdded()).extracting(Analyst::getLogin).contains(USER_NAME_2, USER_NAME_3);
    assertThat(result.getMissingRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  public void givenThreeAnalysts_synchronizeAnalysts() {
    // when
    List<Analyst> analysts = asList(
        new Analyst(USER_NAME_1, DESCRIPTION_1),
        new Analyst(USER_NAME_2, DESCRIPTION_2),
        new Analyst(USER_NAME_3, DESCRIPTION_3));

    SynchronizedAnalysts result = classUnderTest.synchronize(
        asList(
            makeAnalyst(USER_ID_1, USER_NAME_1, DESCRIPTION_1),
            makeAnalyst(USER_ID_2, USER_NAME_2, DESCRIPTION_2),
            makeAnalyst(USER_ID_3, USER_NAME_3, DESCRIPTION_3)),
        analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getMissingRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  public void givenTwoAnalysts_synchronizeAnalysts() {
    // when
    List<Analyst> analysts = asList(
        new Analyst(USER_NAME_1, DESCRIPTION_1),
        new Analyst(USER_NAME_3, DESCRIPTION_3));

    SynchronizedAnalysts result = classUnderTest.synchronize(
        asList(
            makeAnalyst(USER_ID_1, USER_NAME_1, DESCRIPTION_1),
            makeAnalyst(USER_ID_2, USER_NAME_2, DESCRIPTION_2)),
        analysts);

    // then
    assertThat(result.getAdded()).extracting(Analyst::getLogin).contains(USER_NAME_3);
    assertThat(result.getMissingRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEqualTo(singletonList(USER_NAME_2));
  }

  @Test
  public void givenUserAndAnalyst_synchronizeAnalysts() {
    // when
    List<Analyst> analysts = asList(
        new Analyst(USER_NAME_1, DESCRIPTION_1),
        new Analyst(USER_NAME_2, DESCRIPTION_2),
        new Analyst(USER_NAME_3, DESCRIPTION_3));

    SynchronizedAnalysts result = classUnderTest.synchronize(
        asList(
            makeAnalyst(USER_ID_1, USER_NAME_1, DESCRIPTION_1),
            makeUser(USER_ID_2, USER_NAME_2, null, ROLE_DECISION_TREE_VIEWER)),
        analysts);

    // then
    assertThat(result.getAdded()).extracting(Analyst::getLogin).contains(USER_NAME_3);
    assertThat(result.getMissingRole()).isEqualTo(singletonList(USER_ID_2));
    assertThat(result.getUpdatedDisplayName())
        .extracting(UpdatedUser::getUserId).contains(USER_ID_2);
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  public void givenAnalystWithDifferentDisplayName_synchronizeAnalysts() {
    // when
    List<Analyst> analysts = singletonList(new Analyst(USER_NAME_1, "new_description"));

    SynchronizedAnalysts result = classUnderTest.synchronize(
        asList(makeAnalyst(USER_ID_1, USER_NAME_1, DESCRIPTION_1)), analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getMissingRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName())
        .extracting(UpdatedUser::getUserId).contains(USER_ID_1);
    assertThat(result.getDeleted()).isEmpty();
  }

  private User makeAnalyst(long userId, String userName, String displayName) {
    User user = makeUser(userId, userName, displayName, ROLE_ANALYST);
    when(user.hasOnlyRole(ROLE_ANALYST)).thenReturn(true);

    return user;
  }

  private User makeUser(long userId, String userName, String displayName, Role role) {
    User user = mock(User.class);
    when(user.getId()).thenReturn(userId);
    when(user.getUserName()).thenReturn(userName);
    when(user.isExternalUser()).thenReturn(true);
    when(user.hasRole(role)).thenReturn(true);
    when(user.getDisplayName()).thenReturn(displayName);

    return user;
  }
}
