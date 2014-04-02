import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;


public class HttpClient {
	public static void main(String args[]) throws MalformedURLException {
		if (args.length < 1) {
			System.out.println("Not enough arguments.");
			return;
		}
		
		URL url = new URL(args[0]);
		try {
			Socket socket = new Socket(url.getHost(), 80);
			OutputStream out = socket.getOutputStream();
			String msg = "GET " + url.getPath() + " HTTP/1.1" + "\r\n" + "Host: " + url.getHost() + "\r\n\r\n";
			out.write(msg.getBytes());
			InputStream in = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				System.out.println(inputLine);
			}
			br.close();
			in.close();
			out.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
