import java.io.*;
import java.net.*;

public class WebClient {

    public String getWebContentByGet(String urlString, final String charset, int timeout) throws IOException {
        if(urlString == null || urlString.length() == 0) {
            return null;
        }

        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString
                : ("http://" + urlString).intern();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // MISSION 1
        // Set User-Agent in Request header : Student Number/Name/Program Name/Subject
        conn.setRequestProperty("User-Agent", "2019009261/GAONCHOI/WebClient/computer_network");
        conn.setRequestProperty("Accept", "text/html");
        conn.setConnectTimeout(timeout);
        try {
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        String line = null;
        StringBuffer sb = new StringBuffer();

        while((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        if(reader != null) {
            reader.close();
        }
        if(conn != null) {
            conn.disconnect();
        }
        return sb.toString();
    }

    public String getWebContentByPost(String urlString, String data, final String charset, int timeout) throws IOException {
        if (urlString == null || urlString.length() == 0) {
            return null;
        }

        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString
                : ("http://" + urlString).intern();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");

        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");

        // MISSION 1
        // Set User-Agent in Request header : Student Number/Name/Program Name/Subject
        connection.setRequestProperty("User-Agent", "2019009261/GAONCHOI/WebClient/computer_network");

        connection.setRequestProperty("Accept", "text/xml");
        connection.setConnectTimeout(timeout);
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        byte[] content = data.getBytes("UTF-8");

        out.write(content);
        out.flush();
        out.close();

        try {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        InputStream input = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        String line = null;
        StringBuffer sb = new StringBuffer();

        while((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        if(reader != null) {
            reader.close();
        }
        if(connection != null) {
            connection.disconnect();
        }
        return sb.toString();
    }
}
