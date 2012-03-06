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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.codehaus.plexus.util.IOUtil;

/**
 * 
 * @author Jan Kronquist
 */
public class LabCreator {

	private static final String BEGIN_VERSION_ONLY = "@BEGIN_VERSION_ONLY";

	private static final String END_VERSION_ONLY = "@END_VERSION_ONLY";

	private static final String BEGIN_VERSION = "@BEGIN_VERSION";

	private static final String END_VERSION = "@END_VERSION";

	private static final String BEGIN_UP_TO_VERSION = "@BEGIN_UP_TO_VERSION";

	private static final String END_UP_TO_VERSION = "@END_UP_TO_VERSION";

	private Map<String, Integer> constants;

	public LabCreator() {
		constants = new HashMap<String, Integer>();
	}

	public LabCreator(String... constants) {
		this(Arrays.asList(constants));
	}

	public LabCreator(List<String> constants) {
		int index = 0;
		this.constants = new HashMap<String, Integer>();
		for (String constant : constants) {
			this.constants.put(constant, index++);
		}
	}

	public VersionedContents labify(File file) throws IOException {
		return labify(new FileReader(file), file.getAbsolutePath());
	}

	public VersionedContents labify(Reader r, String name) throws IOException {
		BufferedReader reader = new BufferedReader(r);
		try {
			VersionedContents contents = new VersionedContents();
			Version currentVersion = Version.ZERO;
			Stack<Version> versions = new Stack<Version>();
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				String parsed = line.trim();
				if (parsed.contains(BEGIN_VERSION_ONLY)) {
					versions.push(currentVersion);
					currentVersion = parseVersion(parsed);
				}
				else if (parsed.contains(END_VERSION_ONLY)) {
					Version endVersion = parseVersion(parsed);
					if (!currentVersion.equals(endVersion)) {
						throw new IllegalArgumentException("Incorrect end-tag! expected " + currentVersion
								+ " but was " + endVersion + ": " + name);
					}
					currentVersion = versions.pop();
				}
				else if (parsed.contains(BEGIN_UP_TO_VERSION)) {
					versions.push(currentVersion);
					currentVersion = parseVersion(parsed);
				}
				else if (parsed.contains(END_UP_TO_VERSION)) {
					Version endVersion = parseVersion(parsed);
					if (!currentVersion.equals(endVersion)) {
						throw new IllegalArgumentException("Incorrect end-tag! expected " + currentVersion
								+ " but was " + endVersion + ": " + name);
					}
					currentVersion = versions.pop();
				}
				else if (parsed.contains(BEGIN_VERSION)) {
					versions.push(currentVersion);
					currentVersion = parseVersion(parsed);
				}
				else if (parsed.contains(END_VERSION)) {
					Version endVersion = parseVersion(parsed);
					if (!currentVersion.equals(endVersion)) {
						throw new IllegalArgumentException("Incorrect end-tag! expected " + currentVersion
								+ " but was " + endVersion + ": " + name);
					}
					currentVersion = versions.pop();
				}
				else {
					contents.add(line, currentVersion);
				}
			}
			return contents;
		}
		finally {
			IOUtil.close(reader);
		}
	}

	protected Version parseVersion(String string) {
		if (string.contains(BEGIN_VERSION_ONLY)) {
			int version = parseInt(string.substring(string.indexOf(BEGIN_VERSION_ONLY) + BEGIN_VERSION_ONLY.length())
					.trim());
			return new Version(version, Version.TriggerType.CONTENT_IN_THIS_VERSION_ONLY);
		}
		else if (string.contains(END_VERSION_ONLY)) {
			int version = parseInt(string.substring(string.indexOf(END_VERSION_ONLY) + END_VERSION_ONLY.length())
					.trim());
			return new Version(version, Version.TriggerType.CONTENT_IN_THIS_VERSION_ONLY);
		}
		else if (string.contains(BEGIN_UP_TO_VERSION)) {
			int version = parseInt(string.substring(string.indexOf(BEGIN_UP_TO_VERSION) + BEGIN_UP_TO_VERSION.length())
					.trim());
			return new Version(version, Version.TriggerType.UP_TO_VERSION);
		}
		else if (string.contains(END_UP_TO_VERSION)) {
			int version = parseInt(string.substring(string.indexOf(END_UP_TO_VERSION) + END_UP_TO_VERSION.length())
					.trim());
			return new Version(version, Version.TriggerType.UP_TO_VERSION);
		}
		else if (string.contains(BEGIN_VERSION)) {
			int version = parseInt(string.substring(string.indexOf(BEGIN_VERSION) + BEGIN_VERSION.length()).trim());
			return new Version(version);
		}
		else { // END_VERSION
			int version = parseInt(string.substring(string.indexOf(END_VERSION) + END_VERSION.length()).trim());
			return new Version(version);
		}
	}

	private int parseInt(String string) {
		String value = beforeFirstWhiteSpace(string);
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e) {
			Integer integer = constants.get(value);
			if (integer == null) {
				throw new IllegalArgumentException("Constant not defined: " + value);
			}
			return integer;
		}
	}

	private String beforeFirstWhiteSpace(String string) {
		for (int indx = 0; indx < string.length(); indx++) {
			if (Character.isWhitespace(string.charAt(indx))) {
				return string.substring(0, indx);
			}
		}
		return string;
	}
}
