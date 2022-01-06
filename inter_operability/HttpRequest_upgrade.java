import java.io.* ;
import java.net.* ;
import java.util.* ;

enum StatusCode{
	OK, BAD_REQUEST, FORBIDDEN, NOT_FOUND, HTTP_VERSION_NOT_SUPPORTED, INTERNAL_SERVER_ERROR
}

final class HttpRequest_upgrade implements Runnable {
    final static String CRLF = "\r\n";
    final static String HTTP_VERSION = "1.1";
    final static String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    
    // final static string content Length "";
    final static int BUFFER_IN_SIZE = 2048; // 2048
    final static int BUFFER_OUT_SIZE = 2048; // 2048
    final static Properties CONTENT_TYPES = new Properties();
    final static EnumMap<StatusCode, String> SCODES = new EnumMap<StatusCode, String> (StatusCode.class);
    
    static {
    	CONTENT_TYPES.setProperty("html", "text/html");
		CONTENT_TYPES.setProperty("jpg", "image/jpg");
    	
    	SCODES.put(StatusCode.OK, "200");
    	SCODES.put(StatusCode.BAD_REQUEST, "400");
    	SCODES.put(StatusCode.FORBIDDEN, "403");
    	SCODES.put(StatusCode.NOT_FOUND, "404");
    	
    	SCODES.put(StatusCode.HTTP_VERSION_NOT_SUPPORTED, "505");
    }
    
    StatusCode code;    
    Socket socket;
    File requestedFile;
    
    // Constructor
    public HttpRequest_upgrade(Socket socket) throws Exception {
		this.socket = socket;
		this.code = null;
		this.requestedFile = null;
    }
    
    // Implement the run() method of the Runnable interface.
    public void run() {
		try {
		    processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
    }

    private void processRequest() throws Exception {		
		
		// Get a reference to the socket's input and output streams.
		InputStream is = null;
		DataOutputStream os = null;
        FileInputStream fis = null;

		// Set up input stream filters.
		BufferedReader br = null;
		
		
		try {
			is = socket.getInputStream();
			os = new DataOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(is), BUFFER_IN_SIZE);

			// Get the request line of the HTTP request message.
	        String requestLine = br.readLine();
	        String errorMsg = parseRequestLine(requestLine);
	        
	        String headerLine = null;
	        while ((headerLine = br.readLine()).length() != 0) {
			    System.out.println(headerLine);
			}
	        
	        if(errorMsg == null) {
	        	try {
	        		fis = new FileInputStream(requestedFile);
	        	} catch(FileNotFoundException e){
	        		System.out.println("FileNotFoundException while opening file inputStream");
	        		e.printStackTrace();
	        		code = StatusCode.NOT_FOUND;
	        	}
	        } else {
	        	System.out.println();
	        	System.out.println(errorMsg);
	        }
	        sendResponseMessage(fis, os);
			
		} finally {
			if(os != null)
				os.close();
			if(br != null)
				br.close();
			if(fis != null)
				fis.close();
			socket.close();
		}
    }
    
    private void sendResponseMessage(FileInputStream fis, DataOutputStream os) throws Exception {
		// TODO Auto-generated method stub
    	
    	String statusLine = "HTTP/" + HTTP_VERSION + " " + SCODES.get(code) + " ";
    	String entityBody = "<HTML>" + CRLF + " <HEAD><TITLE>?</TITLE></HEAD>" + CRLF + " <BODY>?</BODY>"+ CRLF;
    	
    	String message;
    	switch(code) {
    	
    	case OK:
    		message = "OK";
    		break;
    	case BAD_REQUEST:
    		message = "BAD REQUEST";
    		break;
    	case FORBIDDEN:
    		message = "FORBIDDEN";
    		break;
    	case NOT_FOUND:
    		message = "NOT_FOUND";
    		break;
    	case HTTP_VERSION_NOT_SUPPORTED:
    		message = "HTTP_VERSION_NOT_SUPPORTED";
    		break;
    		
    	case INTERNAL_SERVER_ERROR:
    		message = "INTERNAL_SERVER_ERROR";
    		break;
    	default:
    		message = "empty";    		
    	}
    	
    	statusLine = statusLine + message;
    	if(code != StatusCode.OK) {
			entityBody = entityBody.replaceAll("\\?", message + " - sent by Gaon's Webserver");
		}

    	System.out.println("StatusLine : " + statusLine);
    	System.out.println("entityBody : " + CRLF + entityBody);

    	// send the status line.
    	os.writeBytes(statusLine + CRLF);

    	//Construct and the header lines
    	sendHeaderLines(os);
    	os.writeBytes(CRLF);
    	
    	if(code == StatusCode.OK) {
    		System.out.println("sending request file to Client...");
    		sendBytes(fis, os);
    	}else {
    		System.out.println("sending error message to Client...");
    		os.writeBytes(entityBody);
    	}
	}
    
    private void sendHeaderLines(DataOutputStream os) throws Exception{
    	StringBuffer headerLines = new StringBuffer();
    	
    	String contentTypeLine = "Content-Type: ";
    	String contentLength = "Content-Length: ";
    	System.out.println("code " + code);
    	
    	switch (code) {
    	case OK:
    		contentTypeLine += this.contentType(this.requestedFile.getName()) + CRLF;		// Mission 8
    		contentTypeLine += contentLength + "1024" + CRLF;	// Mission 6
    		break;

    	default:
    		contentTypeLine += this.contentType("text.html") + CRLF;	// Mission 7
    	}
    	headerLines.append(contentTypeLine);
    	os.writeBytes(headerLines.toString());
    }

	private String parseRequestLine(java.lang.String requestLine) {
		// TODO Auto-generated method stub
    	System.out.println();
		// signature
		System.out.println("GAON CHOI, 2019009261 -- SERVER");
    	System.out.println("Received HTTP request: ");
    	System.out.println(requestLine);
    	StringTokenizer tokens = new StringTokenizer(requestLine);
    	if(tokens.countTokens() != 3) {
			// the input doesn't match expectations(format)
    		code = StatusCode.NOT_FOUND;	// Mission 4
    		return "Request line malformed. Returning BAD NOT FOUND.";
    	}
    	
    	String method = tokens.nextToken().toUpperCase();
    	String fileName = tokens.nextToken();    	
    	
        System.out.println("file name: " + fileName);

        // Modify to your path
		// fileName = "." + fileName ;
        fileName = "." + fileName;
        File file = new File(fileName);
        
        if(!file.exists()) {
        	code = StatusCode.NOT_FOUND;
        	return "File not FOUND";        	
        }
        if(!file.canRead()) {
        	code = StatusCode.FORBIDDEN;
        	return "File is not Readdable";    
        }
        
        if(file.isDirectory()) {
        	File[] list = file.listFiles(new FilenameFilter() {
        		public boolean accept(File dir, String f) {
        			if(f.equalsIgnoreCase("index.html"))
        				return true;
        			return false;
        		}
        	});
        	if(list == null || list.length == 0) {
        		code = StatusCode.NOT_FOUND;
        		return "File not FOUND";
        	}
        	else if(list.length != 1){
        		code = StatusCode.INTERNAL_SERVER_ERROR;
            	return "found more than one file";	
        	}
        	file = list[0];
        }
        
        requestedFile = file;
        String version = tokens.nextToken().toUpperCase();
        if(version.equals("HTTP/1.0")) {
        	code = StatusCode.BAD_REQUEST;	// Mission 5
        	return "HTTP Version String is malformed.";	
        }
        if(!version.matches("HTTP/([1-9][0-9.]*)")) {
        	code = StatusCode.BAD_REQUEST;
        	return "HTTP Version String is malformed.";
        }
        if(!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1") ) {
        	code = StatusCode.HTTP_VERSION_NOT_SUPPORTED;
        	return "HTTP Version is not supported.";
        }
        
        code = StatusCode.OK;	// Mission 3
    	return null;
	}
	
    

    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
		// Construct a 1K buffer to hold bytes on their way to the socket.
		byte[] buffer = new byte[BUFFER_OUT_SIZE];
		int bytes = 0;
		
		// Copy requested file into the socket's output stream.
		while ((bytes = fis.read(buffer)) != -1) {
		    os.write(buffer, 0, bytes);
		}
    }

    private static String contentType(String fileName) {
		String fname = fileName.toLowerCase();
		int lastdot = fname.lastIndexOf(".");
		if( (lastdot != -1) && (lastdot != fname.length() -1) ) {
			System.out.println("type : " + CONTENT_TYPES.getProperty(fname.substring(lastdot + 1) ));
			return CONTENT_TYPES.getProperty(fname.substring(lastdot + 1), DEFAULT_CONTENT_TYPE);
		}
		
		return DEFAULT_CONTENT_TYPE;
    }
}