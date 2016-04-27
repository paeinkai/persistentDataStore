package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import org.apache.log4j.Logger;

public class DeleteCommand extends ServerCommand {

	private static Logger logger = Logger.getLogger(DeleteCommand.class);
	
	@Override
	public void run() throws IOException, ServerException 
	{
		String filename = StreamUtil.readLine(inputStream);
		
		logger.debug("DeleteCommand filename: " + filename);

		if (FileUtil.deleteData(filename))
			this.sendOK();	
		
		logger.debug("Delete file successful");
	}

}
