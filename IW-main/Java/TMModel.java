package Java;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TMModel {
    private String model_path;
    private String label_path;
    private PythonInterpreter interpreter;
    private Object model_interpreter;
    private String[] labels;

    public final static int MODEL_WIDTH = 224;
    public final static int MODEL_HEIGHT = 224;

    // Returns a new classifier using a python interpreter and quantized TFlite
    // model.
    // Data_folder is the absolute path to the .tflite model folder.
    public TMModel(PythonInterpreter interpreter, String data_folder) {
        this.interpreter = interpreter;
        model_path = data_folder + "model.tflite";
        label_path = data_folder + "labels.txt";
        interpreter.getInterpreter().eval("import ../Python/tm_model");
        model_interpreter = interpreter.getInterpreter().invoke("tm_model.setup_interpreter", model_path);
        interpreter.getInterpreter().eval("labels = tm_model.load_labels(\"" + label_path + "\")");

        Object[] labelsObject = interpreter.getInterpreter().getValue("labels", ArrayList.class).toArray();

        labels = new String[labelsObject.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = labelsObject[i].toString();
        }
    }

    // Classifies a 224x224 numpy image array. The object passed in should be an
    // image
    // captured using the PiCamera api. Returns a Tuple with the classification and
    // confidence of the model as a Tuple.
    public Tuple<String, Double> classify(Object image_array) {
        interpreter.getInterpreter().set("model_interpreter", model_interpreter);
        interpreter.getInterpreter().set("image_array", image_array);
        interpreter.getInterpreter().eval("label_id, prob = tm_model.classify_image(model_interpreter,image_array)");
        int label_id = interpreter.getInterpreter().getValue("label_id", Integer.class);
        interpreter.getInterpreter().eval("import numpy as np;prob = np.round(prob,4)");
        String prob = "" + interpreter.getInterpreter().getValue("prob", Double.class);
        return new Tuple<String, Double>(labels[label_id], Double.parseDouble(prob));

    }

    // Returns the classification label of a given classification
    public String getLabel(Tuple<String, Double> classification) {
        return classification.name();
    }

    // Returns the models probability assignment of the label
    public double getProbability(Tuple<String, Double> classification) {
        return classification.value();
    }

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.nanoTime();

        String[] ar = { "/home/pi/Downloads" };
        PythonInterpreter inter = new PythonInterpreter(ar);
        TMModel model = new TMModel(inter, "/home/pi/Downloads/");
        PiCamera camera = new PiCamera(inter, 224, 224, 224, 224);
        camera.startPreview();
        camera.start();
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            Object capture = camera.capture();
            Tuple<String, Double> classification = model.classify(capture);
            System.out.println(classification);
        }
        camera.stop_preview();

        long end = System.nanoTime();
        System.out.println((end - start) / 1000000);

    }

}
