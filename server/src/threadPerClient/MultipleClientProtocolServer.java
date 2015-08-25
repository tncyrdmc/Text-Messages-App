package threadPerClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import protocol_whatsapp.WhatsAppProtocolFactory;
import tokenizer_whatsapp.WhatsappTokenizerFactory;

public class MultipleClientProtocolServer<T> implements Runnable {
	private ServerSocket serverSocket;
	private int listenPort;
	private WhatsAppProtocolFactory _protocolFactory;
	private WhatsappTokenizerFactory _tokenizerFactory;
	private static boolean notExit;

	// Constructor
	public MultipleClientProtocolServer(int port, WhatsAppProtocolFactory protocolFactory, WhatsappTokenizerFactory tokenizerFactory)
	{
		serverSocket = null;
		listenPort = port;
		_protocolFactory = protocolFactory;
		_tokenizerFactory = tokenizerFactory;
		notExit = true;
	}

	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenPort);
			System.out.println("Listening....");
		}
		catch (IOException e) {
			System.out.println("Cannot listen on port " + listenPort);
		}

		while (notExit)
		{
			try {
				ConnectionHandler<String> newConnection = new ConnectionHandler<String>(serverSocket.accept(), _protocolFactory.create(),_tokenizerFactory.create());
				new Thread(newConnection).start();
			}
			catch (IOException e)
			{
				System.out.println("Failed to accept on port " + listenPort);
			}
		}
		try {
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Closes the connection
	public void close() throws IOException
	{
		serverSocket.close();
	}

	public static void main(String[] args) throws IOException
	{
		// Get port
		int port = Integer.decode(args[0]).intValue();
		
		System.out.println("Welcome to Whatsapp Server!");

		MultipleClientProtocolServer<String> server = new MultipleClientProtocolServer<String>(port, new WhatsAppProtocolFactory(), new WhatsappTokenizerFactory());
		Thread serverThread = new Thread(server);
		serverThread.start();
		
		ExecutorService ex= Executors.newFixedThreadPool(1);
		CompletionService<Boolean> isExit = new ExecutorCompletionService<Boolean>(ex);
		isExit.submit(new CallableScanner());
		ex.shutdown();
		try {
			ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			notExit = isExit.take().get().booleanValue();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
	
		try {
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			System.out.println("Server stopped");
		}
	}

}

