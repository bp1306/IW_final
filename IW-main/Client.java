
import java.io.FileNotFoundException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame
        implements KeyListener,
        ActionListener
{
    JTextArea displayArea;
    JTextField typingArea;
    static final String newline = System.getProperty("line.separator");
    PiCamera camera; 
    PiCar car;
    TMModel model;
     
    public static void main(String[] args) throws FileNotFoundException {  
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
         
        //Schedule a job for event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				try {
                createAndShowGUI();
			}
			catch(FileNotFoundException ex) {
				ex.printStackTrace();
			}
            }
        });
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI()  throws FileNotFoundException {
		try {
			String[] ar = {"/home/pi/Downloads"};
			PythonInterpreter inter = new PythonInterpreter(ar);
			//Create and set up the window.
			Client frame = new Client("KeyEventDemo",inter);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 
			//Set up the content pane.
			frame.addComponentsToPane();
			 
			 
			//Display the window.
			frame.pack();
			frame.setVisible(true);
		}
		catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
     
    private void addComponentsToPane() {
         
        JButton button = new JButton("Clear");
        button.addActionListener(this);
         
        typingArea = new JTextField(20);
        typingArea.addKeyListener(this);
         
        //Uncomment this if you wish to turn off focus
        //traversal.  The focus subsystem consumes
        //focus traversal keys, such as Tab and Shift Tab.
        //If you uncomment the following line of code, this
        //disables focus traversal and the Tab events will
        //become available to the key event listener.
        //typingArea.setFocusTraversalKeysEnabled(false);
         
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));
         
        getContentPane().add(typingArea, BorderLayout.PAGE_START);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.PAGE_END);
    }
     
    public Client(String name, PythonInterpreter inter) throws FileNotFoundException {
		 super(name);
		
        // Creat the Model
         model = new TMModel(inter,"/home/pi/Downloads/");

         // Start the camera

        camera = new PiCamera(inter,224,224,224,224);
        camera.startPreview();
        camera.start();

        // Create the car
        car = new PiCar(inter,17,22,18,23);
       
    }
     
     
    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        //displayInfo(e, "KEY TYPED: ");
        
    }
     
    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()=='w')
			car.forward();
		else if (e.getKeyChar()=='a')
			car.left();
		else if (e.getKeyChar()=='s')
			car.backward();
		else if (e.getKeyChar()=='d')
			car.right();
		else if (e.getKeyChar()==' ') {
			Object capture = camera.capture();
            Tuple<String,Double> classification = model.classify(capture);
            System.out.println(classification);
			displayArea.append(classification + newline);
			displayArea.setCaretPosition(displayArea.getDocument().getLength());
		}
    }
     
    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        car.halt();
    }
     
    
}
