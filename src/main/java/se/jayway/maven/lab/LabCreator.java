package se.jayway.maven.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import org.codehaus.plexus.util.IOUtil;

/**
 * 
 * @author Jan Kronquist
 */
public class LabCreator {
	
	private static final String BEGIN = "@BEGIN_VERSION";
	private static final String END = "@END_VERSION";

	public VersionedContents labify(File file) throws IOException {
		return labify(new FileReader(file), file.getAbsolutePath());
	}

	public VersionedContents labify(Reader r, String name) throws IOException {
		BufferedReader reader = new BufferedReader(r);
		try {
			VersionedContents contents = new VersionedContents();
			int currentVersion = 0;
			Stack<Integer> versions = new Stack<Integer>();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				String parsed = line.trim();
				if (parsed.contains(BEGIN)) {
					versions.push(currentVersion);
					currentVersion = Integer.parseInt(parsed.substring(parsed.indexOf(BEGIN)+BEGIN.length()).trim());
				} else if (parsed.contains(END)) {
					int endVersion = Integer.parseInt(parsed.substring(parsed.indexOf(END)+END.length()).trim());
					if (currentVersion != endVersion) {
						throw new IllegalArgumentException("Incorrect end-tag! expected " + currentVersion + " but was " + endVersion + ": " + name);
					}
					currentVersion = versions.pop();
				} else {
					contents.add(line, currentVersion);
				}
			}
			return contents;
		} finally {
			IOUtil.close(reader);
		}
	}

}

