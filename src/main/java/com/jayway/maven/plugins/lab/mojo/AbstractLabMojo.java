package com.jayway.maven.plugins.lab.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

import com.jayway.maven.plugins.lab.LabRunner;

public abstract class AbstractLabMojo extends AbstractMojo {
	protected static final String ORIGINAL = "original";

	/**
	 * Root for the project.
	 * 
	 * @parameter expression="${basedir}"
	 * @required
	 */
	private File targetDirectory;

	/**
	 * Temporary storage when running the lab.
	 * 
	 * @parameter expression="${basedir}/.lab"
	 * @required
	 */
	private File labStorageDirectory;

	public File getLabStorageDirectory() {
		return labStorageDirectory;
	}

	public File getTargetDirectory() {
		return targetDirectory;
	}

	protected LabRunner createLabRunner() {
		return new LabRunner(targetDirectory, labStorageDirectory);
	}

	protected void deleteLabStorage() throws MojoExecutionException {
		try {
			FileUtils.deleteDirectory(labStorageDirectory);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to delete lab storage: " + labStorageDirectory, e);
		}
	}
	
	protected void assertLabInitialized() throws MojoFailureException {
		if (!labStorageDirectory.exists()) {
			throw new MojoFailureException("Lab not initialized!");
		}
	}

}
