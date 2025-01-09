import java.io.FileNotFoundException;
public class PiCamera {
    private PythonInterpreter interpreter;
    private Object camera;
   
   
    // Returns a new PiCamera object that takes photos of resolution width, height. The lores
    // parameters are for the camera’s preview box and must be less than or equal to the
    // width/height parameters.
    public PiCamera(PythonInterpreter interpreter, int width, int height, int lores_width, int lores_height) {
        this.interpreter = interpreter;
        interpreter.getInterpreter().eval("import ../Python/pi_camera");
        camera = interpreter.getInterpreter().invoke("pi_camera.Camera",width,height,lores_width,lores_height);
    }

    // Opens the cameras preview box
    public void startPreview() {
        interpreter.getInterpreter().invoke("pi_camera.start_preview",camera);
    }

    // Starts the camera running. Should be called immediately following start_preview
    public void start() {
        interpreter.getInterpreter().invoke("pi_camera.start",camera);     
    }

    // Delays the program ’seconds’ seconds.
    public void delay(int seconds) {
        interpreter.getInterpreter().invoke("pi_camera.delay",seconds);
    }

    // Returns a width x height image capture stored in a python object format. 
    // Can be passed to TMModel for classification
    public Object capture() {
            return interpreter.getInterpreter().invoke("pi_camera.capture",camera); 
    }

    // Stops the camera. Should be called before stop preview
    public void stop() {
        interpreter.getInterpreter().invoke("pi_camera.stop",camera);     
    }

    // Stops the preview box.
    public void stop_preview() {
        interpreter.getInterpreter().invoke("pi_camera.stop_preview",camera);     
    }


    public static void main(String[] args) throws FileNotFoundException {
        PythonInterpreter inter = new PythonInterpreter(args);
        PiCamera test = new PiCamera(inter,224,224,224,224);
        test.startPreview();
        test.start();
        test.delay(5);
        test.capture();
        test.stop_preview();
        test.stop();
        inter.close();
    }




}