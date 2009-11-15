package se.jayway.maven.lab;

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

	private static final String CURRENT_STEP_PREFIX = "Current step ";
	private String labCurrentStepFileName;
	private final File labStorageDirectory;
	private final LabProperties labProperties;
	private final File targetDirectory;

	public LabRunner(File buildDirectory, File targetDirectory) {
		this.targetDirectory = targetDirectory;
		this.labCurrentStepFileName = new File(buildDirectory,
				"labCurrentStep.txt").getAbsolutePath();
		this.labStorageDirectory = new File(buildDirectory, "chunker");
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
