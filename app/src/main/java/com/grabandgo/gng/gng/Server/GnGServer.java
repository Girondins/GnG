import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;





public class GnGServer implements Runnable{
	private ServerSocket serverSocket;
	private Thread server = new Thread(this);
	private ArrayList<ClientHandler> alch;
	private int counter = 0;
	private int port;


	public GnGServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.port = port;
		alch = new ArrayList<ClientHandler>();
		server.start();
	}
	
	public void run(){
		
		while (true) {
			try {
				System.out.println("Server Online on Port: " + port);
				Socket socket = serverSocket.accept();
				ClientHandler ch = new ClientHandler(socket);
				ch.setClientID(counter++);
				alch.add(ch);
				ch.start();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		
	}
	
	
	


	private class ClientHandler extends Thread{
		private Socket socket;
		private HackedInputStream his;
		private ObjectOutputStream oos;
		private RequestHandler handler = new RequestHandler();
		private int id;
		
		public ClientHandler(Socket socket){
			this.socket = socket;
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				his = new HackedInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void setClientID(int id){
			this.id = id;
			System.out.println("Client " + id + " Connected");
		}
		
		public int getClientID(){
			return this.id;
		}

	
	public void run(){
		Object object;
		try{
		while(true){
			try {
				object = his.readObject();
				if(object instanceof String){
					String request = (String)object;
					handler.handleRequest(request);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}catch(IOException e){
			
		}
	}
	
}
	
	public static void main(String[] args) throws IOException {
		new GnGServer(9000);

	}

}
