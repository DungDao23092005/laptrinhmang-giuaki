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

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {

                if (serverMessage.equals("CHO")) {
                    System.out.println("Ban la nguoi choi 1. Dang cho nguoi choi 2...");
                    continue;
                }
                if (serverMessage.equals("START")) {
                    System.out.println("--------------------");
                    System.out.println("Game bat dau! Moi ban chon (KEO, BUA, BAO):");

                    String userChoice = "";
                    while (true) {
                        userChoice = consoleInput.nextLine().toUpperCase().trim();
                        if (userChoice.equals("KEO") || userChoice.equals("BUA") || userChoice.equals("BAO")) {
                            break;
                        }
                        System.out.println("Lua chon khong hop le. Vui long chi nhap KEO, BUA, hoac BAO:");
                    }

                    out.println(userChoice);
                    System.out.println("Ban da chon: " + userChoice + ". Dang cho doi thu...");

                } else if (serverMessage.equals("CHOI_TIEP?")) {
                    System.out.println("--------------------");
                    System.out.println("Ban co muon choi tiep? (Y/N):");
                    String playAgain = consoleInput.nextLine().toUpperCase().trim();
                    out.println(playAgain);
                    if (!playAgain.equals("Y")) {
                        System.out.println("Ban da chon thoat. Cam on da choi!");
                        break;
                    }
                    System.out.println("Dang cho doi thu xac nhan...");

                } else if (serverMessage.equals("QUIT_DOI_THU")) {
                    System.out.println("!!! DOI THU DA NGAT KET NOI. GAME KET THUC. !!!");
                    break;
                } else if (serverMessage.equals("QUIT_GAME")) {
                    System.out.println("Mot trong hai nguoi da chon thoat. Game ket thuc!");
                    break;
                } else {
                    String[] parts = serverMessage.split(":");
                    if (parts.length == 3) {
                        String result = parts[0];
                        String myChoice = parts[1];
                        String opponentChoice = parts[2];

                        System.out.println("=====================");
                        System.out.println("KET QUA: BAN " + result);
                        System.out.println("Ban chon: " + myChoice + " | Doi thu chon: " + opponentChoice);
                        System.out.println("=====================");
                    } else {
                        System.out.println("[SERVER]: " + serverMessage);
                    }
                }
            }   
        } catch (Exception e) {
            System.out.println("Mat ket noi toi server: " + e.getMessage());
        }

        System.out.println("Game ket thuc. Da ngat ket noi.");
    }
}
