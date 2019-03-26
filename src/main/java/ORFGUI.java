/**
 * @Author Carlijn Fransen
 * @Date 22 march 2019
 * @Project ORFFinder GUI
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    protected JComboBox minimalORFLengthComboBox;
    protected JTable resultsTable;

    public void setORFResultsTable(String[][] results){
        String [] column = {"ORF_Start","ORF_Stop","Readingframe"};
        DefaultTableModel model = new DefaultTableModel(results,column);
        resultsTable.setModel(model);
        resultsTable.setVisible(true);
        }

}
