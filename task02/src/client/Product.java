package client;

public class Product {

  private Integer id;
  private String title;
  private Integer price;
  private Float rating;

  public Product(Integer id, String title, Integer price, Float rating) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.rating = rating;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getPrice() {
    return this.price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Float getRating() {
    return this.rating;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }
}
