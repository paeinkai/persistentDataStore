package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;


	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;

	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte data[]) throws ClientException
	{
		logger.debug("Executing Write Operation");
		try {
			/*
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			*/
			Socket socket = new Socket(address, port);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");
			StreamUtil.writeLine("write", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(Integer.toString(data.length), outputStream);
			StreamUtil.writeData(data, outputStream);
			
			logger.debug("Reading response");
			String response = StreamUtil.readLine(inputStream);
			logger.debug("Response code:" + response);
			StreamUtil.closeSocket(inputStream);
			if (!"ok".equalsIgnoreCase(response))
			{
				throw new ClientException(response);
			}
		}
		catch (IOException ex) {
			System.err.println("IOException on write");
			ex.printStackTrace();
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException
	{
		logger.debug("Executing Read Operation");
		byte readResp[];
		String size;
		
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket(address, port);
			
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Read Message");
			StreamUtil.writeLine("read", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			logger.debug("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			if ("ok".equalsIgnoreCase(response))
			{
				size = StreamUtil.readLine(inputStream);
				int sz = Integer.parseInt(size);
				readResp = StreamUtil.readData(sz, inputStream);
				logger.debug("Response " + response);
				socket.close();
			}
			else
			{
				socket.close();
				throw new ClientException(response);
			}
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
		
		return readResp;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException
	{
		logger.debug("Executing Delete Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket(address, port);

			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Delete Message");
			StreamUtil.writeLine("delete", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			logger.debug("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			socket.close();
			if (!"ok".equalsIgnoreCase(response))
			{
				throw new ClientException(response);
				
			}
			
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException
	{
		logger.debug("Executing Directory Operation");
		ArrayList<String> nameList = new ArrayList<String>();
		byte bList[];
		
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket(address, port);

			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Directory Message");
			StreamUtil.writeLine("directory", outputStream);
			
			logger.debug("Reading Response");
			String response = StreamUtil.readLine(inputStream);
			if ("ok".equalsIgnoreCase(response))
			{
				String num = StreamUtil.readLine(inputStream);
				int nameSize = Integer.parseInt(num);
				for (int i=0; i < nameSize ; i++) {	
					bList = StreamUtil.readData(nameSize, inputStream);
					String name = new String(bList);
					nameList.add(name);
				}
				socket.close();
			}
			else
			{
				socket.close();
				throw new ClientException(response);
			}
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
		return nameList;
	}

}
