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
package com.jayway.maven.plugins.lab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.jayway.maven.plugins.lab.FileIndex;

public class FileIndexTest {

	@Test
	public void savingAndLoadingFileIndexShouldWork() throws Exception {
		FileIndex fileIndex = new FileIndex();
		fileIndex.addFile("qwe", 0);
		fileIndex.addFile("asd", 1);
		fileIndex.addFile("zxc", 2);
		File file = File.createTempFile("FileIndex", ".txt");
		fileIndex.save(file);
		FileIndex loadedIndex = FileIndex.load(file);
		
		assertEquals(Collections.singleton("qwe"), toSet(loadedIndex.getFiles(0)));
		assertTrue(toSet(loadedIndex.getFiles(1)).contains("qwe"));
		assertTrue(toSet(loadedIndex.getFiles(1)).contains("asd"));
		assertFalse(toSet(loadedIndex.getFiles(1)).contains("zxc"));
		assertTrue(toSet(loadedIndex.getFiles(2)).contains("qwe"));
		assertTrue(toSet(loadedIndex.getFiles(2)).contains("asd"));
		assertTrue(toSet(loadedIndex.getFiles(2)).contains("zxc"));
	}

	private static <T> Set<T> toSet(Iterable<T> i) {
		Set<T> set = new HashSet<T>();
		for (T obj : i) {
			set.add(obj);
		}
		return set;
	}
}
