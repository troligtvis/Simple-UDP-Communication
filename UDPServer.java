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

		while(true){
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
				
					if(answer.equals("CORRECT")){
						currentClientIP = null;
						currentClientPort = 0;
						isGameStarted = false;
					}
				}
				
			} else {
				sendData = busyMsg.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			}
			
			serverSocket.send(sendPacket);
			receiveData = new byte[1024];
			sendData = new byte[1024];
		}
	}
	
	public static void startGame(){
		randomNumber = randomizeNumber();
	}
	
	public static int randomizeNumber(){
		Random rand = new Random();
		return rand.nextInt(10)+1;
	}
}
