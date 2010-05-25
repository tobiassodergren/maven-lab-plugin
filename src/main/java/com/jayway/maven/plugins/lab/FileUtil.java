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
