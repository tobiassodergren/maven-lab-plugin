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
import java.io.IOException;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;

/**
 * 
 * 
 * @author Jan Kronquist
 */
public class LabRunner {

	public static final String LAB_DIRECTORY = "lab";	// TODO: move elsewhere
	
	private static final String CURRENT_STEP_PREFIX = "Current step ";
	private String labCurrentStepFileName;
	private final File labStorageDirectory;
	private final LabProperties labProperties;
	private final File targetDirectory;

	public LabRunner(File buildDirectory, File targetDirectory) {
		this.targetDirectory = targetDirectory;
		this.labCurrentStepFileName = new File(buildDirectory,
				"labCurrentStep.txt").getAbsolutePath();
		this.labStorageDirectory = new File(buildDirectory, LAB_DIRECTORY);
		labProperties = LabProperties.load(labStorageDirectory);
	}

	public int changeTo(int stepNo) {
		if (stepNo > getMaxStep()) {
			return getCurrentStep();
		}
		logCurrentStep("Switching to step " + stepNo);
		FileIndex fileIndex;
		try {
			fileIndex = FileIndex.load(new File(labStorageDirectory, FileIndex.INDEX_FILE_NAME));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (String fileName : fileIndex.getFiles(stepNo)) {
			File sourceFile = findSourceFile(stepNo, fileName);
			File targetFile = new File(targetDirectory, fileName);
			try {
				FileUtils.copyFile(sourceFile, targetFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		for (String fileName : fileIndex.getFilesNotIn(stepNo)) {
			File targetFile = new File(targetDirectory, fileName);
			targetFile.delete();
		}
		logCurrentStep(CURRENT_STEP_PREFIX + stepNo);
		return stepNo;
	}

	private File findSourceFile(int stepNo, String fileName) {
		while (true) {
			File sourceFile = new File(directoryForStep(stepNo), fileName);
			if (sourceFile.exists()) {
				return sourceFile;
			} else {
				stepNo--;
				if (stepNo < 0) {
					throw new IllegalStateException("Could not find step information for: " + fileName);
				}
			}
		}
	}

	private File directoryForStep(int stepNo) {
		return new File(labStorageDirectory, "" + stepNo);
	}

	private void logCurrentStep(String msg) {
		try {
			FileUtils.fileAppend(labCurrentStepFileName, msg + "\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int nextStep() {
		return changeTo(getCurrentStep()+1);
	}
	
	public int getMaxStep() {
		return labProperties.getMaxStep();
	}

	public int getCurrentStep() {
		try {
			List<String> lines = FileUtil.loadFile(new File(labCurrentStepFileName));
			int currentStep = 0;
			for (String line : lines) {
				if (line.startsWith(CURRENT_STEP_PREFIX)) {
					currentStep = Integer.parseInt(line
							.substring(CURRENT_STEP_PREFIX.length()));
				}
			}
			return currentStep;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
