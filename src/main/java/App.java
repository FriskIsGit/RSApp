import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class App {
    public void display() {
        JFrame frame = new JFrame();

        addLabelToFrame(frame, "2<length<2048", 55, 25, 140, 20);
        addLabelToFrame(frame, "Bit length of primes", 55, 5, 200, 20);
        addLabelToFrame(frame, "Value of e:",310,20,100,20);
        addLabelToFrame(frame, "N: ",550,20,100,20);
        addLabelToFrame(frame, "Given Message: ",40, 210, 140, 50);
        addLabelToFrame(frame, "Decrypted Message: ",40, 340, 200, 50);
        addLabelToFrame(frame, "length%32 != 1",55, 95, 200, 50);

        JTextField fieldPrimeLength = new JTextField();
        frame.add(fieldPrimeLength);
        fieldPrimeLength.setBounds(55, 50, 170, 60);
        fieldPrimeLength.setFont(new Font("Source Sans Pro Black", Font.BOLD, 20));
        fieldPrimeLength.setForeground(new Color(0x00FF00));
        fieldPrimeLength.setBackground(Color.black);
        fieldPrimeLength.setCaretColor(Color.white);

        JTextField fieldE = new JTextField();
        frame.add(fieldE);
        fieldE.setBounds(280, 50, 200, 60);
        fieldE.setFont(new Font("Source Sans Pro Black", Font.ITALIC, 20));
        fieldE.setForeground(new Color(0x00FF00));
        fieldE.setBackground(Color.black);
        fieldE.setCaretColor(Color.white);

        JTextField fieldN = new JTextField();
        frame.add(fieldN);
        fieldN.setVisible(false);
        fieldN.setEditable(false);
        fieldN.setBounds(530, 50, 250, 60);
        fieldN.setFont(new Font("Source Sans Pro Black", Font.ITALIC, 20));
        fieldN.setForeground(new Color(0x00FF00));
        fieldN.setBackground(Color.black);
        fieldN.setCaretColor(Color.white);

        JTextField fieldToDecrypt = new JTextField();
        frame.add(fieldToDecrypt);
        fieldToDecrypt.setBounds(40, 250, 700, 50);
        fieldToDecrypt.setFont(new Font("Source Sans Pro Black", Font.ITALIC, 20));
        fieldToDecrypt.setForeground(new Color(0x00FF00));
        fieldToDecrypt.setBackground(Color.black);
        fieldToDecrypt.setCaretColor(Color.white);

        JTextField decryptedMessageField = new JTextField();
        frame.add(decryptedMessageField);
        decryptedMessageField.setBounds(40, 380, 700, 50);
        decryptedMessageField.setFont(new Font("Source Sans Pro Black", Font.ITALIC, 20));
        decryptedMessageField.setForeground(new Color(0x00FF00));
        decryptedMessageField.setBackground(Color.black);
        decryptedMessageField.setCaretColor(Color.white);

        JButton valuesButton = new JButton("Submit values");
        frame.add(valuesButton);
        valuesButton.setFocusable(false);
        valuesButton.setBounds(175, 135, 150, 40);
        valuesButton.addActionListener(new ActionListener() {
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

                    valuesButton.setEnabled(false);
                    fieldPrimeLength.setEditable(false);
                    fieldE.setEditable(false);

                    // continue calculations for RSA
                    BigInteger p = AppContainer.generatePrime(Integer.parseInt(length));
                    BigInteger q = AppContainer.generatePrime(Integer.parseInt(length));
                    BigInteger N = p.multiply(q);
                    BigInteger Fi = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
                    BigInteger e = new BigInteger(strE);
                    for (; e.compareTo(Fi) < 0; e = e.add(new BigInteger("1"))) {
                        if(AppContainer.isPrime(e,128) && !GeneratorDecryptor.isDivisible(Fi,e)){
                            fieldE.setText(String.valueOf(e));
                            break;
                        }
                    }
                    fieldN.setText(String.valueOf(N));
                    fieldN.setVisible(true);
                    BigInteger d = AppContainer.findD(e, Fi);

                    JButton decryptButton = new JButton("Decrypt!");
                    frame.add(decryptButton);
                    decryptButton.setFocusable(false);
                    decryptButton.setBounds(340, 305, 90, 40);
                    decryptButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent decryptionEvent) {
                            System.out.println("Pressed Decrypt button");
                            String decryptedStr = AppContainer.decrypt(d,N,fieldToDecrypt.getText());
                            decryptedMessageField.setText(decryptedStr);
                        }
                    });
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
