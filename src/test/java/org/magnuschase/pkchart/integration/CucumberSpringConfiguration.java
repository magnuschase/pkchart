// src/test/java/org/magnuschase/pkchart/integration/CucumberSpringConfiguration.java
package org.magnuschase.pkchart.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = {
      "spring.datasource.url=jdbc:postgresql://localhost:5432/collectiondb_tests",
      "spring.datasource.username=postgres",
      "spring.datasource.password=postgres",
      "jwt.secret=QXg0X2pilei9lSXjGp9pwHB5qeXJSjbPldYtFxneQFA="
    })
public class CucumberSpringConfiguration {

  @Value("${local.server.port}")
  private int port;

  @PostConstruct
  public void setupRestAssured() {
    RestAssured.port = port;
  }
}
