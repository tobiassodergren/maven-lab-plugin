This is a Maven plugin used to create stepwise labs which a developer can use 
to learn new technologies, frameworks and tools.

For example a test driven lab can easily be created by letting each step of lab
add new test cases, either new classes or simply new methods. The student can 
then modify the main code until the test passes and then move to the next step 
of the lab. 

Commands:
* "mvn lab:init" initializes the lab at step 0
* "mvn lab:next" moves to the next step of the lab
* "mvn lab:currentStep" shows the current step
* "mvn lab:reset" restores all files that were affected by the lab
* "mvn lab:setStep -DlabStep=nnn" to change to a particular step of the lab

Tags to add code in steps:
* "@BEGIN_VERSION <stepNo>" start a block of code that will start to be included in the step and onwards
* "@END_VERSION <stepNo>" end the block of code

Tags to add code that will only appear in a particular step:
* "@BEGIN_VERSION_ONLY <stepNo>" start a block of code that belongs to a step 
* "@END_VERSION_ONLY <stepNo>" end a block of code that belongs to a step

Tags to add code up to a specified version:
* "@BEGIN_UP_TO_VERSION" start a block of code that will be included up to (but not including) a version
* "@END_UP_TO_VERSION" end the block of code

Notice that the entire line containing a tag will be removed from the output!
The purpose of this is to support tags regardless of the file format, eg java,
properties or xml.

The stepNo can either be a number or a constant. When using constants they
must be specified in a file called "labStepConstants.properties" placed in
the root of the project. The line number of the constant is used as stepNo. 

Process:
* Create lab code
* Add version TAGS (@BEGIN_VERSION, @END_VERSION, @BEGIN_VERSION_ONLY, @END_VERSION_ONLY,
                    @BEGIN_UP_TO_VERSION, @END_UP_TO_VERSION)
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

Example:
  How do we achieve a lab of two steps?
	Step 1:
		function first() { }
		first();
	Step 2:
		function first() { }
		function second() { }
		second();
				
  Solution:
		// @BEGIN_VERSION First
		function first() { }
		// @END_VERSION First
		// @BEGIN_VERSION Second
		function second() { }
		// @END_VERSION Second
		// @BEGIN_VERSION_ONLY First
		first();
		// @END_VERSION_ONLY First
		// @BEGIN_VERSION_ONLY Second
		second();
		/* @END_VERSION_ONLY Second */

Notice that it doesn't matter what type of comment style we use and that the
entire line containing the tag is removed.
