package com.silenteight.payments.bridge.testing;

import com.silenteight.sep.base.common.batch.DefaultBatchConfigurerConfiguration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBatchTest
@Import({DefaultBatchConfigurerConfiguration.class})
@EnableBatchProcessing
public class BaseBatchTest extends PbBaseDataJpaTest {

  @Autowired
  protected JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  protected JdbcTemplate jdbcTemplate;
}
