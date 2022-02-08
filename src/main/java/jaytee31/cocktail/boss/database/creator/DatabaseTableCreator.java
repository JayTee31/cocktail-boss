package jaytee31.cocktail.boss.database.creator;

import jaytee31.cocktail.boss.database.connector.DataSource;
import jaytee31.cocktail.boss.database.connector.SQLiteJDBC;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseTableCreator {
    private static final DataSource ds = new SQLiteJDBC();

    public void createTables(final String databaseName) throws Exception {
        Connection c = ds.getConnection(databaseName);

        String sql = "create table `ingredients` (\n" +
                "  `id` integer not null primary key autoincrement,\n" +
                "  `name` varchar(255) not null UNIQUE\n" +
                ");" +
                "create table `cocktails` (\n" +
                "  `id` integer not null primary key autoincrement,\n" +
                "  `name` varchar(255) not null UNIQUE\n" +
                ");" +
                "create table `cocktail_ingredients` (\n" +
                "  `id` integer not null primary key autoincrement,\n" +
                "  `cocktail_id` integer not null REFERENCES cocktails(id) ON DELETE CASCADE,\n" +
                "  `ingredient_id` integer not null REFERENCES ingredients(id) ON DELETE RESTRICT,\n" +
                "  `amount` DECIMAL not null,\n" +
                "  `unit` varchar(255) not null\n" +
                ");" +
                "create table `collections` (\n" +
                "  `id` integer not null primary key autoincrement,\n" +
                "  `name` varchar(255) not null UNIQUE\n" +
                ");" +
                "create table `cocktail_collections` (\n" +
                "  `id` integer not null primary key autoincrement,\n" +
                "  `collection_id` INTEGER not null REFERENCES collections(id) ON DELETE CASCADE,\n" +
                "  `cocktail_id` INTEGER not null REFERENCES cocktails(id) ON DELETE CASCADE\n" +
                ");";

        Statement stm = c.createStatement();
        stm.executeQuery(sql);
        stm.close();
        c.close();
    }
}
