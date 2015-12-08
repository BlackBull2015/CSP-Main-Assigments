/**
 * Created by Eric on 9/28/2015.
 * Sample Gui using Swing worker//
 */
package Gui;

import javax.swing.*;
//import java.awt.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;



public class GuiThreads extends JPanel {
    private JButton jb1;
    private JButton jb2;
    private JButton jb3;
    private JButton jb4;
    private JTextArea jcomp5;
    private JTextArea jcomp6;
    private JTextArea jcomp7;
    private JTextArea jcomp8;
    File fl;


    public GuiThreads() {
        //construct components
        jb1 = new JButton ("Load");
        jb2 = new JButton ("Revers");
        jb3 = new JButton ("Reverse 2w.");
        jb4 = new JButton ("Count");
        jcomp5 = new JTextArea (5, 5);
        jcomp6 = new JTextArea (5, 5);
        jcomp7 = new JTextArea (5, 5);
        jcomp8 = new JTextArea (5, 5);

        //adjust size and set layout
        setPreferredSize (new Dimension(902, 521));
        setLayout(null);

        //add components
        add (jb1);
        add(jb2);
        add(jb3);
        add (jb4);
        add (jcomp5);
        add (jcomp6);
        add (jcomp7);
        add(jcomp8);

        //set component bounds (only needed by Absolute Positioning)
        jb1.setBounds(5, 30, 220, 30);
        jb2.setBounds(225, 30, 220, 30);
        jb3.setBounds(445, 30, 220, 30);
        jb4.setBounds (665, 30, 220, 30);
        jcomp5.setBounds (10, 60, 210, 420);
        jcomp6.setBounds (230, 60, 210, 420);
        jcomp7.setBounds (450, 60, 210, 420);
        jcomp8.setBounds(670, 60, 210, 420);

        jb1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String[] lines = new String[500];
                int i;
                JFileChooser jfc = new JFileChooser();
                jfc.showOpenDialog(jfc);
                fl = jfc.getSelectedFile();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
/******************************************************************************/
                        new SwingWorker<Integer, String>() {
                            @Override
                            protected Integer doInBackground() throws IOException {
                                String str1;
                                Integer x= 0;
                                BufferedReader br = new BufferedReader(new FileReader(fl));
                                while ((str1 = br.readLine()) != null) {
                                    publish(str1);
                                    x++;
                                }

                                return x;
                            }
                            protected void process(List<String> chunks){
                                int s = chunks.size();
                                for(int z = 0; z < s; z++) {
                                    jcomp5.append(chunks.get(z));
                                    jcomp5.append("\n");

                                }
                            }
                            @Override
                            protected void done(){
                            }
                        }.execute();

/****************************************************************************/
                    }
                });


            }
        });


        //Button one to swap each word
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
/******************************************************************************/
                        new SwingWorker<Integer, String>() {
                            @Override
                            protected Integer doInBackground() throws IOException {
                                String str1;
                                Integer x= 0;
                                BufferedReader br = new BufferedReader(new FileReader(fl));
                                while ((str1 = br.readLine()) != null) {
                                    publish(str1);
                                    x++;
                                }

                                return x;
                            }
                            protected void process(List<String> chunks){
                                String[] tokens;
                                String temp;
                                int i;
                                int s = chunks.size();
                                for(int z = 0; z < s; z++) {
                                    temp = chunks.get(z);
                                    tokens = temp.split(" ");
                                    i = tokens.length;
                                    for (int k = i; k > 0; k--) {
                                        jcomp6.append(tokens[k - 1] + " ");
                                    }
                                    jcomp6.append("\n");

                                }
                            }
                            @Override
                            protected void done(){
                            }
                        }.execute();
/******************************************************************************/
                    }
                });


            }
        });


        //Button one swat 2 words
        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
/******************************************************************************/
                        new SwingWorker<Integer, String>() {
                            @Override
                            protected Integer doInBackground() throws IOException {
                                String str1;
                                Integer x= 0;
                                BufferedReader br = new BufferedReader(new FileReader(fl));
                                while ((str1 = br.readLine()) != null) {
                                    publish(str1);
                                    x++;
                                }

                                return x;
                            }
                            protected void process(List<String> chunks){
                                int s = chunks.size();
                                int i;
                                for(int z = 0; z < s; z++) {
                                    String tokens[];
                                    tokens = chunks.get(z).split(" ");
                                    i = tokens.length;

                                    for (int x = 0; x < i; x++) {
                                        if (x % 2 == 0) {
                                            if (x == (i - 1)) {
                                                jcomp7.append(tokens[x] + " ");
                                            } else {
                                                jcomp7.append(tokens[x + 1] + " ");
                                                jcomp7.append(tokens[x] + " ");
                                            }
                                        }
                                    }
                                    jcomp7.append("\n");
                                }
                            }
                            @Override
                            protected void done(){
                            }
                        }.execute();

/****************************************************************************/
                    }
                });



            }
        });


        //Button one to count letters
        jb4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
/******************************************************************************/
                    new SwingWorker<Integer, String>() {
                        @Override
                        protected Integer doInBackground() throws IOException {
                            String str1;
                            Integer x= 0;
                            BufferedReader br = new BufferedReader(new FileReader(fl));
                            while ((str1 = br.readLine()) != null) {
                                publish(str1);
                                x++;
                            }

                            return x;
                        }
                        protected void process(List<String> chunks){
                            int s = chunks.size();
                            String array[];
                            int i;
                            String[] tokens = new String[300];
                            int[] occurance = new int[300];
                            for(int z = 0; z < s; z++) {
                                array = chunks.get(z).split(" ");
                                i = array.length;

                                for (int x = 0; x < i; x++) {
                                    innerloop:
                                    for (int y = 0; y < 200; y++) {
                                        if ((array[x].equalsIgnoreCase(tokens[y]))) {
                                            occurance[y]++;
                                            break innerloop;
                                        }
                                        else if (tokens[y] == null) {
                                            tokens[y] = array[x];
                                            occurance[y]++;
                                            break innerloop;
                                        }
                                    }
                                }
                            }
                            for (int x = 0; x < 200; x++) {
                                if (tokens[x] != null) {
                                    jcomp8.append(tokens[x] + " " + occurance[x]);
                                    jcomp8.append("\n");
                                }
                            }
                        }
                        @Override
                        protected void done(){
                        }
                    }.execute();
/****************************************************************************/
                }
            });

            }
        });
    }

    public static void main (String[] args) {
        JFrame frame = new JFrame ("MyPanel");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new GuiThreads());
        frame.pack();
        frame.setVisible (true);
    }
}
