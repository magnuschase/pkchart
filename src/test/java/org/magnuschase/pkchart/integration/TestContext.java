package org.magnuschase.pkchart.integration;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class TestContext {
  public String adminToken;
  public String userToken;
}
