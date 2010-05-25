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
package com.jayway.maven.plugins.lab.mojo.create;

import java.io.File;
import java.io.IOException;

import com.jayway.maven.plugins.lab.FileUtil;
import com.jayway.maven.plugins.lab.LabCreator;
import com.jayway.maven.plugins.lab.VersionedContents;


/**
 * @author Jan Kronquist
 */
public abstract class AbstractCreateLabMojo extends AbstractProjectFilesMojo {

	/**
	 * Root for the project.
	 * @parameter expression="${basedir}/labStepConstants.properties"
	 * @required
	 */
	private File labStepConstantsFile;

	private LabCreator labCreator;

	@Override
	protected final void init() {
		if (labStepConstantsFile.exists()) {
			try {
				labCreator = new LabCreator(FileUtil.loadFile(labStepConstantsFile));
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
