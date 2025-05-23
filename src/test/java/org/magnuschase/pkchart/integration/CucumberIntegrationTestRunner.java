package org.magnuschase.pkchart.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources",
    glue = "org.magnuschase.pkchart.integration",
    plugin = {"pretty", "summary", "html:target/cucumber-reports.html"})
public class CucumberIntegrationTestRunner {
  // This class is used only as an entry point for Cucumber tests
}
