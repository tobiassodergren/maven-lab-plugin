/*
 * Copyright (C) 2009, 2010 Jayway AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.maven.plugins.lab;

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
	private static class Row {
		public Row(String line, Version version) {
			this.contents = line;
			this.version = version;
		}
	
		@Override
		public String toString() {
			return "Row [contents=" + contents + ", version=" + version + "]";
		}

		String contents;
	
		Version version;
	}

	private List<Row> rows = new ArrayList<Row>();

	private Version maxVersion = Version.ZERO;

	private Version leastVersion = Version.VERY_BIG;

	public boolean hasContents(int versionNumber) {
		return leastVersion.sameOrLessThan(versionNumber);
	}

	public void writeVersion(File dest, int versionNumber) throws IOException {
		Writer writer = new FileWriter(dest);
		try {
			writeVersion(writer, versionNumber);
		}
		finally {
			IOUtil.close(writer);
		}
	}

	public void writeVersion(Writer o, int versionNumber) {
		PrintWriter out = new PrintWriter(o);
		for (Row row : rows) {
			if (row.version.containsStuffFor(versionNumber)) {
				out.println(row.contents);
			}
		}
		out.flush();
	}

	public Version getMaxVersion() {
		return maxVersion;
	}

	public Version getLeastVersion() {
		return leastVersion;
	}

	public void add(String line, Version currentVersion) {
		rows.add(new Row(line, currentVersion));
		if (currentVersion.greaterThan(maxVersion)) {
			maxVersion = currentVersion;
		}
		if (currentVersion.lessThan(leastVersion)) {
			leastVersion = currentVersion;
		}
	}
}