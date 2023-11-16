package playstoreSummary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

  public static void main(String args[]) throws Exception {
    if (args.length < 0) {
      System.out.println("ERROR: Please provide a googleplaystore.csv");
      System.exit(1);
    }

    Map<String, Category> categories = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String line;
      Integer linesRead = 0;
      br.readLine(); // skip column headers
      while (null != (line = br.readLine())) {
        linesRead += 1;
        String[] rows = line.trim().split(",");
        String app = rows[0];
        String category = rows[1].toLowerCase();
        Float rating = Float.parseFloat(rows[2]);

        // Create new category and place in local map
        if (null == categories.get(category)) {
          Category newCat = new Category(category);
          categories.put(category, newCat);
        }

        Category cur = categories.get(category);

        if (rating.isNaN()) {
          cur.addDiscarded();
          // System.out.printf("App: %s rating is not a number.\n", app);
          continue;
        }

        cur.addApp(app, rating);
        // System.out.printf("%s, %s, %s\n", app, category, rating);
      }

      for (Entry<String, Category> cat : categories.entrySet()) {
        Category curCat = cat.getValue();
        cat.getValue().summarise();

        System.out.printf("\nCategory: %s\n", curCat.getCategory().toUpperCase());

        if (curCat.getMapOfApps().size() <= 0) {
          System.out.printf("\tDiscarded: %d\n", curCat.getDiscarded());
          continue;
        }

        System.out.printf("\tHighest: %s, (%.1f)\n",
          curCat.getHighestRatedApp(),
          curCat.getHighestRatedAppRating()
        );
        System.out.printf("\tLowest: %s, (%.1f)\n",
          curCat.getLowestRatedApp(),
          curCat.getLowestRatedAppRating()
        );
        System.out.printf("\tAverage: %.1f\n", curCat.getAverageRating());
        System.out.printf("\tCount: %d\n", curCat.getTotalApps());
        System.out.printf("\tDiscarded: %d\n", curCat.getDiscarded());
      }

      System.out.printf("\nTotal lines in file: %d\n", linesRead);
    }
  }
}
