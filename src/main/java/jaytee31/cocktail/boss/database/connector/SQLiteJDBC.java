package jaytee31.cocktail.boss.database.connector;

import jaytee31.cocktail.boss.database.creator.DatabaseTableCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class SQLiteJDBC implements DataSource {
    @Override
    public Connection getConnection(String databaseName) {
        Connection c = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

            if (c != null) {
                rs = c.getMetaData().getCatalogs();

                while(rs.next()){
                    String catalogs = rs.getString(1);

                    if (!databaseName.equals(catalogs)){
                        new DatabaseTableCreator().createTables(databaseName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }

        return c;
    }
}
