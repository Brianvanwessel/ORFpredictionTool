/**
 * @Author Carlijn Fransen
 * @Date 22 march 2019
 * @Project ORFFinder GUI
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

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
    protected JComboBox chooseORFComboBox;
    protected JButton blastButton;
    protected JComboBox minimalORFLengthComboBox;
    protected JTable resultsTable;

    public void setORFResultsTable(String[][] results){
        String [] column = {"ORF_Start","ORF_Stop","Readingframe"};
        DefaultTableModel model = new DefaultTableModel(results,column);
        resultsTable.setModel(model);
        resultsTable.setVisible(true);
        }
    public void setORFComobox(ArrayList<Integer> usedORFIDS){
        for (int i = 0;i < usedORFIDS.size();i++){
            chooseORFComboBox.addItem(usedORFIDS.get(i));
        }
    }
}
