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

## Manual release on sonatype web

- Navigate to [Staging Repositories](https://oss.sonatype.org/#stagingRepositories)
- Select your repo (mostly last in the list)
- Close it
- Release it

## Check

If it succeeds you can check the [sonatype release repo](https://oss.sonatype.org/content/repositories/releases/com/github/lalyos/jfiglet/)
