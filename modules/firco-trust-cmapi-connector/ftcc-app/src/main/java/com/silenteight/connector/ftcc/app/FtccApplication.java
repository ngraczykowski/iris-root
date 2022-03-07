package com.silenteight.connector.ftcc.app;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import static java.lang.System.setProperty;

@EntityScan
@Slf4j
@SpringBootApplication
public class FtccApplication {

  public static void main(String[] args) {
    SpringApplication.run(FtccApplication.class, args);
  }

  private static void setUpSystemProperties() {
    // NOTE(ahaczewski): Force use of fast random source.
    setProperty("java.security.egd", "file:/dev/urandom");
  }
}
