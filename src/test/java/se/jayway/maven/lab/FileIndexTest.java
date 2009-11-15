package se.jayway.maven.lab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

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
