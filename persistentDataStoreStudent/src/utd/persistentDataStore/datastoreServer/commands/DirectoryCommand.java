package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.List;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import org.apache.log4j.Logger;

public class DirectoryCommand extends ServerCommand {

	private static Logger logger = Logger.getLogger(DirectoryCommand.class);
	
	@Override
	public void run() throws IOException, ServerException {
		logger.debug("DirectoryCommand received");
		List<String> directory = FileUtil.directory();
		this.sendOK();
		StreamUtil.writeLine(""+directory.size(), outputStream);
		for (String filename : directory)
		{
			StreamUtil.writeLine(filename, outputStream);
		}
		logger.debug("Finished sending directory");
	}
	
}
