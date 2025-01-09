import jep.JepConfig;
import jep.SharedInterpreter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileNotFoundException;

public class PythonInterpreter {
    private SharedInterpreter jep;
    private JepConfig config;
    
    // Creates a new PythonInterpreter. ’Paths’ are the absolute file paths where the python
    // modules invoked by the interpreter are stored. 
    public PythonInterpreter(String[] paths) throws FileNotFoundException {
        this.config = new JepConfig();
        if (paths!=null) { 
            for (int i =0; i<paths.length;i++) {
                if(!Files.isDirectory(Path.of(paths[i]))) {
                    throw new FileNotFoundException("Path " + paths[i] + " does not exist");
                }
                this.config.addIncludePaths(paths[i]);
            }
        }
        SharedInterpreter.setConfig(config);
        jep = new SharedInterpreter();
    }
    // Returns the JEP python interpreter. Should only be called when trying to directly
    // execute python code (generally not needed for students)
    public SharedInterpreter getInterpreter(){
        return jep;
    }

    // closes the interpreter
    public void close() {
        jep.close();
    }
    public static void main(String[] args)  throws FileNotFoundException{
        PythonInterpreter test = new PythonInterpreter(args);

    }


}