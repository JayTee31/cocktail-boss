package jaytee31.cocktail.boss.cocktails;

import jaytee31.cocktail.boss.database.connector.DataSource;
import jaytee31.cocktail.boss.database.connector.SQLiteJDBC;
import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Command(name = "cocktails", mixinStandardHelpOptions = true, description = "Handling calls about cocktails")
public class CocktailCommands {
    private static final DataSource ds = new SQLiteJDBC();
    private static final String nameOfDatabase = "cocktail-boss.db";

    @Command(name = "add")
    public void addCommand(@Parameters(index = "0", description = "The location of the file which contains the description of the cocktail") String location)
            throws Exception {


        // --force option is missing yet

        String json = FileUtils.readFileToString(new File(location), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(json);
        String cocktailName = obj.getString("name");
        String cocktailPreparation = obj.getString("preparation");
        JSONArray arr = obj.getJSONArray("ingredients");

        String sql = "INSERT INTO " +
                "cocktails(name, preparation) " +
                "VALUES (?, ?)";
        Connection c = ds.getConnection(nameOfDatabase);
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, cocktailName.trim().toLowerCase());
        p.setString(2, cocktailPreparation);
        p.executeUpdate();

        sql = "SELECT id " +
                "FROM cocktails " +
                "WHERE name = ?";
        p = c.prepareStatement(sql);
        p.setString(1, cocktailName.trim().toLowerCase());
        ResultSet rs = p.executeQuery();
        int cocktailId = rs.getInt("id");

        for (int i = 0; i < arr.length(); i++) {
            String ingredientName = arr.getJSONObject(i).getString("name");
            sql = "SELECT id " +
                    "FROM ingredients " +
                    "WHERE name = ?";
            p = c.prepareStatement(sql);
            p.setString(1, ingredientName.trim().toLowerCase());
            rs = p.executeQuery();
            int ingredientId = rs.getInt("id");

            sql = "INSERT INTO " +
                    "cocktail_ingredients(cocktail_id, ingredient_id, amount, unit) " +
                    "VALUES (?, ?, ?, ?)";
            p = c.prepareStatement(sql);
            p.setString(1, String.valueOf(cocktailId));
            p.setString(2, String.valueOf(ingredientId));
            p.setString(3, arr.getJSONObject(i).getString("amount"));
            p.setString(4, arr.getJSONObject(i).getString("unit"));
            p.executeUpdate();
        }

        p.close();
        c.close();
        rs.close();
    }

    @Command(name = "remove")
    public void removeCommand(@Parameters(index = "0", description = "Name of the cocktail to remove.") String cocktail) throws Exception {
        String sql = "DELETE FROM cocktails " +
                "WHERE name = ?";
        Connection c = ds.getConnection(nameOfDatabase);
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, cocktail.trim().toLowerCase());
        p.executeUpdate();
        p.close();
        c.close();
    }
}
