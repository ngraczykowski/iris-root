package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.users.user.UserMappingFinder.InvalidMappingResultCountException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserMappingFinderTest {

  private static final String USER_NAME_1 = "userName1";
  private static final String USER_NAME_2 = "userName2";
  private static final String NOT_EXISTING_USER = "userName3";
  private static final List<User> USERS_COLLECTION = asList(createUser(1L, USER_NAME_1, false),
                                                            createUser(2L, USER_NAME_2, false),
                                                            createUser(9L, USER_NAME_1, true));
  @Mock
  private UserRepository userRepository;

  private UserMappingFinder underTest;

  @Before
  public void setUp() {
    underTest = new UserMappingFinder(userRepository);

    when(userRepository.findByUserName(any())).thenReturn(USERS_COLLECTION.stream());
    when(userRepository.findById(any())).thenReturn(USERS_COLLECTION.stream());
  }

  @Test
  public void givenUserNames_returnMatchingUserIds() {
    Map<String, Long> userNameMap = underTest.mapUserNameToUserId(asList(USER_NAME_1, USER_NAME_2));

    assertThat(userNameMap)
        .hasSize(2)
        .containsValues(1L, 2L);
  }

  @Test
  public void givenUserIds_returnMatchingUserName() {
    Map<Long, String> userNameMap = underTest.mapUserIdToUserName(asList(1L, 2L, 9L));

    assertThat(userNameMap)
        .hasSize(3)
        .containsValues(USER_NAME_1, USER_NAME_2, USER_NAME_1);
  }

  @Test
  public void givenNotMatchingUserNames_throwInvalidMappingResultCountException() {
    assertThatThrownBy(
        () -> underTest.mapUserNameToUserId(asList(USER_NAME_1, USER_NAME_1, NOT_EXISTING_USER)))
        .isInstanceOf(InvalidMappingResultCountException.class);
  }

  @Test
  public void givenNotMatchingUserIds_throwInvalidMappingResultCountException() {
    assertThatThrownBy(() -> underTest.mapUserIdToUserName(asList(1L, 2L, 3L))).isInstanceOf(
        InvalidMappingResultCountException.class);
  }

  private static User createUser(Long userId, String userName, boolean deleted) {
    User user = Mockito.mock(User.class);
    when(user.getUserName()).thenReturn(userName);
    when(user.getId()).thenReturn(userId);
    when(user.isDeleted()).thenReturn(deleted);
    return user;
  }
}
