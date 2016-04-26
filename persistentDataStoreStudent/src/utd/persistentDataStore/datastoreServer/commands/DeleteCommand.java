package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import org.apache.log4j.Logger;

public class DeleteCommand extends ServerCommand {

	private static Logger logger = Logger.getLogger(DeleteCommand.class);
	
	@Override
	public void run() throws IOException, ServerException {
		// Read message
		String inMessage = StreamUtil.readLine(inputStream);
		logger.debug("inMessage: " + inMessage);

		// Write response
		String outMessage = reverse(inMessage) + "\n";
		StreamUtil.writeLine(outMessage, outputStream);
		logger.debug("Finished writing message");
	}

	private String reverse(String data)
	{
		byte dataBuff[] = data.getBytes();
		int buffSize = dataBuff.length;
		byte reverseBuff[] = new byte[buffSize];
		for (int idx = 0; idx < buffSize; idx++) {
			reverseBuff[idx] = dataBuff[(buffSize - idx) - 1];
		}
		return new String(reverseBuff);
	}
}
