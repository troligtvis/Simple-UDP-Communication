import java.io.*;
import java.net.*;

public class UDPClient {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		clientSocket.setSoTimeout(20000);
		try{
			while(true){
				
					byte[] sendData = new byte[1024];
					byte[] receiveData = new byte[1024];
					String sentence = inFromUser.readLine();
					sendData = sentence.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6701);
					clientSocket.send(sendPacket);
				try{
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					String modifiedSentence = new String(receivePacket.getData());
					System.out.println("FROM SERVER: " + modifiedSentence);
					if(modifiedSentence.trim().equals("BYE")){
						//denna if-sats funkar inte, eftersom klienten aldrig skickar n�gonting 
						break;
					}
				}catch(SocketTimeoutException e){
					System.out.println("Server not responding");
				}
			}
		}catch(SocketException e){
			System.out.println("Socket error: " + e.getMessage());
		}catch(IOException e){
            System.out.println("IO: " + e.getMessage());
		}finally{
			if(clientSocket!=null){
				clientSocket.close();
			}
		}
	}

}
