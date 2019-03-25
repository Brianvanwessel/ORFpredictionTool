/**
 * @Author Carlijn Fransen
 * @Date 22 march 2019
 * @Project ORFFinder GUI
 */

import javax.swing.*;

/**
 *
 */
public class ORFGUI {
    private JPanel panel1;
    private JTextField readFastaField;
    private JButton uploadFastaButton;
    private JButton zoekFile;
    private JComboBox stopCodonComboBox;
    private JComboBox startCodonComboBox;
    private JLabel uploadFileStatusLabel;
    private JButton findORFsButton;
    private JComboBox chooseGeneComboBox;
    private JButton blastButton;
    private JTextArea resultArea;
    private JComboBox minimalORFLength;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ORFGUI");
        frame.setContentPane(new ORFGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
