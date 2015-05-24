package studentside;

import com.illposed.osc.*;
import studentside.gui.Console;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.lang.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class StudentSide extends JFrame {
    public static Console console = new Console();
    public static JTextField inputField = new JTextField();

    public static Font font = new Font("Avenir-Light", Font.PLAIN, 12);

    private void addComponentsToPane() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(panel);
        setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK);

        console.setEditable(false);

        JScrollPane consoleScrollPane = new JScrollPane(console);

        console.setBackground(new Color(230, 230, 250));
        console.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        console.setFont(font);

        DefaultCaret caret = (DefaultCaret) console.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        inputField.setFont(font);

        inputField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterKeyPressed");
        inputField.getActionMap().put("enterKeyPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.printToConsole("[User] " + inputField.getText());

                inputField.setText("");
            }
        });

        getContentPane().add(inputField, BorderLayout.SOUTH);
        getContentPane().add(consoleScrollPane);
    }

    private static void createAndShowGUI() {
        StudentSide frame = new StudentSide("Quiz");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addComponentsToPane();

        frame.setSize(800, 600);
        frame.setVisible(true);

        frame.setResizable(false);

        inputField.requestFocusInWindow();
    }

    public StudentSide(String name) {
        super(name);

        try {
            OSCPortIn receiver = new OSCPortIn(OSCPort.defaultSCOSCPort());

            OSCListener listener = new OSCListener() {
                @Override
                public void acceptMessage(Date time, OSCMessage message) {
                    List<Object> receivedInput = message.getArguments();

                    for(int i = 0; i < receivedInput.size(); i++) {
                        console.printToConsole(receivedInput.get(i).toString());
                    }

                    System.out.println("Message received");
                }
            };

            receiver.addListener("/msg", listener);
            receiver.startListening();

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        console.append("Hello! Welcome to Nate's presentation's quiz!");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
