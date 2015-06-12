# Sally

digitisation control utility (PDF/A-1b) / utilitaire de contrôle de numérisation (PDF/A-1b)

This program is using Java 1.7 and should be compatible with any upper version of Java.

## Compilation

### Prerequisite

In order to compile Sally, you will need:

-Maven

-Java Development Kit 1.7 or later

-A running internet connexion

### Compile process

To compile sally:
-Open a shell in the folder where is the file "pom.xml"

-Type "mvn package"

-Wait for the process to end

-Copy the file "Sally.jar" in the folder "target"

-Past it in the forlder "cdsp"

## Installation

### Prerequisite

java 1.7 or later

### On Windows

To install sally(it is not mandatory):

-copy the folder "cdsp" in "C:\Program Files (x86)\"

-go to computer

-right click -> properties

-left column -> advanced system parameters

-environment variable

-find "path" in the "system variable" cell and click on it

-modify (or edit)

-at the end on the value add ";C:\Program Files (x86)\cdsp\sally\exec" (in the case you have installed the program with default value, in the other case adapt this path)

-click on ok

sally is now installed.

### On Unix

Simply paste the "cdsp" folder somewhere you can read it.

## Usage

### On Windows

If you have installed sally on Windows:

-go to the folder you want to scan

-put version of your "bordereau.csv" at the root of this folder

-type "cmd" in the address bar to open a shell

-type "sally" in the prompt

### On Unix or not installed version on windows

If you are not on Windows or if you have chosen to not install sally you still can use it by typing this command to the shell :

java -jar "path to sally" "path to the folder to scan" "path to the bordereau.csv"


## Bordereau.csv

The file bordereau.csv must follow the syntax :

column  0: identifier

column  10: title

the separator between the columns must be , (comma)