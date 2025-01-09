import java.io.FileNotFoundException;
import java.util.ArrayList;


public class PiCar {
    
    private PythonInterpreter interpreter;
    private Object car;

    // Returns a new PiCar object. ’fl’ is the GPIO motor which controls the left wheel’s
    // forward motion, ’bl’ is the left wheel’s backward motion. ’fr’/’br’ are the
    // equivalent parameters for the forward/backward control on the right side.
    public PiCar(PythonInterpreter interpreter, int fl, int bl, int fr, int br) {
        this.interpreter = interpreter;
        interpreter.getInterpreter().eval("import ../Python/pi_car");
        car=interpreter.getInterpreter().invoke("pi_car.pi_car",fl, bl, fr, br);
       
   }

   // moves the car forward
    public void forward() {
        interpreter.getInterpreter().invoke("pi_car.forward",car);
    }

    // moves the car backward
     public void backward() {
        interpreter.getInterpreter().invoke("pi_car.backward",car);
    }
    
    // moves the car left
     public void left() {
        interpreter.getInterpreter().invoke("pi_car.left",car);
    }
    
    // moves the car right
     public void right() {
        interpreter.getInterpreter().invoke("pi_car.right",car);
    }
    
    // stops the car
     public void halt() {
        interpreter.getInterpreter().invoke("pi_car.halt",car);
    }
    public static void main(String[] args) throws FileNotFoundException {
        long start = System.nanoTime();
        
        String[] ar = {"/home/pi/Downloads"};
        PythonInterpreter inter = new PythonInterpreter(ar);
        TMModel model = new TMModel(inter,"/home/pi/Downloads/");
        PiCamera camera = new PiCamera(inter,224,224,224,224);
        camera.startPreview();
        camera.start();
        PiCar car = new PiCar(inter,17,22,18,23);
        
        char s= 's';
        while (s != 'p') {
            if (s=='w')
                car.forward();
            else if (s=='s')
                car.halt();
            else if (s=='a')
                car.left();
            else if (s=='d')
                car.right();
            else if (s==' ') {
                 Object capture = camera.capture();
            Tuple<String,Double> classification = model.classify(capture);
            System.out.println(classification);
                
            }
            s = 'q';
            
                
        }
        

           
        camera.stop_preview();

        long end = System.nanoTime();
        System.out.println((end-start)/1000000);

    }


}
