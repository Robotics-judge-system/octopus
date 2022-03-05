package ru.anarcom.octopus.configuration;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.anarcom.octopus.JsonbDataFactory;

@Configuration
public class DbUnitConfiguration {

  @Autowired
  private DataSource dataSource;

  @Bean
  public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
    DatabaseConfigBean bean = new DatabaseConfigBean();
    bean.setDatatypeFactory(new JsonbDataFactory());

    DatabaseDataSourceConnectionFactoryBean dbConnFact =
        new DatabaseDataSourceConnectionFactoryBean(dataSource);
    dbConnFact.setDatabaseConfig(bean);
    return dbConnFact;
  }
}
