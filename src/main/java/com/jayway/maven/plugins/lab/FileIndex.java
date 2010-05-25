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
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FileIndex {
	public static final String INDEX_FILE_NAME = "index.txt";

	private static class FileIndexInfo {
		public FileIndexInfo(String relativePath, int firstStep) {
			this.relativePath = relativePath;
			this.firstStep = firstStep;
		}
		private final String relativePath;
		private final int firstStep;
	}
	
	private List<FileIndexInfo> fileIndex = new LinkedList<FileIndexInfo>();

	public void addFile(String relativePath, int firstStep) {
		fileIndex.add(new FileIndexInfo(relativePath, firstStep));
	}

	public void save(File dest) throws IOException {
		PrintWriter writer = new PrintWriter(dest);
		try {
			for (FileIndexInfo info : fileIndex) {
				writer.println(info.firstStep + ":" + info.relativePath);
			}
		} finally {
			writer.close();
		}
	}
	
	public static FileIndex load(File source) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(source));
		try {
			FileIndex fileIndex = new FileIndex();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				String[] split = line.split(":");
				fileIndex.addFile(split[1], Integer.parseInt(split[0]));
			}
			return fileIndex;
		} finally {
			reader.close();
		}
	}

	public Iterable<String> getFiles(int stepNo) {
		List<String> list = new LinkedList<String>();
		for (FileIndexInfo info : fileIndex) {
			if (info.firstStep <= stepNo) {
				list.add(info.relativePath);
			}
		}
		return Collections.unmodifiableList(list);
	}

	public Iterable<String> getFilesNotIn(int stepNo) {
		List<String> list = new LinkedList<String>();
		for (FileIndexInfo info : fileIndex) {
			if (info.firstStep > stepNo) {
				list.add(info.relativePath);
			}
		}
		return Collections.unmodifiableList(list);
	}
}