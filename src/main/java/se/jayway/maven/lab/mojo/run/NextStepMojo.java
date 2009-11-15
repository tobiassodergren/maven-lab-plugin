package se.jayway.maven.lab.mojo.run;

import se.jayway.maven.lab.LabRunner;


/**
 * Change to next step
 *
 * @goal next
 * @author Jan Kronquist
 */
public class NextStepMojo extends AbstractRunLabMojo {

	public int execute(LabRunner lab) {
		return lab.nextStep();
	}

}
