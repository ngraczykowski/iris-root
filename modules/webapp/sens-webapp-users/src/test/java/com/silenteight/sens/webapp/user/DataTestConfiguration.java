package com.silenteight.sens.webapp.user;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfigurationPackage
@SpringBootConfiguration
@EnableJpaRepositories("com.silenteight.sens.webapp.adapter.user")
@EntityScan("com.silenteight.sens.webapp.domain.user")
public class DataTestConfiguration {

}
