import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class NeoNavGUI {

    public static void runCProgram() {
        try {
            Process p = Runtime.getRuntime().exec("./neonav");
            
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("NeoNav Smart Navigator");
        frame.setSize(400,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JButton runButton = new JButton("Run Navigation Engine");
        JButton exitButton = new JButton("Exit");

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runCProgram();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(runButton);
        panel.add(exitButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
