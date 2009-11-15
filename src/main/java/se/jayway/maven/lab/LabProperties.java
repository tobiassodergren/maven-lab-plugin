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
