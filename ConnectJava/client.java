import java.net.*;
import java.io.*;
import java.net.Socket;

class client 
{
	private static BufferedReader in;
	private static BufferedWriter out;
	private static BufferedReader reader;

	public static void main (String [] args)
		{
			try{
				Socket s= new Socket("127.0.0.1", 3000);
			try
			{
				reader = new BufferedReader(new InputStreamReader(System.in));
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				System.out.println("Anything to say? type it here!");
                String word = reader.readLine(); 
                out.write(word + "\n");
                out.flush();
                String serverWord = in.readLine(); 
				System.out.println(serverWord);
			} finally {
                System.out.println("Client closed!");
                s.close();
                in.close();
                out.close();
			} 
		}catch (IOException  e) {
			System.err.println(e);
		}
	}
}
