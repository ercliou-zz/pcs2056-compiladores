package compilador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Classe que extrai a string do texto fonte a partir do arquivo fonte
 * 
 */
public class FileExtractor {

	/**
	 * 
	 * @param fileName
	 *            Nome do arquivo que contém o texto fonte
	 * @return A string contendo o texto fonte
	 * @throws IOException
	 */
	public static String extract(String fileName) throws IOException {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(fileName));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}
}
