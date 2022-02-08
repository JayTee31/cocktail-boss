package jaytee31.cocktail.boss;

import jaytee31.cocktail.boss.cocktails.CocktailCommands;
import jaytee31.cocktail.boss.collections.CollectionCommands;
import jaytee31.cocktail.boss.ingredients.IngredientCommands;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "cocktail-boss", mixinStandardHelpOptions = true,
        version = "cocktail-boss 1.0", description = "Running the cocktail-boss application", subcommands = {
        IngredientCommands.class,
        CocktailCommands.class,
        CollectionCommands.class
    })
public class CocktailBoss {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new CocktailBoss()).execute(args);
        System.exit(exitCode);
    }
}
