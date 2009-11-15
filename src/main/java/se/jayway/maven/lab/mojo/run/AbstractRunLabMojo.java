package se.jayway.maven.lab.mojo.run;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import se.jayway.maven.lab.LabRunner;

public abstract class AbstractRunLabMojo extends AbstractMojo {

	/**
	 * Location of the target directory.
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File buildDirectory;

	/**
	 * Root for the project.
	 * @parameter expression="${basedir}"
	 * @required
	 */
	private File targetDirectory;

	public final void execute() throws MojoExecutionException, MojoFailureException {
		int currentStep = execute(new LabRunner(buildDirectory, targetDirectory));
		super.getLog().info("Current step is: " + currentStep);
	}

	protected abstract int execute(LabRunner lab);

}
