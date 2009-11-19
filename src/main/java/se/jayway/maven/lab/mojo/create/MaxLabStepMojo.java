package se.jayway.maven.lab.mojo.create;

import java.io.File;
import java.io.IOException;

import se.jayway.maven.lab.LabCreator;
import se.jayway.maven.lab.VersionedContents;

/**
 * Goal to find the max version
 *
 * @goal findMax
 * @author Jan Kronquist
 */
public class MaxLabStepMojo extends AbstractCreateLabMojo {
	
	private int maxVersion = 0;

	@Override
	protected String getOutputDirectoryName() {
		return "chunker";
	}

	@Override
	protected void init(LabCreator labCreator) {
	}

	@Override
	protected void process(File file, VersionedContents versionedContents) {
		maxVersion = Math.max(maxVersion, versionedContents.getMaxVersion());
	}

	@Override
	protected void done() throws IOException {
		getLog().info("========================");
		getLog().info("Max version is " + maxVersion);
		getLog().info("========================");
	}

}
