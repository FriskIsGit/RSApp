import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class App {
    public void display() {
        JFrame frame = new JFrame();

        addLabelToFrame(frame, "2<length<2048", 70, 5, 140, 20);
        addLabelToFrame(frame, "Bit length of primes", 70, 20, 200, 20);
        addLabelToFrame(frame, "Value of e",300,20,100,20);

        JTextField fieldE = new JTextField();
        frame.add(fieldE);
        fieldE.setVisible(true);
        fieldE.setBounds(300, 50, 200, 60);
        fieldE.setFont(new Font("Source Sans Pro Black", Font.ITALIC, 20));
        fieldE.setForeground(new Color(0x00FF00));
        fieldE.setBackground(Color.black);
        fieldE.setCaretColor(Color.white);

        JTextField fieldPrimeLength = new JTextField();
        frame.add(fieldPrimeLength);
        fieldPrimeLength.setVisible(true);
        fieldPrimeLength.setBounds(55, 50, 170, 60);
        fieldPrimeLength.setFont(new Font("Source Sans Pro Black", Font.BOLD, 20));
        fieldPrimeLength.setForeground(new Color(0x00FF00));
        fieldPrimeLength.setBackground(Color.black);
        fieldPrimeLength.setCaretColor(Color.white);

        JTextField fieldN = new JTextField();

        JButton buttonPrimeLength = new JButton("Submit");
        buttonPrimeLength.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (fieldPrimeLength.getText().length() != 0 && fieldPrimeLength.getText().length() < 5 && fieldE.getText().length() != 0) {
                    String length = fieldPrimeLength.getText();
                    String strE = fieldE.getText();

                    String regex = "[0-9]+";
                    if (!length.matches(regex) || !strE.matches(regex)) {
                        alert();
                        return;
                    }

                    buttonPrimeLength.setEnabled(false);
                    fieldPrimeLength.setEditable(false);

                    // continue calculations for RSA
                    BigInteger p;
                    BigInteger q;
                    BigInteger N;
                    BigInteger Fi;
                    BigInteger e;
                    BigInteger d;
                } else {
                    alert();
                }
            }

            private void alert() {
                JOptionPane.showMessageDialog(frame, "You made a mistake boi");
                fieldPrimeLength.setText("");
                fieldE.setText("");
            }
        });
        buttonPrimeLength.setFocusable(false);
        buttonPrimeLength.setBounds(65, 120, 150, 40);

        frame.add(buttonPrimeLength);

        frame.setSize(800, 600);
        frame.setTitle("RSA");
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void addLabelToFrame(JFrame frame, String text, int x, int y, int width, int height) {
        JLabel label = new JLabel();
        label.setVisible(true);
        label.setFont(new Font("MV Boli", Font.BOLD, 17));
        label.setForeground(Color.cyan);
        label.setText(text);
        label.setBounds(x, y, width, height);

        frame.add(label);
    }
}
