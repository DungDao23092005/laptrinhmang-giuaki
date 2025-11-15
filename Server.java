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
        try (
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true)
        ) {
            
            out1.println("CHO");
            
            while (true) {
                
                System.out.println("Gui tin hieu START cho vong moi...");
                out1.println("START");
                out2.println("START");

                String choice1 = in1.readLine();
                String choice2 = in2.readLine();
                
                if (choice1 == null) {
                    System.out.println("Nguoi choi 1 da thoat.");
                    try { out2.println("QUIT_DOI_THU"); } catch (Exception e) {}
                    break; 
                }

                if (choice2 == null) {
                    System.out.println("Nguoi choi 2 da thoat.");
                    try { out1.println("QUIT_DOI_THU"); } catch (Exception e) {}
                    break; 
                }
                
                System.out.println("P1 chon: " + choice1 + ", P2 chon: " + choice2);

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

                out1.println(result1 + ":" + choice1 + ":" + choice2);
                out2.println(result2 + ":" + choice2 + ":" + choice1);

                out1.println("CHOI_TIEP?");
                out2.println("CHOI_TIEP?");
                
                String playAgain1 = in1.readLine();
                String playAgain2 = in2.readLine();

                if (playAgain1 == null || playAgain2 == null) {
                     System.out.println("Mot nguoi choi da thoat khi dang hoi choi lai.");
                     try { out1.println("QUIT_DOI_THU"); } catch (Exception e) {}
                     try { out2.println("QUIT_DOI_THU"); } catch (Exception e) {}
                     break;
                }

                if (!playAgain1.equals("Y") || !playAgain2.equals("Y")) {
                    System.out.println("Game ket thuc do nguoi choi chon thoat.");
                    out1.println("QUIT_GAME"); 
                    out2.println("QUIT_GAME");
                    break; 
                }
                
                System.out.println("Ca hai deu dong y choi tiep.");
            }

        } catch (Exception e) {
            System.out.println("Loi trong GameHandler: " + e.getMessage());
        } finally {
            try {
                player1.close();
                player2.close();
                System.out.println("Da dong ket noi cho 1 game session.");
            } catch (Exception e) {
                 // e.printStackTrace();
            }
        }
    }
}