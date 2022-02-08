package jaytee31.cocktail.boss.database.connector;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection(String location);
}
