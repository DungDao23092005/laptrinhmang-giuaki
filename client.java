import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 9999;

        try (
                Socket socket = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner consoleInput = new Scanner(System.in)
        ) {

            System.out.println("Da ket noi toi server.");
            String serverResponse = in.readLine();

            if (serverResponse.equals("CHO")) {
                System.out.println("Ban la nguoi choi 1. Dang cho nguoi choi 2...");
                serverResponse = in.readLine();
            }

            if (serverResponse.equals("START")) {
                System.out.println("--------------------");
                System.out.println("Game bat dau! Moi ban chon (KEO, BUA, BAO):");

                String userChoice = consoleInput.nextLine().toUpperCase().trim();

                out.println(userChoice);
                System.out.println("Ban da chon: " + userChoice + ". Dang cho ket qua...");

                String result = in.readLine();

                System.out.println("=====================");
                System.out.println(result);
                System.out.println("=====================");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Game ket thuc (choi 1 van). Da ngat ket noi.");
    }
}