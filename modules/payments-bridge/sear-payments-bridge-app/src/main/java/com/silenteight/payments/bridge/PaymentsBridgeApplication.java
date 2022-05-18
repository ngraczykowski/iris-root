package com.silenteight.payments.bridge;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.scheduler.Schedulers;

import java.security.Security;

import static com.silenteight.payments.bridge.common.app.EnvironmentUtils.setPropertyFromEnvironment;
import static java.lang.System.setProperty;

@EnableAsync(mode = AdviceMode.ASPECTJ)
@EnableJpaRepositories
@EntityScan
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableScheduling
@EnableRetry
@EnableSchedulerLock(defaultLockAtMostFor = "5s")
@Slf4j
@SpringBootApplication
public class PaymentsBridgeApplication {

  public static void main(String[] args) {
    Security.addProvider(new BouncyCastleProvider());
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    Schedulers.enableMetrics();
    new SpringApplicationTemplate(args, PaymentsBridgeApplication.class)
        .contextCallback(new DefaultSpringApplicationContextCallback())
        .runAndExit(new Configurer());
  }

  private static void setUpSystemProperties() {
    // NOTE(ahaczewski): Force use of fast random source.
    setProperty("java.security.egd", "file:/dev/urandom");
  }

  private static void setUpSecuritySystemProperties() {
    setPropertyFromEnvironment("javax.net.ssl.trustStore", "TRUSTSTORE_PATH");
    setPropertyFromEnvironment("javax.net.ssl.trustStorePassword", "TRUSTSTORE_PASSWORD");
    setPropertyFromEnvironment("javax.net.ssl.trustStoreType", "TRUSTSTORE_TYPE");
    setPropertyFromEnvironment("javax.net.ssl.keyStore", "KEYSTORE_PATH");
    setPropertyFromEnvironment("javax.net.ssl.keyStorePassword", "KEYSTORE_PASSWORD");
    setPropertyFromEnvironment("javax.net.ssl.keyStoreType", "KEYSTORE_TYPE");
  }

  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder
          .bootstrapProperties("spring.application.name=pb");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF)
          .web(WebApplicationType.SERVLET);
    }
  }
}
