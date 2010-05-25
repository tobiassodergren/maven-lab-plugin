mvn com.jayway.maven.plugins.lab:maven-lab-plugin:1.0-SNAPSHOT:init

Background:
* Wrap code with @BEGIN_VERSION, @END_VERSION to have it shown in that step and all subsequent steps.
* Wrap code with @BEGIN_VERSION_ONLY, @END_VERSION_ONLY to have it shown in that step only.
* Example:
  How do we achieve this?
	Step 1:
		function first() { }
		first();
	Step 2:
		function first() { }
		function second() { }
		second();
  By writing this:
		@BEGIN_VERSION First
		function first() { }
		@END_VERSION First
		@BEGIN_VERSION Second
		function second() { }
		@END_VERSION Second
		@BEGIN_VERSION_ONLY First
		first();
		@END_VERSION_ONLY First
		@BEGIN_VERSION_ONLY Second
		second();
		@END_VERSION_ONLY Second

Process:
* Create lab code
* Add version TAGS (@BEGIN_VERSION, @END_VERSION, @BEGIN_VERSION_ONLY, @END_VERSION_ONLY)
* Add maven-lab-plugin to the pom.xml
* Start the lab "mvn lab:init". Lab is initialized at step 0
* Run "mvn lab:next" to move to next step of the lab
* Perform next step of the lab, repeat previous until done

This should go into your pom.xml:

<build>
        <plugins>
                <plugin>
                        <groupId>com.jayway.maven.plugins.lab</groupId>
                        <artifactId>maven-lab-plugin</artifactId>
                </plugin>
        </plugins>
</build>

---

Exceptions:
* Run "mvn lab:setStep -DlabStep=nnn" to change to a particular step of the lab
* Run "mvn lab:currentStep" to display the current step of the lab
