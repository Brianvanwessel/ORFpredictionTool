import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
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
    Database database = new Database();
    ArrayList<String> allReadingFrames = new ArrayList<String>();
    ArrayList<String> headerAndSequence = new ArrayList<String>();
    ArrayList<Integer> usedORFIDS = new ArrayList<Integer>();
    String filePath = "";
    /**
     * De main function calls all the other fucntions and catches exceptions.
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
     * @param e an ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == zoekFileButton){
            String filePath = orfPredict.chooseFile();
            readFastaField.setText(filePath);
        }
        if (e.getSource() == uploadFastaButton){
            try{
                uploadFileStatusLabel.setText("Uploading");
                findORFsButton.setEnabled(true);
                String file = readFastaField.getText();
                headerAndSequence = orfPredict.readFile(file);
                checkDNAFASTA.check(headerAndSequence);
                String RNAseq = ORFPredictionTool.transcribe(headerAndSequence.get(1));
                allReadingFrames.addAll(ORFPredictionTool.getAllReadingFrames(RNAseq));
                uploadFileStatusLabel.setText("Done");
            }catch (NotAnValidDNAFASTA ex){

            } catch (FileNotFoundException ex) {

            } catch (IOException ex) {

            }catch (IllegalSymbolException ex) {

            } catch (IllegalAlphabetException ex) {

            }
        }
        if (e.getSource() == findORFsButton){
            try {
                Integer sequenceID = Database.checkHeader(headerAndSequence.get(0));
                System.out.println(sequenceID);
                if(sequenceID == 0){
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
                    usedORFIDS = Database.insertORFSIntoDatabase(ORFPredictionTool.foundORFs,updatedSequenceID);
                    setORFComobox(usedORFIDS);
                    findORFsButton.setEnabled(false);
                    blastButton.setEnabled(true);
                } else {
                    findORFsButton.setEnabled(false);
                    blastButton.setEnabled(false);
                    ArrayList<ArrayList<String>> headerResultORFList = Database.checkDatabaseORFInfo(headerAndSequence.get(0));
                    String[][] headerResultORFArray = ORFPredictionTool.arraylistToArray(headerResultORFList);
                    setORFResultsTable(headerResultORFArray);

                }

            }catch (ClassNotFoundException ex) {
                ex.printStackTrace();

            } catch (SQLException ex){
                ex.printStackTrace();

            }
        }
        if (e.getSource() == blastButton){
            try {
                Integer selectedORF = Integer.parseInt(chooseORFComboBox.getSelectedItem().toString());
                Integer startORF = usedORFIDS.get(0);
                Integer orfPosition = selectedORF - startORF;
                String orfSequence = orfPredict.getORFSequence(ORFPredictionTool.foundORFs.get(orfPosition),headerAndSequence.get(1));
                Database.getDatabaseBLASTInfo(1);
                blastButton.setEnabled(false);
            }catch (ClassNotFoundException ex ){

            }catch (SQLException ex){

            }
        }

    }
}
