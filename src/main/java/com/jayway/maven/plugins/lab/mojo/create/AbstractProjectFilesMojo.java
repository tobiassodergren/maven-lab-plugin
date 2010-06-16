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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

import com.jayway.maven.plugins.lab.mojo.AbstractLabMojo;

/**
 * 
 * @author Jan Kronquist
 */
public abstract class AbstractProjectFilesMojo extends AbstractLabMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

    /**
     * List of files to include. Specified as fileset patterns.
     *
     * @parameter
     */
    private String[] includes = new String[] {"src/**/*"};

    /**
     * List of files to exclude. Specified as fileset patterns.
     *
     * @parameter
     */
    private String[] excludes = new String[] {"**/target/**", "**/.*/**"};

	private boolean skipHiddenFiles = true;
	
	private boolean overwrite = false;

	public MavenProject getProject() {
		return project;
	}

	public final void execute() throws MojoExecutionException {
		if (!project.isExecutionRoot()) {
			return;
		}
		
		if (getLabStorageDirectory().exists()) {
			if (!overwrite) {
				super.getLog().error("Lab already initialized: " + getLabStorageDirectory());
				return;
			} else {
				deleteLabStorage();
			}
		}
		
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(project.getBasedir());
		scanner.setIncludes(includes);
		scanner.setExcludes(excludes);
		scanner.scan();

		init();

		try {
			for (String fileName : scanner.getIncludedFiles()) {
				File file = new File(fileName);
				if (file.getName().startsWith(".") && skipHiddenFiles) {
					// ignore
				} else {
					process(file);
				}
			}
			done();
		} catch (IOException e) {
			throw new MojoExecutionException("Failed", e);
		}
	}

	protected abstract void process(File file) throws IOException;
	protected abstract void init();
	protected abstract void done() throws IOException;

}

