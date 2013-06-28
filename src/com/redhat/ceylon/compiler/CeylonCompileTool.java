/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.compiler.java.launcher.Main;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState.CeylonState;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.sun.tools.javac.main.JavacOption;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.main.RecognizedOptions;

@Summary("Compiles Ceylon and Java source code and directly produces module " +
		"and source archives in a module repository.")
@Description("The default module repositories are `modules` and " +
		"http://modules.ceylon-lang.org, and the default source directory is `source`. " +
		"The default output module repository is `modules`." +
		"\n\n" +
		"The `<moduleOrFile>` arguments can be either module names (without versions) " +
		"or file paths specifying the Ceylon or Java source code to compile." +
		"\n\n" +
		"When `<moduleOrFile>` specifies a module the compiler searches for compilation units " +
		"belonging to the specified " +
		"modules in the specified source directories. " +
		"For each specified module, the compiler generates a module archive, " +
		"source archive, and their checksum files in the specified output module " +
		"repository." +
		"\n\n"+
		"When `<moduleOrFile>` specifies a source file just that file is compiled and " +
		"the module archive is created or updated with the .class files produced." +
		"The source file is " +
		"treated as relative to the current directory (so any `--source` " +
		"options are ignored)."+
		"\n\n"+
        "All program elements imported by a compilation unit must belong to the " +
        "same module as the compilation unit, or must belong to a module that " +
        "is explicitly imported in the module descriptor." +
        "\n\n" +
        "The compiler searches for dependencies in the following locations:" +
        "\n\n"+
        "* module archives in the specified repositories,\n"+
        "* source archives in the specified repositories, and\n"+
        "* module directories in the specified source directories.\n")
@RemainingSections(
"## Specifying `javac` options\n" +
"\n"+
"It is possible to pass options to the `javac` compiler by prefixing them " +
"with `--javac=` and separating the javac option from its argument (if any) " +
"using another `=`. For example:\n" +
"\n" +
"* The option `--javac=-target=1.6` is equivalent to `javac`'s `-target 1.6` and,\n" +
"* the option `--javac=-g:none` is equivalent to `javac`'s `-g:none`\n" +
"\n" +
"Execute `ceylon compile --javac=-help` for a list of the standard javac " +
"options, and ceylon compile --javac=-X for a list of the non-standard javac " +
"options.\n" +
"\n" +
"**Important note**: There is no guarantee that any particular `javac` " +
"option or combination of options will work, or continue to work in " +
"future releases.")
public class CeylonCompileTool implements Tool{

    private List<File> source = Collections.singletonList(new File("source"));
    private String out;
    private List<URI> repo = Collections.emptyList();
    private String systemRepo;
    private List<String> module = Collections.emptyList();
    private boolean d;
    private List<String> javac = Collections.emptyList();
    private String user;
    private String pass;
    private String encoding;
    private boolean verbose = false;
    private String verboseFlags = "";
    private boolean offline;

    private List<String> arguments;

    public CeylonCompileTool() {
    }
    
    @OptionArgument(longName="src", argumentName="dirs")
    @Description("Path to source files. " +
            "Can be specified multiple times; you can also specify several " +
            "paths separated by your operating system's `PATH` separator." +
            " (default: `./source`)")
    public void setSource(List<File> source) {
        this.source = source;
    }
    
    @OptionArgument(longName="rep", argumentName="url")
    @Description("Specifies a module repository containing dependencies. Can be specified multiple times. " +
    		"(default: `modules`, `~/.ceylon/repo`, http://modules.ceylon-lang.org)")
    public void setRepository(List<URI> repo) {
        this.repo = repo;
    }
    
    @OptionArgument(longName="sysrep", argumentName="url")
    @Description("Specifies the system repository containing essential modules. " +
            "(default: `$CEYLON_HOME/repo`)")
    public void setSystemRepository(String systemRepo) {
        this.systemRepo = systemRepo;
    }
    
    @Option(longName="d")
    @Description("Disables the default module repositories and source directory.")
    public void setDisableDefaultRepos(boolean d) {
        this.d = d;
    }
    
    @OptionArgument(argumentName="url")
    @Description("Specifies the output module repository (which must be publishable). " +
    		"(default: `./modules`)")
    public void setOut(String out) {
        this.out = out;
    }
    
    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository" +
    		"(no default).")
    public void setUser(String user) {
        this.user = user;
    }
    
    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository" +
    		"(no default).")
    public void setPass(String pass) {
        this.pass = pass;
    }

    @OptionArgument(argumentName="encoding")
    @Description("Sets the encoding used for reading source files" +
            "(default: platform-specific).")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Argument(argumentName="moduleOrFile", multiplicity="*")
    // multiplicity=* because of --javac=-help and --javac=-Xhelp are allowed 
    // on their own
    public void setModule(List<String> moduleOrFile) {
        this.module = moduleOrFile;
    }
    
    @Option
    @OptionArgument(argumentName="flags")
    @Description("Produce verbose output. " +
    		"If no `flags` are given then be verbose about everything, " +
    		"otherwise just be vebose about the flags which are present. " +
    		"Allowed flags include: `loader`, `ast`, `code`, `cmr`, `benchmark`.")
    public void setVerbose(String verboseFlags) {
        this.verbose = true;
        this.verboseFlags = verboseFlags;
    }
    
    @OptionArgument(argumentName="option")
    @Description("Passes an option to the underlying java compiler.")
    public void setJavac(List<String> javac) {
        this.javac = javac;
    }

    @Option(longName="offline")
    @Description("Enables offline mode that will prevent the module loader from connecting to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    
    @PostConstruct
    public void init() {
        if (module.isEmpty() &&
                !javac.contains("-help") &&
                !javac.contains("-X") &&
                !javac.contains("-version")) {
            throw new IllegalStateException("Argument moduleOrFile should appear at least 1 time(s)");
        }
        arguments = new ArrayList<>();
        for (File source : this.source) {
            arguments.add("-src");
            arguments.add(source.getPath());
        }
        
        if (d) {
            arguments.add("-d");
        }
        
        if (offline) {
            arguments.add("-offline");
        }
        
        if (verbose) {
            if (verboseFlags == null || verboseFlags.isEmpty()) {
                arguments.add("-verbose");
            } else {
                arguments.add("-verbose:" + verboseFlags);
            }
        }
        
        if (out != null) {
            arguments.add("-out");
            arguments.add(out);
        }
        
        if (user != null) {
            arguments.add("-user");
            arguments.add(user);
        }
        if (pass != null) {
            arguments.add("-pass");
            arguments.add(pass);
        }

        String fileEncoding = encoding;
        if (fileEncoding == null) {
            fileEncoding = CeylonConfig.get(DefaultToolOptions.DEFAULTS_ENCODING);
        }
        if (fileEncoding != null) {
            arguments.add("-encoding");
            arguments.add(fileEncoding);
        }

        if (systemRepo != null) {
            arguments.add("-sysrep");
            arguments.add(systemRepo);
        }
        
        for (URI uri : this.repo) {
            arguments.add("-rep");
            arguments.add(uri.toString());
        }
        
        addJavacArguments(arguments);
        
        JavacOption sourceFileOpt = getJavacOpt(OptionName.SOURCEFILE);
        for (String moduleSpec : this.module) {
            if (sourceFileOpt != null
                    && !sourceFileOpt.matches(moduleSpec)) {
                throw new IllegalArgumentException("Not a valid module name or source file: " + moduleSpec);
            }
            arguments.add(moduleSpec);
        }
        
        if (verbose) {
            System.out.println(arguments);
            System.out.flush();
        }
    }
    
    private JavacOption getJavacOpt(OptionName optionName) {
        for (com.sun.tools.javac.main.JavacOption o : RecognizedOptions.getJavaCompilerOptions(null)) {
            if (o.getName() == optionName) {
                return o;
            }
        }
        return null;
    }

    /**
     * Run the compilation
     * @throws CompilerErrorException If the source code had errors
     * @throws SystemErrorException If there was a system error
     * @throws CompilerBugException If a bug in the compiler was detected.
     */
    @Override
    public void run() {
        
        Main compiler = new Main("ceylon compile");
        int result = compiler.compile(arguments.toArray(new String[arguments.size()]));
        handleExitCode(result, compiler.exitState);
    }

    private void handleExitCode(
            int javacExitCode,
            Main.ExitState exitState) {
        if (exitState == null) {
            throw new IllegalStateException("Missing ExitState, " + javacExitCode);
        }
        CeylonState ceylonState = exitState.ceylonState;
        switch (ceylonState) {
        case OK:
            break;
        case ERROR:
            throw new CompilerErrorException(exitState.errorCount);
        case SYS:
            throw new SystemErrorException(exitState.abortingException);
        case BUG:
            throw new CompilerBugException(exitState);
        default:
            throw new IllegalStateException("Unexpected CeylonState " + ceylonState);
        }
    }
/*
    private void handleExitCode(
            Main compiler, int javacExitCode) {
        switch (javacExitCode) {
        case Main.EXIT_OK:
            break;
        case Main.EXIT_ERROR:
            if (compiler.ceylonBackendErrors) {
                throw new CompilerBugException(javacExitCode, compiler);
            }
            throw new CompilerErrorException(compiler.errorCount);
        case Main.EXIT_SYSERR:
            throw new SystemErrorException(compiler.abortingException);
        case Main.EXIT_ABNORMAL:
            if (!compiler.bug && !compiler.ceylonBackendErrors) {
                // pretend it's an ordinary compiler error, to work around javacs sloppy error handling 
                // (see where compiler.compile() returns EXIT_ABNORMAL)
                throw new CompilerErrorException(compiler.errorCount);
            }
            // else fall through
        default:
            throw new CompilerBugException(javacExitCode, compiler);
        }
    }
*/
    private void addJavacArguments(List<String> arguments) {
        for (String argument : javac) {       
            String value = null;
            int index = argument.indexOf('=');
            if (index != -1) {
                value = index < argument.length() ? argument.substring(index+1) : "";
                argument = argument.substring(0, index);
            }
            arguments.add(argument);
            if (value != null) {
                arguments.add(value);
            }
        }
    
    }
}
