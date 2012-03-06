package com.jayway.maven.plugins.lab;

/**
 * Contains not only the version number, but also a flag that indicates whether
 * or not the {@link VersionedContents} should exist for exactly this version
 * only, or for all versions greater than or equal to this version.
 *
 * @author Ulrik Sandberg
 */
public class Version {

    public enum TriggerType {
        /**
         * Content should only be included for a one version.
         */
        CONTENT_IN_THIS_VERSION_ONLY,
        /**
         * Content should be included from (and including) a version.
         */
        FROM_VERSION,
        /**
         * Content should be included from the first version, up to (not including) a version.
         */
        UP_TO_VERSION
    }

    public static final Version ZERO = new Version(0);

    public static final Version VERY_BIG = new Version(Integer.MAX_VALUE);

    private final int versionNumber;

    private final TriggerType triggerType;

    public Version(int version) {
        this(version, TriggerType.FROM_VERSION);
    }

    public Version(int versionNumber, TriggerType triggerType) {
        this.versionNumber = versionNumber;
        this.triggerType = triggerType;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public TriggerType getTriggerType() {
        return triggerType;
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

    public boolean greaterThan(int versionNumber) {
        return this.getVersionNumber() > versionNumber;
    }

    public boolean containsStuffFor(int versionNumber) {
        if (this.getTriggerType().equals(TriggerType.CONTENT_IN_THIS_VERSION_ONLY)) {
            if (this.sameAs(versionNumber)) {
                return true;
            }
        } else if (this.getTriggerType().equals(TriggerType.FROM_VERSION) && this.sameOrLessThan(versionNumber)) {
            return true;
        } else if (this.getTriggerType().equals(TriggerType.UP_TO_VERSION) && this.greaterThan(versionNumber)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Version [contentInThisVersionOnly=" + triggerType + ", versionNumber=" + versionNumber
                + "]";
    }

    @Override
    public int hashCode() {
        int result = versionNumber;
        result = 31 * result + (triggerType != null ? triggerType.hashCode() : 0);
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
        if (triggerType != other.triggerType)
            return false;
        if (versionNumber != other.versionNumber)
            return false;
        return true;
    }
}
