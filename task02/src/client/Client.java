package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Client implements AutoCloseable {

  InputStreamReader isr;
  BufferedReader br;
  OutputStreamWriter osw;
  BufferedWriter bw;
  Socket sock;

  private List<Product> products = new ArrayList<>();
  private List<Product> selected = new ArrayList<>();

  private String requestId;
  private Integer budget; // in cents
  private Integer spent;
  private Integer remaining;

  public Client(String host, Integer port) throws Exception {
    sock = new Socket(host, port);
    isr = new InputStreamReader(sock.getInputStream());
    br = new BufferedReader(isr);
    osw = new OutputStreamWriter(sock.getOutputStream());
    bw = new BufferedWriter(osw);
    this.budget = 0;
  }

  public void readProducts() throws IOException {
    // logic variables
    String line;
    String directive = "";
    String value = "";
    Integer itemCount = 0;
    Integer counter = 0;
    Boolean breakLoop = false;

    // local product variables
    Integer prod_id = 0;
    String title = "";
    Integer price = 0;
    Float rating = (float) 0;

    while (null != (line = br.readLine())) {
      if (line.contains(":")) {
        String[] splitLine = line.trim().split(":");
        directive = splitLine[0];
        value = splitLine[1].trim();
      } else {
        directive = line;
        value = "";
      }
      switch (directive) {
        case "request_id":
          this.requestId = value;
          break;
        case "item_count":
          itemCount = Integer.parseInt(value);
          break;
        case "budget":
          value = value.replace(".", "");
          this.budget = Integer.parseInt(value);
          break;
        case "prod_start":
          prod_id = 0;
          title = "";
          price = 0;
          rating = (float) 0;
          break;
        case "prod_end":
          Product newProduct = new Product(prod_id, title, price, rating);
          this.products.add(newProduct);
          // increase counter and check if hit item counts
          counter += 1;
          if (counter >= itemCount) {
            breakLoop = true;
          }
          break;
        case "prod_id":
          prod_id = Integer.parseInt(value);
          break;
        case "title":
          title = value;
          break;
        case "price":
          value = value.replace(".", "");
          price = Integer.parseInt(value);
          break;
        case "rating":
          rating = Float.parseFloat(value);
          break;
        default:
          break;
      }

      if (breakLoop == true) {
        break;
      }
    }
  }

  public void selectProducts() {
    Comparator<Product> comparator = Comparator
      .comparing(Product::getRating)
      .thenComparingInt(Product::getPrice)
      .reversed();
    Collections.sort(products, comparator);

    Product prev = null;
    Integer tempBudget = this.budget;

    for (Product product : products) {
      if (product.getPrice() > tempBudget) {
        continue;
      }
      if (prev != null) {
        if (product.getRating() == prev.getRating()) {
          if (product.getPrice() > prev.getPrice()) {
            selected.removeLast();
            tempBudget += prev.getPrice();
            selected.add(product);
            tempBudget -= product.getPrice();
            continue;
          }
        }
      }

      selected.add(product);
      tempBudget -= product.getPrice();
      prev = product;
    }

    this.remaining = tempBudget;
    this.spent = this.budget - tempBudget;
  }

  public String sendMessage(String name, String email) throws IOException {
    // build item string
    StringBuilder stringBuilder = new StringBuilder();
    for (Product product : selected) {
      stringBuilder.append(product.getId());
      stringBuilder.append(",");
    }
    String items = stringBuilder.toString();

    String spentDollars = convertToDollar(this.spent);
    String remainingDollars = convertToDollar(this.remaining);

    bw.write("request_id: " + this.requestId + "\n");
    bw.flush();
    bw.write("name: " + name + "\n");
    bw.flush();
    bw.write("email: " + email + "\n");
    bw.flush();
    bw.write("items: " + items + "\n");
    bw.flush();
    bw.write("spent: " + spentDollars + "\n");
    bw.flush();
    bw.write("remaining: " + remainingDollars + "\n");
    bw.flush();
    bw.write("client_end" + "\n");
    bw.flush();

    String line = br.readLine();
    return line;
  }

  private String convertToDollar(Integer cents) {
    String dollars = String.valueOf(cents);
    if (dollars.length() >= 3) {
      dollars =
        new StringBuilder(dollars).insert(dollars.length() - 2, ".").toString();
    } else {
      dollars = new StringBuilder(dollars).insert(0, "0.").toString();
    }
    return dollars;
  }

  @Override
  public void close() throws Exception {
    bw.flush();
    osw.flush();
    bw.close();
    osw.close();
    br.close();
    isr.close();
    sock.close();
  }
}
