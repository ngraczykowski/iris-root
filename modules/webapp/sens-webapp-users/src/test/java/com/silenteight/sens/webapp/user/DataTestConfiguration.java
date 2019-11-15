package com.silenteight.sens.webapp.user;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

@AutoConfigurationPackage
@SpringBootConfiguration
@Import(UsersModuleConfiguration.class)
public class DataTestConfiguration {

}
