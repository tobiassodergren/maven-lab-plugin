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
package se.jayway.maven.lab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.junit.Test;

public class LabCreatorTest {
	public static String newline = System.getProperty("line.separator");

	@Test
	public void correctContentsOfVersion() throws IOException {
		String name = "VersionTest.txt";
		VersionedContents versionedContents = new LabCreator().labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
		checkCorrectContetsOfVersion(versionedContents);
	}

	@Test
	public void correctContentsOfVersionWithConstans() throws IOException {
		String name = "VersionTestWithConstants.txt";
		VersionedContents versionedContents = new LabCreator("noll", "ETT", "TVÅ", "tre").labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
		checkCorrectContetsOfVersion(versionedContents);
	}

	private void checkCorrectContetsOfVersion(VersionedContents versionedContents) {
		String contents1 = getContents(versionedContents, 1);
		assertEquals("VERSION1" + newline, contents1);
		String contents2 = getContents(versionedContents, 2);
		assertEquals("VERSION1" + newline + "VERSION2" + newline, contents2);
		String contents3 = getContents(versionedContents, 3);
		assertEquals("---" + newline + "VERSION1" + newline + "VERSION2" + newline + "---" + newline, contents3);
	}

	private String getContents(VersionedContents versionedContents, int step) {
		StringWriter writer = new StringWriter();
		versionedContents.writeVersion(writer, step);
		return writer.getBuffer().toString();
	}

	@Test
	public void nonJavaFilesShouldWork() throws IOException {
		String name = "NonJavaFile.properties";
		VersionedContents versionedContents = new LabCreator().labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
		assertEquals(new Version(2), versionedContents.getMaxVersion());
		assertEquals(new Version(0), versionedContents.getLeastVersion());
	}
	
	@Test
	public void normalLab() throws IOException {
		String name = "Dummy.java.txt";
		VersionedContents versionedContents = new LabCreator().labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
		assertEquals(new Version(3), versionedContents.getMaxVersion());
		assertEquals(new Version(1), versionedContents.getLeastVersion());
	}
	@Test
	public void incorrectlyNestedTagsShouldGenerateException() throws IOException {
		String name = "Evil.java.txt";
		try {
			new LabCreator().labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
			fail("Expected exception!");
		} catch (IllegalArgumentException e) {
			// ok!
		}
	}
	@Test
	public void firstStepsMightBeEmpty() throws IOException {
		String name = "StartingEmpty.java.txt";
		VersionedContents versionedContents = new LabCreator().labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
		assertEquals(new Version(3), versionedContents.getLeastVersion());
		assertFalse(versionedContents.hasContents(2));
		assertTrue(versionedContents.hasContents(3));
		assertEquals(new Version(7), versionedContents.getMaxVersion());
	}

	@Test
	public void correctContentsOfVersionThisOnly() throws IOException {
		String name = "VersionTestThisOnly.txt";
		VersionedContents versionedContents = new LabCreator().labify(new InputStreamReader(LabCreatorTest.class.getClassLoader().getResourceAsStream(name)), name);
		checkCorrectContetsOfVersionThisOnly(versionedContents);
	}

	private void checkCorrectContetsOfVersionThisOnly(VersionedContents versionedContents) {
		String contents1 = getContents(versionedContents, 1);
		assertEquals("VERSION1" + newline, contents1);
		String contents2 = getContents(versionedContents, 2);
		assertEquals("VERSION2" + newline, contents2);
		String contents3 = getContents(versionedContents, 3);
		assertEquals("---" + newline + "VERSION2" + newline + "---" + newline, contents3);
	}
}
