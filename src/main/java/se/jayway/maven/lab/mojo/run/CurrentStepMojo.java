package se.jayway.maven.lab.mojo.run;

import se.jayway.maven.lab.LabRunner;


/**
 * Get the current step no
 *
 * @goal currentStep
 * @author Jan Kronquist
 */
public class CurrentStepMojo extends AbstractRunLabMojo {

	public int execute(LabRunner lab) {
		return lab.getCurrentStep();
	}

}
