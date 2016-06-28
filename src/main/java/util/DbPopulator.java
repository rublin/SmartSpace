package util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Created by Sheremet on 28.06.2016.
 */
public class DbPopulator extends ResourceDatabasePopulator{
    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    @Autowired
    private DataSource dataSource;

    public DbPopulator(String scriptLocation) {
        super(RESOURCE_LOADER.getResource(scriptLocation));
    }

    public void execute() {
        DatabasePopulatorUtils.execute(this, dataSource);
    }
}
