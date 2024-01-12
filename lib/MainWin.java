package lib;
import javax.swing.*;

public class MainWin extends JFrame{
    public MainWin(){
        
        
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        this.add(panel);

        JLabel addressLabel = new JLabel("Address");
        JTextField addressField = new JTextField(20);
        panel.add(addressLabel);
        panel.add(addressField);

        JLabel portLabel = new JLabel("Port");
        JTextField portField = new JTextField(20);
        panel.add(portLabel);
        panel.add(portField);

        JLabel lengthLabel = new JLabel("Packet Length");
        JTextField lengthField = new JTextField(20);
        panel.add(lengthLabel);
        panel.add(lengthField);

        JButton button = new JButton("Submit");
        panel.add(button);

        button.addActionListener(e -> {
            String address = addressField.getText();
            String port = portField.getText();
            String length = lengthField.getText();

            System.out.println("Address: " + address);
            System.out.println("Port: " + port);
            System.out.println("Packet Length: " + length);
        });

        this.setVisible(true);
        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        
    }
    public static void main(String[] args){
        new MainWin();
    }
}

