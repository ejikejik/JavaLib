package ejik.util.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zipper {
	public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
		Inflater decompressor = new Inflater();
		decompressor.setInput(data);
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
	    byte[] buf = new byte[1024];
	    while (!decompressor.finished()) {
	    	int count = decompressor.inflate(buf);
	    	bos.write(buf, 0, count);
	    }
	    bos.close();
	    return bos.toByteArray();
	}
	
	public static byte[] compress(byte[] data) throws IOException {
		 Deflater compressor = new Deflater();
		 compressor.setLevel(Deflater.BEST_COMPRESSION);
		 compressor.setInput(data);
		 compressor.finish();
		 ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		 byte[] buf = new byte[1024];
		 while (!compressor.finished()) {
			 int count = compressor.deflate(buf);
			 bos.write(buf, 0, count);
		 }
		 bos.close();
		 return bos.toByteArray();
	}
}
