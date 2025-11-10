import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 9999; 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server da khoi dong tai cong " + port);
            System.out.println("Dang cho 2 nguoi choi ket noi...");

            while (true) {
                Socket player1 = serverSocket.accept();
                System.out.println("Nguoi choi 1 da ket noi: " + player1.getInetAddress());
                PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
                out1.println("CHO");

                Socket player2 = serverSocket.accept();
                System.out.println("Nguoi choi 2 da ket noi: " + player2.getInetAddress());
                System.out.println("Da du 2 nguoi choi. Bat dau GameHandler...");
                GameHandler gameHandler = new GameHandler(player1, player2);
                gameHandler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class GameHandler extends Thread {
    private Socket player1;
    private Socket player2;

    public GameHandler(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);

            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);

            out1.println("START");
            out2.println("START");

            String choice1 = in1.readLine();
            System.out.println("Nguoi choi 1 chon: " + choice1);

            String choice2 = in2.readLine();
            System.out.println("Nguoi choi 2 chon: " + choice2);

            String result1 = "";
            String result2 = "";

            if (choice1.equals(choice2)) {
                result1 = "HOA";
                result2 = "HOA";
            } else if ((choice1.equals("KEO") && choice2.equals("BAO")) ||
                       (choice1.equals("BUA") && choice2.equals("KEO")) ||
                       (choice1.equals("BAO") && choice2.equals("BUA"))) {
                result1 = "THANG";
                result2 = "THUA";
            } else {
                result1 = "THUA";
                result2 = "THANG";
            }
            System.out.println("-> Ket qua: P1 " + result1 + ", P2 " + result2);
            out1.println("KET QUA: BAN " + result1);
            out2.println("KET QUA: BAN " + result2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                player1.close();
                player2.close();
                System.out.println("Da dong ket noi 1 game session.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
