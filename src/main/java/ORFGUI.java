import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * @Author Carlijn Fransen
 * @author Brian van Wessel
 * @Version 1
 * @Since 1
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
    protected JComboBox chooseORFComboBox;
    protected JButton blastButton;
    protected JComboBox minimalORFLengthComboBox;
    protected JTable resultsTable;

    /**
     * The function setORFResultsTable adds the ORF results into the JTABLE.
     * @param results an Array containing ORF results.
     */
    public void setORFResultsTable(String[][] results){
        String [] column = {"ORF_Start","ORF_Stop","Readingframe","ORF_ID"};
        DefaultTableModel model = new DefaultTableModel(results,column);
        resultsTable.setModel(model);
        resultsTable.setVisible(true);
        }

    /**
     * The function setORFCombobox adds all ORFs into chooseORFCombobox.
     * @param usedORFIDS an Arraylist ocntaining all found ORFS.
     */
    public void setORFComobox(ArrayList<String> usedORFIDS){
        for (int i = 0;i < usedORFIDS.size();i++){
            chooseORFComboBox.addItem(usedORFIDS.get(i));
        }
    }

    /**
     * The function setALLResultsTaBLE adds all Results into the JTABLE
     * @param results an Array containing all results.
     */
    public void setALLResultsTable(String[][] results){
        String [] column = {"ORF_Start","ORF_Stop","Readingframe","BlAST_start","BLAST_stop","Percentage_identity","E_value"};
        DefaultTableModel model = new DefaultTableModel(results,column);
        resultsTable.setModel(model);
        resultsTable.setVisible(true);
    }

    /**
     * The function setBLASTResultsTable adds all BLAST results into the JTABLE
     * @param results an array containing all BLAST results.
     */
    public void setBLASTResultsTable(String[][] results){
        String [] column = {"BLAST_Start","BLAST_Stop","Percentage_identity","E_value"};
        DefaultTableModel model = new DefaultTableModel(results,column);
        resultsTable.setModel(model);
        resultsTable.setVisible(true);
    }
}
