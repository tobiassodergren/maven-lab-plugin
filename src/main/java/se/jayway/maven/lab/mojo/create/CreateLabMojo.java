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
package se.jayway.maven.lab.mojo.create;

import java.io.File;
import java.io.IOException;

import se.jayway.maven.lab.FileIndex;
import se.jayway.maven.lab.FileUtil;
import se.jayway.maven.lab.LabCreator;
import se.jayway.maven.lab.LabProperties;
import se.jayway.maven.lab.LabRunner;
import se.jayway.maven.lab.VersionedContents;

/**
 * Goal to chunkify
 *
 * @goal create
 * @author Jan Kronquist
 */
public class CreateLabMojo extends AbstractCreateLabMojo {

	/**
     * The highest step number used in any of the files.
     * @parameter expression="${maxStep}"
     */
    private int maxStep;

    private int maxStepFound;

	private FileIndex fileIndex = new FileIndex();

	@Override
	protected void init(LabCreator labCreator) {
	}
	
	@Override
	protected void process(File file, VersionedContents versionedContents) throws IOException {
		if (versionedContents.getMaxVersion() == 0) {
			return;
		}
		boolean addedToIndex = false;
		int maxStepToUse = (maxStep != 0) ? maxStep : versionedContents.getMaxVersion();
		maxStepFound = Math.max(maxStepFound, maxStepToUse);
		for (int step=0; step<=maxStepToUse; step++) {
			if (versionedContents.hasContents(step)) {
				String relativePath = FileUtil.relativePathFromBase(file, getProject().getBasedir());
				File versionRoot = new File(getOutputDirectory(), "" + step);
				File dest = FileUtil.makeDestination(relativePath, versionRoot);
				versionedContents.writeVersion(dest, step);
				if (addedToIndex == false) {
					addedToIndex = true;
					fileIndex.addFile(relativePath, step);
				}
			}
		}
	}

	@Override
	protected String getOutputDirectoryName() {
		return LabRunner.LAB_DIRECTORY;
	}

	@Override
	protected void done() throws IOException {
		if (maxStepFound == 0) {
			throw new IllegalStateException("No steps found");
		}
		fileIndex.save(new File(getOutputDirectory(), FileIndex.INDEX_FILE_NAME));
		new LabProperties(maxStepFound).save(getOutputDirectory());
	}
}

