/**
 * @Author Carlijn Fransen
 * @Date 22 march 2019
 * @Project ORFFinder GUI
 */

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 *
 */
public class ORFGUI extends JFrame {
    protected JPanel oRFPredictionToolPanel;
    protected JTextField readFastaField;
    protected JButton uploadFastaButton;
    protected JButton zoekFileButton;
    protected JComboBox stopCodonComboBox;
    protected JComboBox startCodonComboBox;
    protected JLabel uploadFileStatusLabel;
    protected JButton findORFsButton;
    protected JComboBox chooseGeneComboBox;
    protected JButton blastButton;
    protected JTextArea resultArea;
    protected JComboBox minimalORFLengthComboBox;

}
