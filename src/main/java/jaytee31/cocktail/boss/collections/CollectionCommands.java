package jaytee31.cocktail.boss.collections;

import picocli.CommandLine.Command;

@Command(name = "collections", mixinStandardHelpOptions = true, description = "Handling calls about collections")
public class CollectionCommands implements Runnable {
    @Override
    public void run() {
        System.out.println("ccollections");
    }
}
