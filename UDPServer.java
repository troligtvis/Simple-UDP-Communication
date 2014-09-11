/*
 * 
 * Simple UDP connection program
 * Created by Karl-Johan Drougge and Danijel Kljakic
 * 2014-09-11
 * 
 * 
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {

	private static String answer;
	private static int randomNumber;
	
	
	public static void main(String[] args) throws Exception {
		
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
		
		boolean isGameStarted = false;

		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		serverSocket.setSoTimeout(5000);
		while(true){
			try{
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
			
				System.out.println("WHO:" + receivePacket.getSocketAddress());
				
				IPAddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				
				String initMsg = new String(receivePacket.getData()).trim();
				
				if(currentClientIP == null){
					if(initMsg.equals(helloMsg)){
						currentClientIP = IPAddress;
						currentClientPort = port;
						sendData = okMsg.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					} else {
						sendData = errorMsg.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					}
				} else if(currentClientIP.equals(IPAddress) && currentClientPort == port) {
					if(!isGameStarted){
						String startClientMsg = new String(receivePacket.getData()).trim();
						if(startClientMsg.equals(startMsg)){
							startGame();
							sendData = readyMsg.getBytes();
							sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
							isGameStarted = true;
						} else {
							sendData = errorMsg.getBytes();
						}
						
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					} else{
						String guessStr = new String(receivePacket.getData()).trim();
						int number = checkAndStripToNumber(guessStr);
						if(number != -1){
							int guessedNumber = number;
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
					
							if(answer.equals("CORRECT")){
								currentClientIP = null;
								currentClientPort = 0;
								isGameStarted = false;
							}
						} else {
							sendData = errorMsg.getBytes();
							sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
						}
					}
					
				} else {
					sendData = busyMsg.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				}
				
				serverSocket.send(sendPacket);
				receiveData = new byte[1024];
				sendData = new byte[1024];
			}catch(SocketException e){
				System.out.println("Socket error: " + e.getMessage());
			}catch(SocketTimeoutException s){
				System.out.println("SKRIV HÄR VAD SOM HÄNDER");
			}finally{
				if (serverSocket != null) 
					serverSocket.close();
			}
		}
	}
	
	public static void startGame(){
		randomNumber = randomizeNumber();
	}
	
	public static int checkAndStripToNumber(String checkMe){
		if(checkMe.length() >= 6 ){
			String first = checkMe.substring(0, 6);
			String second = checkMe.substring(first.length(), checkMe.length());
			String guessMsg = "GUESS ";
			
			if(first.equals(guessMsg)){
				if(isNumeric(second)){
					int guessNr = Integer.parseInt(second);
					return guessNr;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		}
		return -1;
	}
	
	public static boolean isNumeric(String possibleNumericString){
		try{  
		    int i = Integer.parseInt(possibleNumericString);  
		  } catch(NumberFormatException nfe){  
		    return false;  
		  }

		return true;  
	}
	
	public static int randomizeNumber(){
		Random rand = new Random();
		return rand.nextInt(10)+1;
	}
}
