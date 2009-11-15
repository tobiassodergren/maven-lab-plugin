mvn se.jayway.maven:maven-lab-plugin:1.0-SNAPSHOT:init


Process:
* Create lab code
* Add version TAGS (@BEGIN_VERSION, @END_VERSION)
* Add maven-lab-plugin to the pom.xml
* Start the lab "mvn lab:init". Lab is initialized at step 0
* Run "mvn lab:next" to move to next step of the lab
* Perform next step of the lab, repeat previous until done

This should go into your pom.xml:

<build>
        <plugins>
                <plugin>
                        <groupId>se.jayway.maven</groupId>
                        <artifactId>maven-lab-plugin</artifactId>
                </plugin>
        </plugins>
</build>

---

Exceptions:
* Run "mvn lab:setStep -DlabStep=nnn" to change to a particular step of the lab
* Run "mvn lab:currentStep" to display the current step of the lab

----------

NOT IMPLEMENTED:

Nice to have:
* Tests must run in order to be allowed to move to next step
