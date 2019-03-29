import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */

public class ORFPredictionTool {
    private JFileChooser fileChooser;
    protected static ArrayList<ORF> foundORFs = new ArrayList<ORF>();

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
     * @param filepath A String containing the path of the chosen FAST file.
     * @return headerAndSequence is an Arraylist containg an header and sequence.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ArrayList<String> readFile(String filepath) throws FileNotFoundException,IOException {
        ArrayList<String> headerAndSequence = new ArrayList<String>();
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(filepath));
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
            throw new FileNotFoundException("Select a valid File");
        } catch (IOException ex) {
            throw new IOException("Select a valid File");
        }
        return headerAndSequence;
    }

    /**
     * The function transcribe transcribes the a DNA sequence.
     * @param dnaSequence contains the DNA sequence that has to be transcribed.
     * @return RNAseq is a String containg the transcrived DNA sequence.
     * @throws IllegalSymbolException
     * @throws IllegalAlphabetException
     */
    public static String transcribe(String dnaSequence) throws IllegalSymbolException,IllegalAlphabetException {
        String RNAseq = "";
        try {
            SymbolList symL = DNATools.createDNA(dnaSequence);
            symL = DNATools.toRNA(symL);
            RNAseq = symL.seqString().toUpperCase();
        } catch (IllegalSymbolException ex) {
            throw new IllegalSymbolException("Give an valid DNA sequence");
        } catch (IllegalAlphabetException ex) {
            throw new IllegalAlphabetException("Give an valid DNA sequence");
        }
        return RNAseq;
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
     * The function setCodons splits the selectedCodons String to get the Codons that selectedCodons contains and adds the Codons to an Arraylist.
     * @param selectedCodons A string containing different Codons seprated by ", ".
     * @return codons an Arraylist containing the Codons that where extracted from  selectedCodons.
     */
    public static ArrayList<String> setCodons(String selectedCodons){
        ArrayList<String> codons = new ArrayList<String>();
        String[] splitCodons = selectedCodons.split(", ");
        for(int i = 0; i< splitCodons.length;i++){
            codons.add(splitCodons[i]);
        }

        return codons;
    }

    /**
     * The function combineStartandStopCodons combines 2 Arraylist containing start and stop codons into one 2D Arraylist.
     * @param startCodons an Arraylist containing startcodons.
     * @param stopCodons an Arraylist containing stopcodons.
     * @return startAndStopCodons an 2D Arraylist containing 2 Arraylist.
     */
    public ArrayList<ArrayList<String>> combineStartandStopCodons(ArrayList<String> startCodons,ArrayList<String> stopCodons){
        ArrayList<ArrayList<String>> startAndStopCodons = new ArrayList<ArrayList<String>>();
        startAndStopCodons.add(startCodons);
        startAndStopCodons.add(stopCodons);
        return startAndStopCodons;
    }



    /**
     * The function searches possible ORFs in a readingframe and makes an ORF object for each ORF.
     * @param readingFramesequence is a String of the sequence of a reading frame.
     * @param startAndStopCodons is an Arrayist containing 2 arraylist with the possible start an stop codons.
     * @param readingFrame is a String containg the current readingFrame.
     */
    public void getORFs(String readingFramesequence, ArrayList<ArrayList<String>> startAndStopCodons, String readingFrame,Integer minmalORFSize) {
        Integer codonsFirstPosition = 0;
        Integer codonsSecondPosition = 1;
        Integer codonsThirtPosition = 2;
        ArrayList<Integer> foundStartCodons = new ArrayList<Integer>();
        for (int i = 0; i < (readingFramesequence.length() / 3); i++) {
            String currentCodon = "";
            Integer currentPosition = (i * 3) + 1;
            currentCodon = String.valueOf(readingFramesequence.charAt(codonsFirstPosition)) + String.valueOf(readingFramesequence.charAt(codonsSecondPosition)) + String.valueOf(readingFramesequence.charAt(codonsThirtPosition));
            if (startAndStopCodons.get(0).contains(currentCodon)) {
                foundStartCodons.add(currentPosition);
            } else if (startAndStopCodons.get(1).contains(currentCodon)) {
                if(foundStartCodons.size() > 1) {
                    if (currentPosition - foundStartCodons.get(0) > minmalORFSize) {
                        foundORFs.add(new ORF(foundStartCodons.get(0), currentPosition, readingFrame));
                    }
                }
                foundStartCodons.clear();
            }
            codonsFirstPosition += 3;
            codonsSecondPosition += 3;
            codonsThirtPosition += 3;
        }
    }

    /**
     * The function getORFSequence get the sequence that belongs to a certain ORF.
     * @param selectedORF an ORF object
     * @param fileSequence is a String containing the DNA sequence of the chosen file.
     * @return orfSequence a String containg the sequence of the selected ORF.
     */
    public String getORFSequence(ORF selectedORF,String fileSequence){
        StringBuilder fileSequenceBuilder = new StringBuilder();
        String orfSequence = "";
        fileSequenceBuilder.append(fileSequence);
        if(selectedORF.getReading_Frame().contains("-")){
            fileSequenceBuilder.reverse();
            orfSequence = fileSequenceBuilder.substring(selectedORF.getORF_start()-1,selectedORF.getORF_stop() -1);
        } else if(selectedORF.getReading_Frame().contains("+")){
            orfSequence = fileSequenceBuilder.substring(selectedORF.getORF_start()-1,selectedORF.getORF_stop() -1);
        }
        return orfSequence;
    }

    /**
     * The function arraylistToArray converts an arraylist to an 2D Array.
     * @param arraylist an arrylist that needs to be converted to an Array.
     * @return headerResultsArray is an 2D Array that contains the information of the given Arraylist./
     */
    public static String[][] arraylistToArray(ArrayList<ArrayList<String>> arraylist){
        String[][] headerResultsArray = new String[arraylist.size()][];
        for (int i = 0; i < arraylist.size(); i++) {
            ArrayList<String> row = arraylist.get(i);
            headerResultsArray[i] = row.toArray(new String[row.size()]);
        }
        return headerResultsArray;
    }
}

