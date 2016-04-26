package utd.persistentDataStore.datastoreServer;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import utd.persistentDataStore.datastoreServer.commands.ServerCommand;
import utd.persistentDataStore.datastoreServer.commands.ReadCommand;
import utd.persistentDataStore.datastoreServer.commands.WriteCommand;
import utd.persistentDataStore.datastoreServer.commands.DirectoryCommand;
import utd.persistentDataStore.datastoreServer.commands.DeleteCommand;
import utd.persistentDataStore.simpleSocket.server.EchoHandler;
import utd.persistentDataStore.simpleSocket.server.Handler;
import utd.persistentDataStore.simpleSocket.server.ReverseHandler;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreServer
{
	private static Logger logger = Logger.getLogger(DatastoreServer.class);

	static public final int port = 10023;

	public void startup() throws IOException
	{
		logger.debug("Starting Service at port " + port);

		ServerSocket serverSocket = new ServerSocket(port);

		InputStream inputStream = null;
		OutputStream outputStream = null;
		while (true) {
			try {
				logger.debug("Waiting for request");
				// The following accept() will block until a client connection 
				// request is received at the configured port number
				Socket clientSocket = serverSocket.accept();
				logger.debug("Request received");

				inputStream = clientSocket.getInputStream();
				outputStream = clientSocket.getOutputStream();

				ServerCommand command = dispatchCommand(inputStream);
				logger.debug("Processing Request: " + command);
				command.setInputStream(inputStream);
				command.setOutputStream(outputStream);
				command.run();
				
				StreamUtil.closeSocket(inputStream);
			}
			catch (ServerException ex) {
				String msg = ex.getMessage();
				logger.error("Exception while processing request. " + msg);
				StreamUtil.sendError(msg, outputStream);
				StreamUtil.closeSocket(inputStream);
			}
			catch (Exception ex) {
				logger.error("Exception while processing request. " + ex.getMessage());
				ex.printStackTrace();
				StreamUtil.closeSocket(inputStream);
			}
		}
	}

	private ServerCommand dispatchCommand(InputStream inputStream) throws ServerException
	{
		// Need to implement
		try {
			String commandString = StreamUtil.readLine(inputStream);
			
			if ("Read".equalsIgnoreCase(commandString)) {
				ServerCommand servCmd = new ReadCommand();
				return servCmd;
			}
			else if ("Write".equalsIgnoreCase(commandString)) {
				ServerCommand servCmd = new WriteCommand();
				return servCmd;
			}
			else if ("directory".equalsIgnoreCase(commandString)) {
				ServerCommand servCmd = new DirectoryCommand();
				return servCmd;
			}
			else if ("delete".equalsIgnoreCase(commandString)) {
				ServerCommand servCmd = new DeleteCommand();
				return servCmd;
			}
			else {	
				throw new ServerException("Unknown Request: " + commandString);
			}
		} catch(IOException e) {
			System.err.println("Unknown Request error: " + e);
		}
		return null;
	}

	public static void main(String args[])
	{
		DatastoreServer server = new DatastoreServer();
		try {
			server.startup();
		}
		catch (IOException ex) {
			logger.error("Unable to start server. " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
