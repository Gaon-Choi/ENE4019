import java.io.*;
import java.net.*;
import java.util.*;


public class main {
    public static void main(String[] args) throws IOException{
        WebClient client = new WebClient();
        String url;
        String data = "2019009261"; // student ID
        Scanner scanner = new Scanner(System.in);
        int picnum;


        // STEP 1
        System.out.println("[ STEP 1 ]");
        System.out.print("URL: ");
        url = scanner.nextLine();
        System.out.println();

        // MISSION 2
        // 2-1: Send Get Method to the Server that you are assigned
        String s = client.getWebContentByGet(url + "/test/index.html", "UTF-8", 10000);
        System.out.println(s);

        // MISSION 2
        // 2-2: Answer How many Picture you received

        // STEP 2
        System.out.println("[ STEP 2 ]");
        System.out.println("Insert number of valid pictures: ");
        picnum = scanner.nextInt();
        String ans = client.getWebContentByPost(url + "/test/picResult.html", data + "/" + Integer.toString(picnum), "UTF-8", 10000);
        System.out.println(ans);


        // MISSION 3
        // Post your message to http://166.104.143.225:61359/test/postHandleTest
        // What message did you receive?
        System.out.println("Answer: ");
        String s1 = client.getWebContentByPost(url + "/test/postHandleTest.html", data, "UTF-8", 10000);
        System.out.println(s1);


        // MISSION 4
        // Send Get Method to the Server that you are assigned and check what you can see

        // STEP 3
        System.out.println("[ STEP 3 ]");
        System.out.print("URL: ");
        url = scanner.next();
        ImageGUI img_gui = new ImageGUI();
        img_gui.imshow(url);
    }
}
