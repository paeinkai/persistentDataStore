	package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import org.apache.log4j.Logger;

public class ReadCommand extends ServerCommand {
	
	private static Logger logger = Logger.getLogger(ReadCommand.class);

	@Override
	public void run() throws IOException, ServerException {
		String filename = StreamUtil.readLine(inputStream);
		
		logger.debug("ReadCommand filename: " + filename);

		byte[] data = FileUtil.readData(filename);
		this.sendOK();
		StreamUtil.writeLine(""+data.length, outputStream);
		StreamUtil.writeData(data, outputStream);
		
		logger.debug("Finished reading file.");
	}

}
