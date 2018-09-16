package v4.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import v4.common.CloudFileWriter;
import v4.common.types.FileChunk;
import v4.common.types.FileManifest;
import v4.common.types.ServerMessage;
import v4.common.types.GetFileChunkClientMessage;
import v4.common.types.GetFileClientMessage;
import v4.common.types.GetSingleFileManifestClientMessage;
import v4.common.types.SingleFileManifestServerMessage;

public class SplitFileGetter {
	
	public ConcurrentHashMap<Integer, FileChunk> buffer = new ConcurrentHashMap();
	
	public void getFileInChunks(GetFileClientMessage clientMessage) {
		// Get File Manifest
		FileManifest fileManifest = getFileManifestFromServer(clientMessage.fileName);
		// For each chunk, spawn a slave to fetch a chunk, and add it to the buffer
		for(int chunkNumber = 0; chunkNumber < fileManifest.numChunks; chunkNumber++) {
			fetchAndWriteToBuffer(clientMessage.fileName, chunkNumber);
		}
		// Spawn a new thread that appends data the first (by chunk index) available chunk to the file 
		appendChunksToFile(fileManifest);
	}

	private void fetchAndWriteToBuffer(String fileName, int chunkNumber) {
		new Thread(new FileChunkFetcher(fileName, chunkNumber, getNewSocket(), buffer)).start();
	}

	private FileManifest getFileManifestFromServer(String fileName) {
		try {
			Socket soc = getNewSocket();
			ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
	        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
	        out.writeObject(new GetSingleFileManifestClientMessage(fileName));
	        return ((SingleFileManifestServerMessage)in.readObject()).fileManifest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void appendChunksToFile(FileManifest fileManifest) {
		new Thread(new FileChunkAppender(fileManifest, buffer)).start();
	}

	private Socket getNewSocket() {
		try {
			return new Socket("192.168.56.110", 5217);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

class FileChunkFetcher implements Runnable {
	
	String fileName;
	int chunkNumber;
	Socket soc;
	ConcurrentHashMap<Integer, FileChunk> buffer;
	
	FileChunkFetcher(
			String fileName, 
			int chunkNumber, 
			Socket soc, 
			ConcurrentHashMap<Integer, FileChunk> buffer) {
		this.fileName = fileName;
		this.chunkNumber = chunkNumber;
		this.soc = soc;
		this.buffer = buffer;
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
	        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
	        out.writeObject(new GetFileChunkClientMessage(fileName, chunkNumber));
	        ServerMessage serverMessage = ((ServerMessage)in.readObject());
	        if (serverMessage.hasError) {
	        	throw new RuntimeException("Error while reading chunk: " + serverMessage.errorCause);
	        }
	        FileChunk fileChunk = new FileChunk(chunkNumber, serverMessage.message);
	        buffer.put(chunkNumber, fileChunk);
	        System.out.println("Fetched & Buffered chunk # " + chunkNumber);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class FileChunkAppender implements Runnable {
	
	FileManifest fileManifest;
	ConcurrentHashMap<Integer, FileChunk> buffer;
	
	FileChunkAppender(
			FileManifest fileManifest, 
			ConcurrentHashMap<Integer, FileChunk> buffer) {
		this.fileManifest = fileManifest;
		this.buffer = buffer;
	}

	@Override
	public void run() {
		int chunkNumber = 0;
		int numTries = 0;
		while(chunkNumber < fileManifest.numChunks) {
			if (buffer.containsKey(chunkNumber)) {
				try {
					System.out.println("Trying to append chunk - " + chunkNumber);
					new CloudFileWriter().appendToFile(fileManifest.fileName, 
							buffer.get(chunkNumber).chunk);
					chunkNumber++;
					numTries = 0;
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("Error writing to chunk - " + chunkNumber);
				}
			} else {
				try {
					Thread.sleep(10);
					numTries++;
					if (numTries > 10) {
						throw new RuntimeException("Exhausted number of retries of waiting for chunk");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
}
