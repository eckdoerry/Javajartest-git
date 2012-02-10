/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

// Modified by Eck Doerry 2/2012, to show packaging of image and sound resources, playable in runnable jar.
// Requires image files within an "image" subfolder inside the package
// Requires sound files inside a "sounds" folder" inside the project package.

package components;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class MediaPackageDemo extends JPanel
                        implements ActionListener {
    protected JButton b1, b2, b3, b4;
    protected JTextArea t1;
    static final long serialVersionUID=1;
    private AudioClip beepSound;
    private static ArrayList<String> textContents;
   

    public MediaPackageDemo() {
        setLayout(new BorderLayout());
        ImageIcon leftButtonIcon = createImageIcon("images/right.gif");
        ImageIcon middleButtonIcon = createImageIcon("images/middle.gif");
        ImageIcon rightButtonIcon = createImageIcon("images/left.gif");

        b1 = new JButton("Disable middle button", leftButtonIcon);
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        b1.setMnemonic(KeyEvent.VK_D);
        b1.setActionCommand("disable");

        b2 = new JButton("Giddy-up pardner!!", middleButtonIcon);
        b2.setVerticalTextPosition(AbstractButton.BOTTOM);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.setMnemonic(KeyEvent.VK_M);
        b2.setActionCommand("doAction");

        b3 = new JButton("Enable middle button", rightButtonIcon);
        //Use the default text position of CENTER, TRAILING (RIGHT).
        b3.setMnemonic(KeyEvent.VK_E);
        b3.setActionCommand("enable");
        b3.setEnabled(false);
        
        b4 = new JButton("Clear the Deck!");
        b4.setActionCommand("doClear");
        b4.setEnabled(false);
        
        t1= new JTextArea("", 40, 40);

        //Listen for actions on buttons 1 and 3.  // Eck: Listens on button2 now too
        b1.addActionListener(this);
        b3.addActionListener(this);
        b2.addActionListener(this);
        b4.addActionListener(this);

        b1.setToolTipText("Click this button to disable the middle button.");
        b2.setToolTipText("This middle button plays a sound when you click it.");
        b3.setToolTipText("Click this button to enable the middle button.");
        b4.setToolTipText("Click this button to clear the text.");

        //Add Components to this container, using the default FlowLayout.
        add(b4, BorderLayout.NORTH);
        add(b1, BorderLayout.WEST);
        add(b2, BorderLayout.CENTER);
        add(b3, BorderLayout.EAST);
        add(t1, BorderLayout.SOUTH);
        
        // Finally, load up the sound clip so it's ready to play;
        beepSound = createAudioClip("sounds/cowboy.wav");
       
        // And now load up the text file from the jar. Just read into String array...
        textContents= getTextFile("docs/demo.txt");
    }

    public void actionPerformed(ActionEvent e) {
    	if ("disable".equals(e.getActionCommand())) {
    		b2.setEnabled(false);
    		b1.setEnabled(false);
    		b3.setEnabled(true);
    	} else if ("doAction".equals(e.getActionCommand())) {
    		if (beepSound != null) {
    			beepSound.play();  			
    		} else {
    			Toolkit.getDefaultToolkit().beep();
    		}
    		//and dump the textfile contents to the textarea...again
    		for(int i=0; i<textContents.size(); i++) {
    			t1.append(textContents.get(i)+"\n");
    		}
    		t1.append("\n");
    		b4.setEnabled(true);
    	} else if ("doClear".equals(e.getActionCommand())) {
    		t1.setText("");
    		b4.setEnabled(false);
    	} else {
    		b2.setEnabled(true);
    		b1.setEnabled(true);
    		b3.setEnabled(false);
    	}
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MediaPackageDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
        	System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /** Returns an new AudioClip, or null if the path was invalid. */
    protected static AudioClip createAudioClip(String path) {
        java.net.URL clipURL = MediaPackageDemo.class.getResource(path);
        if (clipURL != null) {
            return Applet.newAudioClip(clipURL);
        } else {
        	System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /** Used to read in a specified text file, which is stashed in the package (and thus
     * ultimately in the JAR file, if you choose to package it up that way!
    */
    private static ArrayList<String> getTextFile(String path) {
    	InputStream inStream= MediaPackageDemo.class.getResourceAsStream(path);
    	ArrayList<String> inList = new ArrayList<String>();
    	if (inStream != null) {
    		BufferedReader in= new BufferedReader(new InputStreamReader(inStream));
    		String nextLine;
    		try {   // Whole try-catch is a bit of a waste, but needed to satisfy readLine 
    			while( (nextLine= in.readLine()) != null) {
    				inList.add(nextLine);
    			}
    		} catch (IOException ex) {
    			System.out.println("something bad happened!");
    			System.exit(0);
    		}
    		return inList;
    	} else {
    		System.err.println("Couldn't find file: " + path);
    		return null;
    	}
    }

    /**
     * Create the GUI and show it.  For thread safety, 
     * this method should be invoked from the 
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("ButtonDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MediaPackageDemo newContentPane = new MediaPackageDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
}
