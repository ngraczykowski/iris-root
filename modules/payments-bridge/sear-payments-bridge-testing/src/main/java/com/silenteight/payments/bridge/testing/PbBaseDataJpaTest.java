package com.silenteight.payments.bridge.testing;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.springframework.context.annotation.Import;

/**
 * A base class for all Spring Data JPA integration tests.
 * <p/>
 * Use it like this:
 * <pre>
 *   class SomeRepositoryIT extends PbBaseDataJpaTest {
 *
 *     &#64;Autowired
 *     SomeRepository repository;
 *
 *     &#64;Test
 *     void repositoryTest() {
 *       // assertThat(...)...
 *     }
 *   }
 * </pre>
 */
@Import(RepositoryTestConfiguration.class)
public class PbBaseDataJpaTest extends BaseDataJpaTest {
}
