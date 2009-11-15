package se.jayway.maven.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Kronquist
 */
public abstract class FileUtil {

	public static File makeDestination(String relativePath, File targetBaseDir) {
		File destination = new File(targetBaseDir, relativePath);
		if (destination.getParentFile().exists() == false) {
			destination.getParentFile().mkdirs();
		}
		return destination;
	}

	public static String relativePathFromBase(File file, File basedir) {
		String absolutePath = basedir.getAbsolutePath();
		if (file.getAbsolutePath().startsWith(absolutePath) == false) {
			throw new IllegalArgumentException("File not in basedir: " + file);
		}
		String relativePath = file.getAbsolutePath().substring(absolutePath.length());
		return relativePath;
	}

	public static List<String> loadFile(File file) throws IOException {
		List<String> lines = new ArrayList<String>();
		if (file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				line = line.trim();
	
				if (line.length() != 0) {
					lines.add(line);
				}
				line = reader.readLine();
			}
	
			reader.close();
		}
	
		return lines;
	}

}
