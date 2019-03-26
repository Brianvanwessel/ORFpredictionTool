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
            findORFsButton.setEnabled(true);
            String file = readFastaField.getText();
            try{
                headerAndSequence = orfPredict.readFile(file);
                checkDNAFASTA.check(headerAndSequence);
                    String RNAseq = ORFPredictionTool.transcribe(headerAndSequence);
                    allReadingFrames.addAll(ORFPredictionTool.getAllReadingFrames(RNAseq));
                    uploadFileStatusLabel.setText("done");
            }catch (NotAnValidDNAFASTA ex){

            } catch (FileNotFoundException ex) {

            } catch (IOException ex) {

            }catch (IllegalSymbolException ex) {

            } catch (IllegalAlphabetException ex) {

            }
        }
        if (e.getSource() == findORFsButton){
            try {
                ArrayList<ArrayList<String>> headerResultORFList;
                headerResultORFList = Database.checkDatabaseORFInfo(headerAndSequence.get(0));
                if(headerResultORFList.size() == 0){
                    ArrayList<ArrayList<String>> startAndStopCodons;
                    ArrayList<String> stopCodons;
                    ArrayList<String> startCodons;
                    Integer minimalORFLength = Integer.parseInt(minimalORFLengthComboBox.getSelectedItem().toString());
                    String selectedStartCodons = startCodonComboBox.getSelectedItem().toString();
                    String seletedStopCodons = stopCodonComboBox.getSelectedItem().toString();
                    startCodons = orfPredict.setCodons(selectedStartCodons);
                    stopCodons = orfPredict.setCodons(seletedStopCodons);
                    startAndStopCodons = orfPredict.combineStartandStopCodons(startCodons, stopCodons);
                    System.out.println(allReadingFrames);
                    orfPredict.getORFs(allReadingFrames.get(0), startAndStopCodons, "+1", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(1), startAndStopCodons, "+2", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(2), startAndStopCodons, "+3", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(3), startAndStopCodons, "-1", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(4), startAndStopCodons, "-2", minimalORFLength);
                    orfPredict.getORFs(allReadingFrames.get(5), startAndStopCodons, "-3", minimalORFLength);
                    Collections.sort(ORFPredictionTool.foundORFs);
                } else {
                    findORFsButton.setEnabled(false);
                    String[][] headerResultORFArray = Database.arraylistToArray(headerResultORFList);
                    setORFResultsTable(headerResultORFArray);


                }

            }catch (ClassNotFoundException ex) {

            } catch (SQLException ex){

            }
        }
        if (e.getSource() == blastButton){
            try {
                Database.getDatabaseBLASTInfo(1);
            }catch (ClassNotFoundException ex ){

            }catch (SQLException ex){

            }
        }

    }
}
