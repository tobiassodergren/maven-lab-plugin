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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * 
 * @author Jan Kronquist
 */
public abstract class AbstractProjectFilesMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Location of the file.
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected File buildDirectory;
	
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

	private File outputDirectory;

	public MavenProject getProject() {
		return project;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public final void execute() throws MojoExecutionException {
		if (!project.isExecutionRoot()) {
			return;
		}
		
		this.outputDirectory = new File(buildDirectory, getOutputDirectoryName());
		if (outputDirectory.exists()) {
			try {
				FileUtils.deleteDirectory(outputDirectory);
			} catch (IOException e) {
				throw new MojoExecutionException("Failed to delete outputdirectory", e);
			}
		}
		outputDirectory.mkdirs();
		
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

	protected abstract String getOutputDirectoryName();
	protected abstract void process(File file) throws IOException;
	protected abstract void init();
	protected abstract void done() throws IOException;

}

