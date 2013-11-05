# jfiglet

Java implementation of FIGfonts (http://www.figlet.org/) to create ascii art banners. My goals were:

- distributed as a maven dependency, first as a [github hosted](http://lalyos.github.io/mvn-repo/) later reach the central repo
- should be small


## Figlet

Figlet is a command-line tool which helps you to create ascii banners like this:

```
       ____________________    ____________
      / / ____/  _/ ____/ /   / ____/_  __/
 __  / / /_   / // / __/ /   / __/   / /   
/ /_/ / __/ _/ // /_/ / /___/ /___  / /    
\____/_/   /___/\____/_____/_____/ /_/     
```

## Usage
you can use it from command line or from java code

## java api

add the following maven dependency to your `pom.xml`

```
<dependency>
	<groupId>com.github.lalyos</groupId>
	<artifactId>jfiglet</artifactId>
	<version>0.0.1</version>
</dependency>

```

Then use the `convertOneLine()` static method to do the magic

```
import com.github.lalyos.jfiglet.FigletFont;

public class App {
  public static void main(String[] args) {
    String asciiArt = FigletFont.convertOneLine("hello");
    System.out.println(asciiArt);
  }
}

```

### command line

You can use it from the source directory with maven

```
mvn exec:java -Dexec.arguments="jfiglet rulez"
```

Or from commandline

```
curl -o jfiglet.jar http://not-yet-distributed
java -jar jfiglet.jar "text to convert"
```

## Related projects

First I wrapped http://artii.herokuapp.com/ which is based on the artii gem
I found 2 java implementations:
- [http://www.jave.de/eclipse/figlet/index.html] its not just FIGlet its too much, includes swing editor and so ...
- [http://www.rigaut.com/benoit/CERN/FigletJava/] it contains a single class implementation, so i choose this one.


## Todo

- *smush*-ing: *kerning* is already implemented  as default, smushing needs some coding.
- add more fonts: first i wanted to keep it small, but want to deliver a couple of fonts included in the jar, or maybe as a separate maven dependency
