package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.user.dto.CreateUserRequest;
import com.silenteight.sens.webapp.user.dto.UpdateUserRequest;
import com.silenteight.sens.webapp.user.exception.UserAlreadyExistException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_DECISION_TREE_MANAGER;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_DECISION_TREE_VIEWER;
import static java.util.Collections.singletonList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private static final long DUMMY_USER_ID = 1L;
  private static final String CREATED_USER_1_NAME = "userName";
  private static final String CREATED_USER_2_NAME = "otherUserName";
  private static final String CREATED_USER_1_PASS = "userPassword";
  private static final String CREATED_USER_2_PASS = "otherUserPassword";
  private static final Role CREATED_ROLE = ROLE_DECISION_TREE_VIEWER;
  private static final List<Role> USER_ROLES = singletonList(ROLE_DECISION_TREE_VIEWER);
  private static final Role UPDATED_ROLE = ROLE_DECISION_TREE_MANAGER;
  private static final List<Role> UPDATED_USER_ROLES = singletonList(UPDATED_ROLE);
  private static final String DISPLAY_NAME = "displayName";

  @Mock
  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder = new TestPasswordEncoder();

  private UserService classUnderTest;

  @Before
  public void setUp() {
    User dummyUser = createUser(CREATED_USER_2_NAME, CREATED_USER_2_PASS);
    when(userRepository.save(any(User.class))).thenReturn(dummyUser);

    classUnderTest = new UserService(userRepository, passwordEncoder);
  }

  @Test
  public void forGivenNamePasswordRolesShouldCreateLocalUser() {
    // given
    CreateUserRequest request = makeCreateUserRequest(CREATED_USER_1_PASS);
    User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);

    // when
    classUnderTest.create(request);

    // then
    verify(userRepository, times(1)).save(user);
    assertThat(user.getUserName()).isEqualTo(CREATED_USER_1_NAME);
    assertThat(user.getDisplayName()).isEqualTo(DISPLAY_NAME);
    assertThat(user.getActiveRoles()).containsAll(USER_ROLES);
    assertThat(user.isActive()).isTrue();
    assertThat(user.isInactive()).isFalse();
    assertThat(user.isLocalUser()).isTrue();
    assertThat(user.hasRole(CREATED_ROLE)).isTrue();
  }

  @Test
  public void forGivenNameShouldCreateExternalUser() {
    // given
    CreateUserRequest request = makeCreateUserRequest(null);
    User user = createUser(CREATED_USER_1_NAME, null);

    // when
    classUnderTest.create(request);

    // then
    verify(userRepository, times(1)).save(user);
    assertThat(user.getUserName()).isEqualTo(CREATED_USER_1_NAME);
    assertThat(user.getDisplayName()).isEqualTo(DISPLAY_NAME);
    assertThat(user.isActive()).isTrue();
    assertThat(user.isExternalUser()).isTrue();
    assertThat(user.hasRole(CREATED_ROLE)).isTrue();
  }

  @Test
  public void forExistingUserNameShouldThrowsUserAlreadyExistException() {
    // given
    CreateUserRequest request = makeCreateUserRequest(CREATED_USER_1_PASS);
    User user = mock(User.class);
    when(userRepository.findOneByUserName(CREATED_USER_1_NAME))
        .thenReturn(Optional.of(user));

    // then
    assertThatThrownBy(
        () -> classUnderTest.create(request))
        .isInstanceOf(UserAlreadyExistException.class);
  }

  @Test
  public void forExistingUserShouldUpdateGivenFields() {
    // given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);

    // when
    UpdateUserRequest request = UpdateUserRequest
        .builder().userId(DUMMY_USER_ID).roles(UPDATED_USER_ROLES).build();

    classUnderTest.update(request);

    // then
    verify(userRepository, times(1)).save(user);
    assertThat(user.hasRole(UPDATED_ROLE)).isTrue();
    assertThat(user.hasOnlyRole(UPDATED_ROLE)).isTrue();
  }

  @Test
  public void shouldActivateUserIfActivatedFlagIsSetToTrue() {
    // given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);

    // when
    UpdateUserRequest request =
        UpdateUserRequest.builder().userId(DUMMY_USER_ID).active(true).build();
    classUnderTest.update(request);

    // then
    verify(userRepository, times(1)).save(getActivatedUser(user));
  }

  @Test
  public void shouldDeactivateUserIfActivatedFlagIsSetToFalse() {
    // given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);

    // when
    UpdateUserRequest request =
        UpdateUserRequest.builder().userId(DUMMY_USER_ID).active(false).build();
    classUnderTest.update(request);

    // then
    verify(userRepository, times(1)).save(getDeactivatedUser(user));
  }

  @Test
  public void shouldPromoteToSuperUserIfSuperUserFlagIsSetToTrue() {
    // given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    user.relegateToUser();
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);

    // when
    UpdateUserRequest request =
        UpdateUserRequest.builder().userId(DUMMY_USER_ID).superUser(true).build();
    classUnderTest.update(request);

    // then
    assertThat(user.isSuperUser()).isTrue();
  }

  @Test
  public void shouldRelegateToUserIfSuperUserFlagIsSetToFalse() {
    // given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    user.promoteToSuperUser();
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);

    // when
    UpdateUserRequest request = UpdateUserRequest
        .builder().userId(DUMMY_USER_ID).superUser(false).build();
    classUnderTest.update(request);

    // then
    assertThat(user.isSuperUser()).isFalse();
  }

  @Test
  public void shouldSetDisplayName() {
    // given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    user.promoteToSuperUser();
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);

    // when
    UpdateUserRequest request =
        UpdateUserRequest.builder().userId(DUMMY_USER_ID).displayName(DISPLAY_NAME).build();
    classUnderTest.update(request);

    // then
    assertThat(user.getDisplayName()).isEqualTo(DISPLAY_NAME);
  }

  @Test
  public void findAllById() {
    //given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    when(userRepository.findByUserId(singletonList(user.getId()))).thenReturn(of(user));

    //when
    List<User> users = classUnderTest.findAll(singletonList(user.getId()));

    //then
    assertThat(users).containsExactly(user);
  }

  @Test
  public void userLastLoginAtFieldIsSet() {
    //given
    final User user = createUser(CREATED_USER_1_NAME, CREATED_USER_1_PASS);
    when(userRepository.getOne(DUMMY_USER_ID)).thenReturn(user);
    assertThat(user.getLastLoginAt()).isNull();

    //when
    classUnderTest.userLoggedIn(DUMMY_USER_ID);

    //then
    assertThat(user.getLastLoginAt()).isAfterOrEqualTo(Instant.now().minusSeconds(60));
  }

  private CreateUserRequest makeCreateUserRequest(String password) {
    return CreateUserRequest
        .builder()
        .name(CREATED_USER_1_NAME)
        .password(password)
        .displayName(DISPLAY_NAME)
        .roles(USER_ROLES)
        .build();
  }

  private User getActivatedUser(User user) {
    user.activate();
    return user;
  }

  private User getDeactivatedUser(User user) {
    user.deactivate();
    return user;
  }

  private User createUser(String userName, String userPassword) {
    User user = new User(userName);
    if (!StringUtils.isEmpty(userPassword))
      user.changePassword(passwordEncoder.encode(userPassword));
    user.setDisplayName(DISPLAY_NAME);
    user.changeRoles(USER_ROLES);
    return user;
  }

  private class TestPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
      return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String password) {
      return charSequence.toString().equals(password);
    }
  }
}
