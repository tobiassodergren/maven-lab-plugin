package se.jayway.maven.lab.mojo.create;

import java.io.File;
import java.io.IOException;

import se.jayway.maven.lab.FileUtil;
import se.jayway.maven.lab.LabCreator;
import se.jayway.maven.lab.VersionedContents;

/**
 * @author Jan Kronquist
 */
public abstract class AbstractCreateLabMojo extends AbstractProjectFilesMojo {

	/**
	 * Root for the project.
	 * @parameter expression="${basedir}/labStepContants.properties"
	 * @required
	 */
	private File labStepContantFile;

	private LabCreator labCreator;

	@Override
	protected final void init() {
		if (labStepContantFile.exists()) {
			try {
				labCreator = new LabCreator(FileUtil.loadFile(labStepContantFile));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			labCreator = new LabCreator();
		}
		init(labCreator);
	}


	@Override
	protected final void process(File file) throws IOException {
		process(file, labCreator.labify(file));
	}

	protected abstract void init(LabCreator labCreator);
	protected abstract void process(File file, VersionedContents labify) throws IOException;
}
