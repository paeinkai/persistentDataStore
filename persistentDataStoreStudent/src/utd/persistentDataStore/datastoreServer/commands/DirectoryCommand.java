package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import org.apache.log4j.Logger;

public class DirectoryCommand extends ServerCommand {

	private static Logger logger = Logger.getLogger(DirectoryCommand.class);
	
	@Override
	public void run() throws IOException, ServerException {
		// Read message
		String inMessage = StreamUtil.readLine(inputStream);
		logger.debug("inMessage: " + inMessage);

		// Write response
		String outMessage = inMessage + "\n";
		StreamUtil.writeLine(outMessage, outputStream);
		logger.debug("Finished writing message");
	}

}