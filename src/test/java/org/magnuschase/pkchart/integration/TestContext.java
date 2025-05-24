package org.magnuschase.pkchart.integration;

import org.springframework.stereotype.Component;

import io.cucumber.spring.ScenarioScope;

@Component
@ScenarioScope
public class TestContext {
    public String adminToken;
    public String userToken;
}