package org.magnuschase.pkchart.integration;

import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class CucumberHooks {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Before
  public void wipeDatabase() {
    jdbcTemplate.execute(
        "DO $$ DECLARE r RECORD; BEGIN "
            + "FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP "
            + "EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE;'; "
            + "END LOOP; END $$;");
  }
}
