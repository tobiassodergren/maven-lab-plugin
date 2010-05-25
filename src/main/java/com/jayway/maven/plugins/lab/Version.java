package com.jayway.maven.plugins.lab;

/**
 * Contains not only the version number, but also a flag that indicates whether
 * or not the {@link VersionedContents} should exist for exactly this version
 * only, or for all versions greater than or equal to this version.
 * 
 * @author Ulrik Sandberg
 */
public class Version {
	public static final Version ZERO = new Version(0);

	public static final Version VERY_BIG = new Version(Integer.MAX_VALUE);

	private final int versionNumber;

	private final boolean contentInThisVersionOnly;

	public Version(int version) {
		this(version, false);
	}

	public Version(int versionNumber, boolean contentOnlyInThisVersion) {
		this.versionNumber = versionNumber;
		this.contentInThisVersionOnly = contentOnlyInThisVersion;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public boolean isContentInThisVersionOnly() {
		return contentInThisVersionOnly;
	}

	public boolean greaterThan(Version that) {
		return this.versionNumber > that.versionNumber;
	}

	public boolean lessThan(Version that) {
		return this.versionNumber < that.versionNumber;
	}

	public boolean sameAs(int versionNumber) {
		return this.getVersionNumber() == versionNumber;
	}

	public boolean sameOrLessThan(int versionNumber) {
		return this.getVersionNumber() <= versionNumber;
	}

	public boolean containsStuffFor(int versionNumber) {
		if (this.isContentInThisVersionOnly()) {
			if (this.sameAs(versionNumber)) {
				return true;
			}
		}
		else if (this.sameOrLessThan(versionNumber)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Version [contentInThisVersionOnly=" + contentInThisVersionOnly + ", versionNumber=" + versionNumber
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contentInThisVersionOnly ? 1231 : 1237);
		result = prime * result + versionNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (contentInThisVersionOnly != other.contentInThisVersionOnly)
			return false;
		if (versionNumber != other.versionNumber)
			return false;
		return true;
	}
}
