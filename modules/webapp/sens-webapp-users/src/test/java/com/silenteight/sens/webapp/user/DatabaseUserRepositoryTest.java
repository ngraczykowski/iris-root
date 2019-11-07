package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.adapter.user.JpaUserRepository;
import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.common.support.audit.AuditReaderProvider;
import com.silenteight.sens.webapp.domain.user.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseUserRepositoryTest {

  private static final String USER_NAME_1 = "user_name_1";
  private static final String USER_NAME_2 = "user_name_2";
  private static final int PAGE_SIZE = 10;

  @Mock
  private JpaUserRepository jpaUserRepository;
  @Mock
  private AuditReaderProvider auditReaderProvider;

  private UserRepository userRepository;

  @Before
  public void setUp() {
    userRepository = new DatabaseUserRepository(jpaUserRepository, auditReaderProvider);
  }

  @Test
  public void givenNoUsers_findNoUsers() {
    //given
    Pageable pageable = getPageable();
    when(jpaUserRepository.findByDeletedAtIsNullOrderByUserNameAsc(pageable))
        .thenReturn(new PageImpl<>(emptyList()));

    //when
    PageableResult<User> result = userRepository.find(pageable);

    //then
    verify(jpaUserRepository, times(1))
        .findByDeletedAtIsNullOrderByUserNameAsc(pageable);
    assertThat(result.getTotal()).isEqualTo(0);
    assertThat(result.getResults()).isEmpty();
  }

  @Test
  public void givenUsers_findUsersOrderByUserNameAsc() {
    //given
    Pageable pageable = getPageable();
    List<User> expectedUsers = getUsers();
    when(jpaUserRepository.findByDeletedAtIsNullOrderByUserNameAsc(pageable))
        .thenReturn(new PageImpl<>(expectedUsers));

    //when
    PageableResult<User> result = userRepository.find(pageable);

    //then
    verify(jpaUserRepository, times(1))
        .findByDeletedAtIsNullOrderByUserNameAsc(pageable);
    assertThat(result.getTotal()).isEqualTo(2);
    List<User> currentUsers = result.getResults();
    assertThat(currentUsers).isEqualTo(expectedUsers);
  }

  @Test
  public void givenNotExistingUser_returnEmptyOptional() {
    //given
    when(jpaUserRepository.findOneByDeletedAtIsNullAndUserName(any())).thenReturn(empty());

    //when
    Optional<User> empty = userRepository.findOneByUserName("not_existing_user");

    //then
    assertThat(empty).isEmpty();
  }

  @Test
  public void givenExistingUser_returnUserEntity() {
    //given
    when(jpaUserRepository.findOneByDeletedAtIsNullAndUserName(any()))
        .thenReturn(of(new User(USER_NAME_1)));

    //when
    Optional<User> user = userRepository.findOneByUserName("not_existing_user");

    //then
    assertThat(user)
        .isPresent()
        .get()
        .extracting(User::getUserName)
        .isEqualTo(USER_NAME_1);
  }

  @Test
  public void givenExistingUserId_returnUserEntity() {
    //given
    when(jpaUserRepository.getOne(any())).thenReturn(new User(USER_NAME_1));

    //when
    User user = userRepository.getOne(1L);

    //then
    assertThat(user)
        .isNotNull()
        .extracting(User::getUserName)
        .isEqualTo(USER_NAME_1);
  }

  @Test
  public void savingUser_willSaveToJpaRepository() {
    //given
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    when(jpaUserRepository.save(captor.capture())).thenAnswer(p -> p.getArguments()[0]);
    User user = new User(USER_NAME_1);

    //when
    User savedUser = userRepository.save(user);

    //then
    assertThat(captor.getValue()).isEqualTo(savedUser);
    assertThat(captor.getValue()).extracting(User::getUserName).isEqualTo(USER_NAME_1);
  }

  private static Pageable getPageable() {
    return PageRequest.of(0, PAGE_SIZE);
  }

  private static List<User> getUsers() {
    return Arrays.asList(new User(USER_NAME_1), new User(USER_NAME_2));
  }

  @Test
  public void givenUsers_findAllUsersOrderByUserName() {
    //given
    List<User> expectedUsers = getUsers();
    when(jpaUserRepository.findByDeletedAtIsNullOrderByUserNameAsc()).thenReturn(expectedUsers);

    //when
    List<User> currentUsers = userRepository.findAllOrderByUserName();

    //then
    verify(jpaUserRepository, times(1))
        .findByDeletedAtIsNullOrderByUserNameAsc();
    assertThat(currentUsers).isEqualTo(expectedUsers);
  }

  @Test
  public void givenUsers_findAllByUserId() {
    //given
    List<User> expectedUsers = getUsers();
    List<Long> userIds = expectedUsers.stream().map(User::getId).collect(toList());
    when(jpaUserRepository.findById(userIds)).thenReturn(expectedUsers.stream());

    //when
    Stream<User> currentUsers = userRepository.findByUserId(userIds);

    //then
    assertThat(currentUsers).isEqualTo(expectedUsers);
  }
}
