package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import org.apache.log4j.Logger;

public class WriteCommand extends ServerCommand {

	private static Logger logger = Logger.getLogger(WriteCommand.class);

	@Override
	public void run() throws IOException, ServerException {
		String filename = StreamUtil.readLine(inputStream);
		logger.debug("WriteCommand filename: " + filename);
		int size = Integer.parseInt(StreamUtil.readLine(inputStream));
		byte[] data = StreamUtil.readData(size, inputStream);
		FileUtil.writeData(filename, data);	
		this.sendOK();
		logger.debug("Finished writing file");
	}
	
	
}
