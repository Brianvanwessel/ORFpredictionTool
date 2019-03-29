import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */
public class main extends ORFGUI implements ActionListener {

    ORFPredictionTool orfPredict = new ORFPredictionTool();
    ArrayList<String> allReadingFrames = new ArrayList<String>();
    ArrayList<String> headerAndSequence = new ArrayList<String>();
    ArrayList<String> usedORFIDS = new ArrayList<String>();

    /**
     * De main function calls all the other functions and catches exceptions.
     *
     * @param args
     */
    public static void main(String[] args) {
        ORFGUI frame = new main();
        ((main) frame).addListener(frame);
        frame.setContentPane(frame.oRFPredictionToolPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * The function addListener ads ActionListeners to the ORFGUI frame.
     *
     * @param frame A ORFGUI frame.
     */
    public void addListener(ORFGUI frame) {
        zoekFileButton.addActionListener((ActionListener) frame);
        uploadFastaButton.addActionListener((ActionListener) frame);
        findORFsButton.addActionListener((ActionListener) frame);
        blastButton.addActionListener((ActionListener) frame);
    }

    /**
     * The function actionPerformed contains all the logic for the buttons.
     *
     * @param e an ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == zoekFileButton) {
            String filePath = orfPredict.chooseFile();
            readFastaField.setText(filePath);
        }
        if (e.getSource() == uploadFastaButton) {
            try {
                uploadFileStatusLabel.setText("Uploading");
                findORFsButton.setEnabled(true);
                String file = readFastaField.getText();
                headerAndSequence = orfPredict.readFile(file);
                checkDNAFASTA.check(headerAndSequence);
                String RNAseq = ORFPredictionTool.transcribe(headerAndSequence.get(1));
                allReadingFrames.addAll(ORFPredictionTool.getAllReadingFrames(RNAseq));
                uploadFileStatusLabel.setText("Done");
            } catch (NotAnValidDNAFASTA ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (IllegalSymbolException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (IllegalAlphabetException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            }
        }
        if (e.getSource() == findORFsButton) {
            try {
                Integer sequenceID = Database.checkHeader(headerAndSequence.get(0));
                if (sequenceID == 0) {
                    ORFPredictionTool.foundORFs.clear();
                    ArrayList<ArrayList<String>> startAndStopCodons;
                    ArrayList<String> stopCodons;
                    ArrayList<String> startCodons;
                    Integer updatedSequenceID = Database.setAndGetSequence(headerAndSequence);
                    Integer minimalORFLength = Integer.parseInt(minimalORFLengthComboBox.getSelectedItem().toString());
                    String selectedStartCodons = startCodonComboBox.getSelectedItem().toString();
                    String seletedStopCodons = stopCodonComboBox.getSelectedItem().toString();
                    startCodons = orfPredict.setCodons(selectedStartCodons);
                    stopCodons = orfPredict.setCodons(seletedStopCodons);
                    startAndStopCodons = orfPredict.combineStartandStopCodons(startCodons, stopCodons);
                    orfPredict.getORFs(allReadingFrames.get(0), startAndStopCodons, "+1", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(1), startAndStopCodons, "+2", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(2), startAndStopCodons, "+3", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(3), startAndStopCodons, "-1", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(4), startAndStopCodons, "-2", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(5), startAndStopCodons, "-3", minimalORFLength);
                    Collections.sort(ORFPredictionTool.foundORFs);
                    usedORFIDS = Database.insertORFSIntoDatabase(ORFPredictionTool.foundORFs, updatedSequenceID);
                    setORFComobox(usedORFIDS);
                    ArrayList<ArrayList<String>> headerResultORFList = Database.checkDatabaseORFInfo(headerAndSequence.get(0));
                    String[][] headerResultORFArray = ORFPredictionTool.arraylistToArray(headerResultORFList);
                    setORFResultsTable(headerResultORFArray);
                    findORFsButton.setEnabled(false);
                    blastButton.setEnabled(true);
                } else {
                    findORFsButton.setEnabled(false);
                    blastButton.setEnabled(false);
                    ArrayList<ArrayList<String>> headerResultORFList = Database.checkDatabaseInfo(headerAndSequence.get(0));
                    String[][] headerResultORFArray = ORFPredictionTool.arraylistToArray(headerResultORFList);
                    setALLResultsTable(headerResultORFArray);

                }

            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);

            }
        }
        if (e.getSource() == blastButton) {
            try {
                Integer selectedORF = Integer.parseInt(chooseORFComboBox.getSelectedItem().toString());
                Integer startORF = Integer.parseInt(usedORFIDS.get(0));
                Integer orfPosition = selectedORF - startORF;
                String orfSequence = orfPredict.getORFSequence(ORFPredictionTool.foundORFs.get(orfPosition), headerAndSequence.get(1));
                BLAST_connector.hitList.clear();
                NCBIQBlastAlignmentProperties props = BLAST_connector.settings();
                NCBIQBlastOutputProperties outputProps = BLAST_connector.output();
                ArrayList<String> blastline = BLAST_connector.connector(orfSequence, props, outputProps);
                BLAST_connector.Result_parser(blastline);
                Database.insertBLASTHitsIntoDatabase(BLAST_connector.hitList,Integer.parseInt(chooseORFComboBox.getSelectedItem().toString()));
                Database.getDatabaseORFBLASTInfo(Integer.parseInt(chooseORFComboBox.getSelectedItem().toString()));
                blastButton.setEnabled(false);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (IOException ex){
                JOptionPane.showMessageDialog(getParent(), ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(getParent(), ex);
            }
        }

    }
}
