import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */
public class main {

    /**
     * De main function calls all the other fucntions and catches exceptions.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            ArrayList<String> headerAndSequence = ORFPredictionTool.readFile();
            checkDNAFASTA.check(headerAndSequence);
            String RNAseq = ORFPredictionTool.transcribe(headerAndSequence);
            ArrayList<ArrayList<String>> startAndStopCodons = ORFPredictionTool.setStartAndStopCodons();
            ArrayList<String> allReadingFrames = ORFPredictionTool.getAllReadingFrames(RNAseq);
            ORFPredictionTool.getORFs(allReadingFrames.get(0), startAndStopCodons, "+1");
            ORFPredictionTool.getORFs(allReadingFrames.get(1), startAndStopCodons, "+2");
            ORFPredictionTool.getORFs(allReadingFrames.get(2), startAndStopCodons, "+3");
            ORFPredictionTool.getORFs(allReadingFrames.get(3), startAndStopCodons, "-1");
            ORFPredictionTool.getORFs(allReadingFrames.get(4), startAndStopCodons, "-2");
            ORFPredictionTool.getORFs(allReadingFrames.get(5), startAndStopCodons, "-3");
            Collections.sort(ORFPredictionTool.foundORFs);
        } catch (NotAnValidDNAFASTA ex){
            System.out.println(ex);
        } catch (IndexOutOfBoundsException ex){
            System.out.println(ex);
        }
    }
}
