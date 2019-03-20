import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */

public class ORFPredictionTool {
    private JFileChooser fileChooser;
    private static ArrayList<ORF> foundORFs = new ArrayList<ORF>();

    /**
     * De main function calls all the other fucntions and catches exceptions.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            ArrayList<String> headerAndSequence = readFile();
            checkDNAFASTA.check(headerAndSequence);
            String RNAseq = transcribe(headerAndSequence);
            ArrayList<ArrayList<String>> startAndStopCodons = setStartAndStopCodons();
            ArrayList<String> allReadingFrames = getAllReadingFrames(RNAseq);
            getORFs(allReadingFrames.get(0), startAndStopCodons, "+1");
            getORFs(allReadingFrames.get(1), startAndStopCodons, "+2");
            getORFs(allReadingFrames.get(2), startAndStopCodons, "+3");
            getORFs(allReadingFrames.get(3), startAndStopCodons, "-1");
            getORFs(allReadingFrames.get(4), startAndStopCodons, "-2");
            getORFs(allReadingFrames.get(5), startAndStopCodons, "-3");
            Collections.sort(foundORFs);
        } catch (NotAnValidDNAFASTA ex){
            System.out.println(ex);
        } catch (IndexOutOfBoundsException ex){

        }
    }

    /**
     * The function chooseFile makes it possible to select a FASTA file from the file system.
     *
     * @return A String filePath containing the path of the chosen FASTA file.
     */
    public String chooseFile() {
        String filePath = "";
        File selectedFile;
        int reply;
        fileChooser = new JFileChooser();
        reply = fileChooser.showOpenDialog(null);
        if (reply == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
        }
        return filePath;
    }

    /**
     * The function readFile extracts the header and sequence of the chosen FASTA file.
     *
     * @return headerAndSequence is a Arraylist containg the header and sequeunce of the chosen FASTA file.
     */
    public static ArrayList<String> readFile() {
        String file = "C:\\Users\\brian\\Documents\\Jaar2\\Blok3\\Project informatica\\SD.fa";
        ArrayList<String> headerAndSequence = new ArrayList<String>();
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(file));
            String line;
            String[] splitline;
            String sequentie = "";
            while ((line = inFile.readLine()) != null) {
                splitline = line.split("\t");
                if (splitline[0].startsWith(">")) {
                    headerAndSequence.add(splitline[0]);
                } else if (!splitline[0].startsWith(">")) {
                    sequentie = sequentie + splitline[0];
                }
            }
            headerAndSequence.add(sequentie.toUpperCase());
            inFile.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return headerAndSequence;
    }

    /**
     * The function transcribe transcribes the DNA sequence from the chosen FASTA file.
     *
     * @param headerAndSequence is an Arraylist containing the header and sequeunce of the chosen FASTA file.
     * @return RNAseq is a String containg the transcrived DNA sequence from the chosen FASTA file.
     */
    public static String transcribe(ArrayList<String> headerAndSequence) {
        String RNAseq = "";
        try {
            SymbolList symL = DNATools.createDNA(headerAndSequence.get(1));
            symL = DNATools.toRNA(symL);
            RNAseq = symL.seqString().toUpperCase();
        } catch (IllegalSymbolException ex) {
            //this will happen if you try and make the DNA seq using non IUB symbols
            ex.printStackTrace();
        } catch (IllegalAlphabetException ex) {
            //this will happen if you try and transcribe a non DNA SymbolList
            ex.printStackTrace();
        }
        return RNAseq;
    }

    /**
     * The function setStartAndStopCodons makes an arraylist for the possible start and stop codons.
     *
     * @return startAndStopCodons is an Arrayist containing 2 arraylist with the possible start an stop codons.
     */

    public static ArrayList<ArrayList<String>> setStartAndStopCodons() {
        ArrayList<ArrayList<String>> startandStopCodons = new ArrayList<ArrayList<String>>();
        ArrayList<String> startCodons = new ArrayList<String>();
        ArrayList<String> stopCodons = new ArrayList<String>();
        startCodons.add("AUG");
        startandStopCodons.add(startCodons);
        stopCodons.add("UAG");
        stopCodons.add("UAA");
        stopCodons.add("UAG");
        startandStopCodons.add(stopCodons);
        return startandStopCodons;
    }

    /**
     * The function getAllReaingFrames creates a Arraylist with all reading frams from the RNAseq.
     *
     * @param RNAseq is a String containg the transcrived DNA sequence from the chosen FASTA file.
     * @return allReadingFrames is an Arraylist containg Strings of all possible Reading frames from the RNAseq.
     */

    public static ArrayList<String> getAllReadingFrames(String RNAseq) {
        ArrayList<String> allReadingFrames = new ArrayList<String>();
        allReadingFrames.add(RNAseq);
        String secondReadingFrame = RNAseq.substring(1);
        allReadingFrames.add(secondReadingFrame);
        String thirtReadingFrame = secondReadingFrame.substring(1);
        allReadingFrames.add(secondReadingFrame);
        StringBuilder firstReverseFrame = new StringBuilder();
        firstReverseFrame.append(thirtReadingFrame);
        firstReverseFrame.reverse();
        allReadingFrames.add(firstReverseFrame.toString());
        StringBuilder secondReverseFrame = new StringBuilder();
        secondReverseFrame.append(secondReadingFrame);
        secondReverseFrame.reverse();
        allReadingFrames.add(secondReverseFrame.toString());
        StringBuilder thirtReverseFrame = new StringBuilder();
        thirtReverseFrame.append(RNAseq);
        thirtReverseFrame.reverse();
        allReadingFrames.add(thirtReadingFrame);
        return allReadingFrames;
    }

    /**
     * The function searches possible ORFs in a readingframe and makes an ORF object for each ORF.
     * @param readingFramesequence is a String of the sequence of a reading frame.
     * @param startAndStopCodons is an Arrayist containing 2 arraylist with the possible start an stop codons.
     * @param readingFrame is a String containg the current readingFrame.
     */
    public static void getORFs(String readingFramesequence, ArrayList<ArrayList<String>> startAndStopCodons, String readingFrame) {
        Integer codonsFirstPosition = 0;
        Integer codonsSecondPosition = 1;
        Integer codonsThirtPosition = 2;
        ArrayList<Integer> foundStartCodons = new ArrayList<Integer>();
        for (int i = 0; i < (readingFramesequence.length() / 3); i++) {
            String currentCodon = "";
            Integer currentPosition = (i * 3) + 1;
            try {
                currentCodon = String.valueOf(readingFramesequence.charAt(codonsFirstPosition)) + String.valueOf(readingFramesequence.charAt(codonsSecondPosition)) + String.valueOf(readingFramesequence.charAt(codonsThirtPosition));
            } catch (StringIndexOutOfBoundsException ex) {
            }
            if (startAndStopCodons.get(0).contains(currentCodon)) {
                foundStartCodons.add(currentPosition);
            } else if (startAndStopCodons.get(1).contains(currentCodon)) {
                if(foundStartCodons.size() > 1) {
                    if (currentPosition - foundStartCodons.get(0) > 100) {
                        foundORFs.add(new ORF(foundStartCodons.get(0), currentPosition, readingFrame));
                    }
                }
                //for (int i2 = 0; i2 < foundStartCodons.size(); i2++) {
                   // if (currentPosition - foundStartCodons.get(i2) > 100) {
                     //   foundORFs.add(new ORF(foundStartCodons.get(i2), currentPosition, readingFrame));
                    //}
               // }
                foundStartCodons.clear();
            }
            codonsFirstPosition += 3;
            codonsSecondPosition += 3;
            codonsThirtPosition += 3;
        }
    }
}

