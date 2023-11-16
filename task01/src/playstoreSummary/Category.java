package playstoreSummary;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Category {

  private String category;
  private Float averageRating;
  private String highestRatedApp;
  private Float highestRatedAppRating;
  private String lowestRatedApp;
  private Float lowestRatedAppRating;
  private Integer totalApps;
  private Integer discarded;
  private Map<String, Float> mapOfApps;

  public Category(String category) {
    this.category = category;
    this.discarded = 0;
    this.mapOfApps = new HashMap<>();
  }

  public String getCategory() {
    return category;
  }

  public Float getAverageRating() {
    return averageRating;
  }

  public String getHighestRatedApp() {
    return highestRatedApp;
  }

  public Float getHighestRatedAppRating() {
    return highestRatedAppRating;
  }

  public String getLowestRatedApp() {
    return lowestRatedApp;
  }

  public Float getLowestRatedAppRating() {
    return lowestRatedAppRating;
  }

  public Integer getTotalApps() {
    return totalApps;
  }

  public Integer getDiscarded() {
    return discarded;
  }

  public Map<String, Float> getMapOfApps() {
    return this.mapOfApps;
  }

  public void addDiscarded() {
    this.discarded += 1;
  }

  public void addApp(String app, Float rating) {
    this.mapOfApps.put(app, rating);
  }

  public void summarise() {
    Float avg = (float) 0;
    String nameOfHighest = "";
    Float ratingOfHighest = (float) 0;
    String nameOfLowest = "";
    Float ratingOfLowest = (float) 10;
    this.totalApps = mapOfApps.size();

    for (Entry<String, Float> app : mapOfApps.entrySet()) {
      String curName = app.getKey();
      Float curRating = app.getValue();

      avg += curRating;

      if (curRating > ratingOfHighest) {
        ratingOfHighest = curRating;
        nameOfHighest = curName;
      }

      if (curRating < ratingOfLowest) {
        ratingOfLowest = curRating;
        nameOfLowest = curName;
      }
    }

    this.averageRating = avg /= totalApps;
    this.highestRatedApp = nameOfHighest;
    this.highestRatedAppRating = ratingOfHighest;
    this.lowestRatedApp = nameOfLowest;
    this.lowestRatedAppRating = ratingOfLowest;
  }
  // private Float getAverage() {
  //   Float avg = (float) 0;
  //   for (Entry<String, Float> app : mapOfApps.entrySet()) {
  //     avg += app.getValue();
  //   }
  //   avg /= mapOfApps.size();
  //   System.out.println(avg);
  //   return avg;
  // }
}
