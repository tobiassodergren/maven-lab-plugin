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

import org.junit.Test;

public class ParseVersionTest {
	@Test
	public void basic() {
		assertEquals(123, new LabCreator().parseVersion("@BEGIN_VERSION 123"));
	}
	@Test
	public void beginWithComment() {
		assertEquals(1, new LabCreator().parseVersion("  @BEGIN_VERSION 1 2 3"));
	}
	@Test
	public void tabBeforeVersion() {
		assertEquals(1, new LabCreator().parseVersion("  @END_VERSION	1	2 3"));
	}

	@Test
	public void severalSpacesBeforeVersion() {
		assertEquals(9, new LabCreator().parseVersion(" 	 //@BEGIN_VERSION     9 hello"));
	}

	@Test
	public void constant() {
		assertEquals(2, new LabCreator("a", "b", "c").parseVersion("//@BEGIN_VERSION  c"));
	}

	@Test
	public void constantWithComment() {
		assertEquals(1, new LabCreator("a", "b", "c").parseVersion("//@BEGIN_VERSION 	 b hello 23 "));
	}

	@Test(expected=IllegalArgumentException.class)
	public void constantNotFound() {
		new LabCreator("a", "b", "c").parseVersion("//@BEGIN_VERSION  d");
	}

	@Test(expected=IllegalArgumentException.class)
	public void noVersion() {
		new LabCreator("a", "b", "c").parseVersion("//@BEND_VERSION");
	}
}
