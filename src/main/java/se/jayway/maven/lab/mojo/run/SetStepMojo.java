package se.jayway.maven.lab.mojo.run;

import se.jayway.maven.lab.LabRunner;


/**
 * Change version to versionNo
 *
 * @goal setStep
 * @author Jan Kronquist
 */
public class SetStepMojo extends AbstractRunLabMojo {

	/**
     * The version number to change to.
     * @parameter expression="${labStep}"
     * @required 
     */
    private int labStep;

	public int execute(LabRunner lab) {
		return lab.changeTo(labStep);
	}

}
