package jaytee31.cocktail.boss.ingredients;

import jaytee31.cocktail.boss.stringformatter.StringFormatter;
import jaytee31.cocktail.boss.database.connector.DataSource;
import jaytee31.cocktail.boss.database.connector.SQLiteJDBC;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@Command(name = "ingredients", mixinStandardHelpOptions = true, description = "Handling calls about ingredients")
public class IngredientCommands {
    private static final DataSource ds = new SQLiteJDBC();
    private static final StringFormatter stringFormatter = new StringFormatter();
    private static final String nameOfDatabase = "cocktail-boss.db";

    @Command(name = "add")
    public void addCommand(@Parameters(index = "0", description = "The name of the ingredient to add.") String ingredient)
        throws Exception {
        String sql = "INSERT INTO " +
                "ingredients(name) " +
                "VALUES (?)";
        Connection c = ds.getConnection(nameOfDatabase);
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, ingredient.trim().toLowerCase());
        p.executeUpdate();
        p.close();
        c.close();
    }

    @Command(name = "list")
    public void listCommand() throws Exception {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name " +
                "FROM ingredients " +
                "ORDER BY name";
        Connection c = ds.getConnection(nameOfDatabase);
        ResultSet rs = c.createStatement().executeQuery(sql);

        while (rs.next()) {
            list.add(rs.getString("name"));
        }

        System.out.println(stringFormatter.formatIngredientList(list));

        c.close();
        rs.close();
    }

    @Command(name = "remove")
    public void removeCommand(@Parameters(index = "0", description = "The name of the ingredients to remove.") String ingredient)
            throws Exception {
        String sql = "DELETE FROM ingredients " +
                "WHERE name = ?";
        Connection c = ds.getConnection(nameOfDatabase);
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, ingredient.trim().toLowerCase());
        p.executeUpdate();
        p.close();
        c.close();
    }

    @Command(name = "rename")
    public void renameCommand(@Parameters(index = "0", description = "Old name of the ingredient.") String oldName,
                              @Parameters(index = "1", description = "new name of the ingredient.") String newName)
            throws Exception {
        if (!checkIfOldNameExist(oldName)) {
            throw new IllegalArgumentException();
        }

        String sql = "UPDATE ingredients " +
                "SET name = ? " +
                "WHERE name = ?";
        Connection c = ds.getConnection(nameOfDatabase);
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, newName.trim().toLowerCase());
        p.setString(2, oldName.trim().toLowerCase());
        p.executeUpdate();
        p.close();
        c.close();
    }

    private boolean checkIfOldNameExist(final String oldName) throws Exception {
        boolean check = false;
        String sql = "SELECT name " +
                "FROM ingredients " +
                "WHERE name = ?";
        Connection c = ds.getConnection(nameOfDatabase);
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, oldName.trim().toLowerCase());
        ResultSet rs = p.executeQuery();

        if (rs.next()) {
            check = true;
        }

        p.close();
        c.close();
        rs.close();

        return check;
    }
}
