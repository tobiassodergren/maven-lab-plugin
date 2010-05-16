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
package se.jayway.maven.lab;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class LabProperties {
	private static final String MAX_STEP = "maxStep";
	private final int maxStep;

	public LabProperties(int maxStep) {
		this.maxStep = maxStep;
	}
	
	public static LabProperties load(File directory) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(getFile(directory)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new LabProperties(Integer.parseInt(properties.getProperty(MAX_STEP)));
	}

	public int getMaxStep() {
		return maxStep;
	}

	public void save(File outputDirectory) throws IOException {
		outputDirectory.mkdirs();
		Properties properties = new Properties();
		properties.setProperty(MAX_STEP, "" + maxStep);
		properties.store(new FileWriter(getFile(outputDirectory)), null);
	}

	private static File getFile(File outputDirectory) {
		return new File(outputDirectory, "lab.properties");
	}
	
	
}
