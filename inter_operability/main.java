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

        
		// signature
		System.out.println("GAON CHOI, 2019009261 -- CLIENT");
		
        // STEP 1
        System.out.println("INSERT URL !!!");
        System.out.print("URL: ");
        url = scanner.nextLine();
        System.out.println();

        // MISSION 2
        // 2-1: Send Get Method to the Server that you are assigned(text)
        
        // signature
     	System.out.println("GAON CHOI, 2019009261 -- CLIENT");
     		
        System.out.println("-----* GET TEXT *-----");
        String s = client.getWebContentByGet(url+"/studentID.html", "UTF-8", 10000);
        System.out.println(s);

        // signature
     	System.out.println("GAON CHOI, 2019009261 -- CLIENT");
     	
        // 2-2: Send Get Method to the Server that you are assigned(image)
        System.out.println("-----* GET IMAGE *-----");
        String s1 = client.getWebContentByGet(url+"/image.jpg", "UTF-8", 10000);
        
        try {
        	ImageGUI img_gui = new ImageGUI();
        	img_gui.imshow("http://"+url+"/image.jpg");
        }
        
        catch (Exception e) {
        	System.out.println("EXCEPTION HANDLING --> print plaintext");
        	System.out.println(s1);
        }
        System.out.println();
        
        // MISSION 3
        
        // signature
     	System.out.println("GAON CHOI, 2019009261 -- CLIENT");
     	
        System.out.println("-----* POST TEXT *-----");
        String s2 = client.getWebContentByPost(url+"/studentID.html", data, "UTF-8", 10000);
        System.out.println(s2);
    }
}
