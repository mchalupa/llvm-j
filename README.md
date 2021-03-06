# llvm-j

llvm-j is a Java library for parsing and modifying LLVM IR.

The goals of llvm-j are:
    1. Provide an easy to use LLVM IR parser that feels like a native Java
      library (compared to a collection of JNI bindings).
    2. Make it easy to upgrade to higher versions of LLVM without API changes.
      This is important since LLVM updates may change the LLVM core API -
      we want our users to be able to use llvm-j independent of such changes,
      and allow them to use new LLVM versions without a need to update their code.

To achieve these goals, llvm-j uses Java bindings for the original LLVM C parsing library with java native access ([JNA](https://github.com/java-native-access/jna)),
but provides proxy classes for the most common parsing tasks.
This way, the user never has to call the C bindings directly,
but can work with our own, Java-like API (e.g., we aim to throw Exceptions
for illegal states instead of returning values with a special error meaning).

The Java bindings for the LLVM C parsing library are automatically
generated with [JNAerator](https://github.com/nativelibs4java/JNAerator).
Through this, it is easy to update the bindings to new versions of LLVM,
and the user doesn't have to change any code since the llvm-j API stays
the same.

Currently, we use LLVM **3.9.1**.

## Download/Installation

To build this project yourself, you should first clone or download
this repo.

### Shared LLVM Library
llvm-j dynamically loads the shared libraries of LLVM, so they must be available
for your machine in the right version.
For convenience, we provide a mechanism to automatically download and extract
the shared libraries from an Ubuntu package (for 64 bit systems).
These libraries should work on most Linux distributions, not just Ubuntu!

To automatically download the library, execute on the command-line
`ant download-library -Dllvm.version=3.9.1`
when you're in the project's root directory.
The library will be automatically downloaded and put into directory
`lib/native`.
You can then either move the library to a system path for libraries (e.g., `/usr/local/lib`) or put them in your project.

If you use Windows, macOS, or the provided libraries do not work on your system,
you can 1) either use LLVM packages that include the shared libraries for your system (TODO: add links)
or 2) compile the shared libraries yourself (TODO: add link).

## Use

The proxy classes are provided in Java package [`org.sosy_lab.llvm_j`](ADDLINK) for easy use.
The original bindings are located in [`org.sosy_lab.llvm_j.binding`](ADDLINK).

If you didn't put the LLVM 3.9.1 shared library in one of your system directories
for library lookup,
you can tell llvm-j in which directory the library is in in two ways:
    1. Provide system property `jna.library.path` to Java, for example
       `java -Djna.library.path=additional/lookup/path -jar appParsingLlvm.jar`
       or
    2. Add the directory in the code
       with static method [`Module#addLibraryLookupPaths(List<Path>)`](ADDLINK),
       *before* calling `Module#parseIR(String)`.

To parse LLVM IR, call static method [`Module#parseIR(String)`](ADDLINK) with
the file to parse as argument.
Currently, llvm-j only understands LLVM IR in **bitcode format**
(usually file suffix *.bc).
Method `Module#parseIR(String)` will return a [`Module`](ADDLINK) object

## Development

We use `ant` to manage our build process.

### Creating LLVM bindings 

The bindings for the LLVM C parser are shipped with llvm-j,
but you can also create the bindings yourself.
To do so, you require a full installation of LLVM that includes the LLVM headers.
You can then create the bindings for that LLVM version.
To do so, execute from the root directory of llvm-j:
    `ant bindings -Dllvm.version=3.9.1 -Dllvm.home=/path/to/llvm-3.9.1`
where you replace `3.9.1` with the corresponding LLVM version
and `/path/to/llvm-3.9.1` with the correct path to your LLVM installation
of that version.
That path should contain a sub-folder `include/llvm-c`.

Notice that new bindings may require modifications to the proxy classes,
since the LLVM API may change over time.

### Creating JavaDoc

To create the JavaDoc, run `ant javadoc` in the project's root directory.

### Tools for Code Quality

We provide some checks that may help you in writing good and correct code.
You can run: 
  * [CheckStyle](ADDLINK) through `ant checkstyle`
  * [Eclipse Compiler](ADDLINK) with Eclipse-specific warnings
      through `ant build-project-ecj`
  * [Google Code Formatter](ADDLINK) through `ant format-source`
  * [SpotBugs](ADDLINK) through `ant spotbugs`

You can run `ant all-checks` to run CheckStyle, the Eclipse compiler, the JavaDoc
task (which performs linting for JavaDoc) and SpotBugs.

These tools are, of course, not enough to ensure code quality, but only some
helpers.


Currently, the proxy classes aim at parsing LLVM IR bitcode, not modifying it.
If you miss any functionality, we're always happy about pull-requests
or new issues on GitHub!
