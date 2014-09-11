
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {

	//private static InetAddress currentClientIP;
	//private static int currentClientPort;
	private static String answer;
	private static int randomNumber;
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DatagramSocket serverSocket = new DatagramSocket(9876);
		DatagramPacket receivePacket;
		DatagramPacket sendPacket;
		InetAddress IPAddress;
		int port;
		InetAddress currentClientIP = null;
		int currentClientPort = 0;
		
		
		String helloMsg = "HELLO";
		String okMsg = "OK";
		String startMsg = "START";
		String readyMsg = "READY";
		String busyMsg = "BUSY";
		String errorMsg = "ERROR";
		
		
		//init();
		
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while(true){
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			System.out.println("VEM:" + receivePacket.getSocketAddress());
			
			IPAddress = receivePacket.getAddress();
			port = receivePacket.getPort();
			
			if(currentClientIP == null){
				currentClientIP = IPAddress;
				currentClientPort = port;
			}
			
			if(currentClientIP.equals(IPAddress) && currentClientPort == port) {
				int guessedNumber = Integer.parseInt(new String(receivePacket.getData()).trim());
				
				System.out.println("RECEIVED: " + guessedNumber);
			
				if(randomNumber > guessedNumber){
					answer = "LO";
				} else if(randomNumber < guessedNumber){
					answer = "HI";
				} else {
					answer = "CORRECT";
				}
			
				sendData = answer.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			
				receiveData = new byte[1024];
				sendData = new byte[1024];
			
				serverSocket.send(sendPacket);
			} else {
				sendData = busyMsg.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
			
		}
	}
	
	public static void init(){
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		
	}
	
	public static void startGame(){
		randomNumber = randomizeNumber();
	}
	
	public static int randomizeNumber(){
		Random rand = new Random();
		return rand.nextInt(10)+1;
	}
}
