package client;

public class ClientProgram {

  public static void main(String[] args) throws Exception {
    String HOST_NAME = "localhost";
    Integer PORT = 3000;

    Integer arguments = args.length;

    switch (arguments) {
      case 1:
        PORT = Integer.parseInt(args[0]);
        break;
      case 2:
        PORT = Integer.parseInt(args[0]);
        HOST_NAME = args[1];
        break;
      default:
        break;
    }

    try (Client client = new Client(HOST_NAME, PORT)) {
      client.readProducts();
      client.selectProducts();
      String response = client.sendMessage(
        "S9801044J",
        "anthonypoh1998@gmail.com"
      );
      System.out.println(response);
    }
  }
}
