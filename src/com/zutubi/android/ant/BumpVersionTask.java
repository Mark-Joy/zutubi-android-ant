
package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

/**
 * Ant task to increment the version in an AndroidManifest.xml file.
 */
public class BumpVersionTask extends AbstractManifestUpdateTask {
    private boolean bumpname = false;

    /**
     * Sets whether the last component of the android:versionName value is also
     * incremented.
     *
     * @param bumpname if true, also increment the versionName, if false leave
     *            the versionName unchanged
     */
    public void setBumpname(final boolean bumpname) {
        this.bumpname = bumpname;
    }

    @Override
    protected void updateManifest(final Manifest manifest) {
        updateCode(manifest);

        if (bumpname) {
            updateName(manifest);
        }
    }

    private void updateCode(final Manifest manifest) {
        final String codeString = manifest.getVersionCode();
        try {
            final int code = Integer.parseInt(codeString);
            manifest.setVersionCode(Integer.toString(code + 1));
        } catch (final NumberFormatException e) {
            throw new BuildException("Invalid version code '" + codeString
                    + "': expected an integer");
        }
    };

    private void updateName(final Manifest manifest) {
        final String versionName = manifest.getVersionName();
        if (!Util.stringSet(versionName)) {
            throw new BuildException("Invalid version name '" + versionName + "': name is empty");
        }

        try {
            final Version version = new Version(versionName);
            manifest.setVersionName(version.bump().toString());
        } catch (final IllegalArgumentException e) {
            throw new BuildException(e.getMessage(), e);
        }
    }
}
