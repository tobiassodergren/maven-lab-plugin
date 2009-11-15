package se.jayway.maven.lab.mojo.create;

import java.io.File;
import java.io.IOException;

import se.jayway.maven.lab.LabRunner;

/**
 * Goal to create and initialize a lab.
 *
 * @goal init
 * @author Jan Kronquist
 */
public class InitLabMojo extends CreateLabMojo {
	/**
	 * Root for the project.
	 * @parameter expression="${basedir}"
	 * @required
	 */
	private File targetDirectory;

	@Override
	protected void done() throws IOException {
		super.done();
		new LabRunner(buildDirectory, targetDirectory).changeTo(0);
	}

}
