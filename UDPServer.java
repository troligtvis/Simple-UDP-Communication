
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {

	private static int currentClient;
	private static String answer;

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		DatagramSocket serverSocket = new DatagramSocket(9876);
		
		
		int randomNumber = randomizeNumber();
		
		while(true){
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			System.out.println("Nanting:" + receivePacket.getSocketAddress());

			int guessedNumber = Integer.parseInt(new String(receivePacket.getData()).trim());
			
			System.out.println("RECEIVED: " + guessedNumber);
			
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			if(randomNumber > guessedNumber){
				answer = "LO";
			} else if(randomNumber < guessedNumber){
				answer = "HI";
			} else {
				answer = "CORRECT";
			}
			
			sendData = answer.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			
			serverSocket.send(sendPacket);
		}
	}
	
	public static void startGame(){
		
	}
	
	public static int randomizeNumber(){
		Random rand = new Random();
		return rand.nextInt(10)+1;
	}
}
