package jaytee31.cocktail.boss.stringformatter;

import java.util.List;

public class StringFormatter {

    public String formatIngredientList(final List<String> list) throws Exception {
        StringBuilder str = new StringBuilder();

        for (String s : list) {
            str.append(s);
            str.append("\n");
        }

        return str.toString();
    }
}
