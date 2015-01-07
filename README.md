# jfiglet

Java implementation of FIGfonts to create ascii art banners. My goals were:

- distributed as a maven dependency
- should be small

I started out from Benoît Rigaut, Juillet work found at [www.rigaut.com](http://www.rigaut.com/benoit/CERN/FigletJava/)

## Figlet

[figlet](http://www.figlet.org/) is a command-line tool which helps you to create ascii banners like this:

```
     _  _____  ___   ____  _      _____  _____
    | ||  ___||_ _| / ___|| |    | ____||_   _|
 _  | || |_    | | | |  _ | |    |  _|    | |  
| |_| ||  _|   | | | |_| || |___ | |___   | |  
 \___/ |_|    |___| \____||_____||_____|  |_|  
```

Figlet has a *specification* which is included into the repo for easy access [here](https://github.com/lalyos/jfiglet/blob/master/figfont.txt)

## Usage
you can use it from command line or from java code

### Maven dependency

add the following maven dependency to your `pom.xml`

```
<dependency>
	<groupId>com.github.lalyos</groupId>
	<artifactId>jfiglet</artifactId>
	<version>0.0.7</version>
</dependency>
```

## Usage - code

Then one could use number of `convertOneLine(...)` static methods to do the magic

```
import com.github.lalyos.jfiglet.FigletFont;
import java.io.File;

public class App {
  public static void main(String[] args) {
    // using default font standard.flf, obtained from maven artifact
    String asciiArt1 = FigletFont.convertOneLine("hello");
    System.out.println(asciiArt1);
    
    // using font font2.flf, located somewhere in classpath under path /flf/font2.flf
    String asciiArt2 = FigletFont.convertOneLine(FigletFont.class.getResourceAsStream("/flf/font2.flf"), "hello");
    System.out.println(asciiArt2);
    
    asciiArt2 = FigletFont.convertOneLine("classpath:/flf/font2.flf", "hello");     
    System.out.println(asciiArt2);                
    
    // using font font3.flf, located in file system under path /opt/font3.flf
    String asciiArt3 = FigletFont.convertOneLine(new File("/opt/font3.flf"), "hello");     
    System.out.println(asciiArt3);

    asciiArt3 = FigletFont.convertOneLine("/opt/font3.flf", "hello");     
    System.out.println(asciiArt3);

    // using font font4.flf, from www 
    String asciiArt4 = FigletFont.convertOneLine("http://myhost.com/font4.flf", "hello");     
    System.out.println(asciiArt4);                
  }
}
```

## Usage - command line

You can use the jar from the central repo, or use the latest development version from sourcecode;
```
Usage: java -jar jfiglet.jar [-f FLF] MESSAGE
Prints MESSAGE to stdout as ASCII art using Figlet font
Example: java -jar jfiglet.jar -f "/opt/myfont.flf" "Hello World"


Figlet font:
  -f  FLF is font file location within file system, java classpath or www.
      When FLF starts with `http://'|`https://' file will be fetched from WWW,
      if FLF starts from `classpath:' then it will be looked for in JRE classpath,
      otherwise FLF if path to file in file system
```

### from source

```
git clone git@github.com:lalyos/jfiglet.git
cd jfiglet
mvn exec:java -Dexec.arguments="jfiglet rulez"
```
### from maven central

```
curl -o jfiglet.jar http://central.maven.org/maven2/com/github/lalyos/jfiglet/0.0.7/jfiglet-0.0.7.jar
java -jar jfiglet.jar "text to convert"
```

## Related projects

First I wrapped http://artii.herokuapp.com/ which is based on the artii gem
I found 2 java implementations:
- http://www.jave.de/eclipse/figlet/index.html its not just FIGlet its  much more, includes swing editor and so ...
- http://www.rigaut.com/benoit/CERN/FigletJava/ it contains a single class implementation, so i choose this one.


## Todo

- *smush*-ing: *kerning* is already implemented  as default, smushing needs some coding.
- add more fonts: first i wanted to keep it small, but want to deliver a couple of fonts included in the jar, or maybe as a separate maven dependency
