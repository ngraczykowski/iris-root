package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.common.testing.BaseDataJpaTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserConfiguration.class)
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureTestDatabase(replace = NONE)
public class UserRepositoryIT extends BaseDataJpaTest {

  private static final String USER_NAME_1 = "user_name_1";
  private static final String USER_NAME_2 = "user_name_2";
  private static final String NON_EXISTING_USER_NAME = "non_existent";
  private static final int PAGE_SIZE = 10;

  @Autowired
  private UserRepository userRepository;

  private User user1;
  private User user2;

  @Before
  public void setUp() {
    user1 = entityManager.persistAndFlush(new User(USER_NAME_1));
    user2 = entityManager.persistAndFlush(new User(USER_NAME_2));
  }

  @Test
  public void givenNotExistingUserName_userNotFound() {
    Optional<User> notExistingUser = userRepository
        .findOneByDeletedAtIsNullAndUserName("dummy-user");

    assertThat(notExistingUser).isEmpty();
  }

  @Test
  public void givenExistingUserName_returnUser() {
    Optional<User> existingUser = userRepository.findOneByDeletedAtIsNullAndUserName(USER_NAME_1);

    assertThat(existingUser).isNotEmpty();
    assertThat(existingUser.get().getUserName()).isEqualTo(USER_NAME_1);
  }

  @Test
  public void givenUsers_returnPageOfUsersOrderByUserNameAsc() {
    Page<User> page = userRepository.findByDeletedAtIsNullOrderByUserNameAsc(getPageable());

    assertThat(page.getTotalElements()).isEqualTo(2);
    List<User> users = page.getContent();
    assertThat(users).containsExactly(user1, user2);
  }

  private static Pageable getPageable() {
    return PageRequest.of(0, PAGE_SIZE);
  }

  @Test
  public void givenDeleteExistingUser_returnOneAsNumberOfAffectedRecords() {
    int countAffected = userRepository.delete(Instant.now(), USER_NAME_1);

    assertThat(countAffected).isEqualTo(1);
  }

  @Test
  public void givenDeleteNonExistingUser_returnZeroAsNumberOfAffectedRecords() {
    int countAffected = userRepository.delete(Instant.now(), NON_EXISTING_USER_NAME);

    assertThat(countAffected).isEqualTo(0);
  }

  @Test
  public void givenUsers_returnAllUsersOrderByUserNameAsc() {
    List<User> users = userRepository.findByDeletedAtIsNullOrderByUserNameAsc();

    assertThat(users).containsExactly(user1, user2);
  }
}
