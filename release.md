READ more on: http://central.sonatype.org/pages/apache-maven.html

## In the workdir

Use the maven release plugin to:

- change the POM version number from *SNAPSHOT* to a release
- tag it in the version control
- deploy to sonatype
- change to the next *SNAPSHOT* version

```
mvn release:prepare
mvn release:perform
```

## Release on sonatype web

The sonatype plugin closes, and releases automatically, but for reference,
the manual process was:

- Navigate to [Staging Repositories](https://oss.sonatype.org/#stagingRepositories)
- Select your repo (mostly last in the list)
- Close it
- Release it

## Bintray release

```
curl -Lo /tmp/bintray-functions j.mp/bintray-functions && . /tmp/bintray-functions
cd target
VERSION=$(ls -rt1 jfiglet-*.jar|tail -1|sed "s/.*jfiglet-\([0-9\.]*\).*/\1/")

bint-upload-with-version lalyos maven jfiglet $VERSION jfiglet-$VERSION-javadoc.jar
bint-upload lalyos maven jfiglet $VERSION jfiglet-$VERSION-sources.jar
bint-upload lalyos maven jfiglet $VERSION jfiglet-$VERSION.pom
bint-upload lalyos maven jfiglet $VERSION jfiglet-$VERSION.jar
```

## Check

If it succeeds you can check the [sonatype release repo](https://oss.sonatype.org/content/repositories/releases/com/github/lalyos/jfiglet/)
