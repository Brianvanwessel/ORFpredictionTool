import java.io.*;

import org.biojava.bio.symbol.CodonPrefFilter;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;
import org.biojava3.core.sequence.io.util.IOUtils;
import java.util.ArrayList;
import java.util.regex.*;

/**
 * @author Bram Lobker
 * @Version 2.0
 * @Since 28-03-2019
 */

public class BLAST_connector {
    static ArrayList<Hit> hitList = new ArrayList<Hit>();

    /**
     * The function settings, sets the settings for the BLAST
     * @return props is an object containing the setting used for the BLAST
     */
    public static NCBIQBlastAlignmentProperties settings(){

        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(BlastProgramEnum.blastx);
        props.setBlastDatabase("nr");

        return props;
    }
    /**
     * The function output instantiates a NCBIQBlastOutputProperties, which contains the properties for the output.
     * @return outputProps is an object, containting the properties for the output stream
     */
    public static NCBIQBlastOutputProperties output(){

        NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();

        return outputProps;
    }

    /**
     * The function connector parses the sequence to the BLAST service and accepts the Inputstream to save this im an Arraylist.
     * @param sequence, is a string containing the sequences belonging to the selected ORF.
     * @param props is an object containing the settings for the BLAST.
     * @param outputProps is an object containing the properties belonging to the output.
     * @return BLASTline, an Arraylist which contains all the lines supplied by the outputstream.
     * @throws IOException when there's no internet connection
     * @throws Exception when there are general issues with the BLAST service.
     */
    public static ArrayList<String> connector(String sequence, NCBIQBlastAlignmentProperties props, NCBIQBlastOutputProperties outputProps) throws IOException,Exception{

        String line;
        ArrayList<String> blastline = new ArrayList<String>();
        NCBIQBlastService service = new NCBIQBlastService();
        String rid = null;
        BufferedReader reader = null;

        try {
            rid = service.sendAlignmentRequest(sequence, props);

            while (!service.isReady(rid)) {
                System.out.println("Wait for  5 seconden;BLASTING");
                Thread.sleep(5000);
            }

            InputStream in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));

            while ((line = reader.readLine()) != null) {
                blastline.add(line);
            }
        } catch (IOException I){
            throw new IOException("No internet connection");
        }
        catch (Exception e) {
            throw new Exception(e);
        } finally {
            IOUtils.close(reader);
            service.sendDeleteRequest(rid);
        }

        return blastline;
    }

    /**
     * The function Result_parser extracts all the required data from the lines supplied by the BLAST output stream and
     * instantiates Hit objects with this data.
     * @param BLASTline is an Arraylist containing all the strings supplied by the BLAST outputline.
     */

        public static void Result_parser(ArrayList<String> BLASTline){
        int tempid = 0;
            int tempstart = 0;
            int tempstop = 0;
            String tempeval;
            double tempevalcalc;
            float tempevalfloat = 0.0f;
            int tempiden = 0;
            int blastlinecouner = 29;
            int collection = 0;
            String tempdef = "";
            String line;

            for (int i = 0; i < BLASTline.size(); i++) {
                line = BLASTline.get(i);
                blastlinecouner +=1;

                if (blastlinecouner >= 30) {

                    if (line.contains("Hit_num")) {
                        final Pattern pattern = Pattern.compile("<Hit_num>(.+?)</Hit_num>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempid=(Integer.parseInt(matcher.group(1))-1);
                        collection+=1;
                    }

                    else if (line.contains("Hsp_hit-from")){
                        final Pattern pattern = Pattern.compile("<Hsp_hit-from>(.+?)</Hsp_hit-from>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempstart=Integer.parseInt(matcher.group(1));
                        collection+=1;
                    }

                    else if (line.contains("Hsp_hit-to")){
                        final Pattern pattern = Pattern.compile("<Hsp_hit-to>(.+?)</Hsp_hit-to>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempstop=Integer.parseInt(matcher.group(1));
                        collection+=1;
                    }

                    else if (line.contains("Hsp_identity")){
                        final Pattern pattern = Pattern.compile("<Hsp_identity>(.+?)</Hsp_identity>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempiden=Integer.parseInt(matcher.group(1));
                        collection+=1;
                    }

                    else if (line.contains("Hsp_evalue")){
                        final Pattern pattern = Pattern.compile("<Hsp_evalue>(.+?)</Hsp_evalue>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempeval=matcher.group(1);
                        String[] parts = tempeval.split("e");

                        if (!tempeval.contains("e")){
                            tempevalfloat = Float.valueOf(tempeval);
                        }

                        else{
                        double base = Double.parseDouble(parts[0]);
                        double power= Double.parseDouble(parts[1]);
                        tempevalcalc = Math.pow(base, power);
                        tempevalfloat = (float)tempevalcalc;
                        }

                        collection+=1;

                    }

                    else if (line.contains("Hit_def")) {
                        final Pattern pattern = Pattern.compile("<Hit_def>(.+?)</Hit_def>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempdef = matcher.group(1);
                    }
                    if (collection == 6){
                        hitList.add(new Hit (tempid,tempstart,tempstop,tempiden,tempevalfloat,tempdef));
                        collection=0;
                    }
                }
            }}
    }
