package se.jayway.maven.lab;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.IOUtil;

/**
 * @author Jan Kronquist
 */
public class VersionedContents {
	private List<Row> rows = new ArrayList<Row>();
	private int maxVersion = 0;
	private int leastVersion = Integer.MAX_VALUE;

	public boolean hasContents(int version) {
		return version >= leastVersion;
	}

	public void writeVersion(File dest, int version) throws IOException {
		Writer writer = new FileWriter(dest);
		try {
			writeVersion(writer, version);
		} finally {
			IOUtil.close(writer);
		}
	}
	
	public void writeVersion(Writer o, int version) {
		PrintWriter out = new PrintWriter(o);
		for (Row row : rows) {
			if (row.version <= version) {
				out.println(row.contents);
			}
		}
		out.flush();
	}

	public int getMaxVersion() {
		return maxVersion;
	}

	public int getLeastVersion() {
		return leastVersion;
	}

	public void add(String line, int currentVersion) {
		rows.add(new Row(line, currentVersion));
		if (currentVersion > maxVersion) {
			maxVersion = currentVersion;
		}
		if (currentVersion < leastVersion) {
			leastVersion = currentVersion;
		}
	}
}

class Row {
	public Row(String line, int version) {
		this.contents = line;
		this.version = version; 
	}
	String contents;
	int version;
}