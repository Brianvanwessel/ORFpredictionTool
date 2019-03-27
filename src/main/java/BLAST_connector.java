import java.io.*;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;
import org.biojava3.core.sequence.io.util.IOUtils;
import java.util.ArrayList;
import java.util.regex.*;


public class BLAST_connector {
    static ArrayList<Hit> Hitlist = new ArrayList<Hit>();

    public static void main(String[] args) {
        //String sequence = ORF.getsequence; //TODO Aanpassen op ORF finder class
        String sequence = "MKWVTFISLLFLFSSAYSRGVFRRDAHKSEVAHRFKDLGEENFKALVLIAFAQYLQQCP"; //TODO Temp
        connector(sequence);
    }

    public static void connector(String sequence){

        String line;
        int tempid=0;
        int tempstart=0;
        int tempstop=0;
        String tempeval;
        double tempevalcalc;
        float tempevalfloat = 0.0f;
        int tempiden=0;
        int blastlinecouner =0;
        int collection=0;
        String tempdef = "";

        NCBIQBlastService service = new NCBIQBlastService();
        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(BlastProgramEnum.blastp); //TODO Aanpassen op inkomende sequentie
        props.setBlastDatabase("swissprot"); //TODO Aanpassen
        NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
        String rid = null;
        BufferedReader reader = null;

        /* Hier wordt de BLAST request verzonden */
        try {
            rid = service.sendAlignmentRequest(sequence, props);

            while (!service.isReady(rid)) {
                Thread.sleep(5000);
            }

            /* Zodra de resultaten binnen zijn worden ze ingelezen*/
            InputStream in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));

            while ((line = reader.readLine()) != null) {
                blastlinecouner +=1;

                if (blastlinecouner >= 30) {

                    if (line.contains("Hit_num")) {
                        final Pattern pattern = Pattern.compile("<Hit_num>(.+?)</Hit_num>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempid=(Integer.parseInt(matcher.group(1))-1);
                        collection+=1;
                    }

                    else if (line.contains("Hsp_query-from")){
                        final Pattern pattern = Pattern.compile("<Hsp_query-from>(.+?)</Hsp_query-from>", Pattern.DOTALL);
                        final Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        tempstart=Integer.parseInt(matcher.group(1));
                        collection+=1;
                    }

                    else if (line.contains("Hsp_query-to")){
                        final Pattern pattern = Pattern.compile("<Hsp_query-to>(.+?)</Hsp_query-to>", Pattern.DOTALL);
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

                    if (collection == 5){
                        Hitlist.add(new Hit (tempid,tempstart,tempstop,tempiden,tempevalfloat,tempdef));
                        collection=0;
                    }
                }
            }
        /* */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            IOUtils.close(reader);
            service.sendDeleteRequest(rid);
        }
        Hit[] Hitarray = Hitlist.toArray(new Hit[Hitlist.size()]);


        /* Test */
        System.out.println("Blast ID    "+Hitarray[0].getBLAST_ID());
        System.out.println("BLAST Start "+Hitarray[0].getBLAST_start());
        System.out.println("Blast Stop  "+Hitarray[0].getBLAST_stop());
        System.out.println("Blast Iden  "+Hitarray[0].getPct_iden());
        System.out.println("Blast Eval  "+Hitarray[0].getE_val());
        System.out.println("Blast Desc  "+Hitarray[0].getBLAST_def());
        System.out.println("_______________________________");
        System.out.println("Blast ID    "+Hitarray[1].getBLAST_ID());
        System.out.println("BLAST Start "+Hitarray[1].getBLAST_start());
        System.out.println("Blast Stop  "+Hitarray[1].getBLAST_stop());
        System.out.println("Blast Iden  "+Hitarray[1].getPct_iden());
        System.out.println("Blast Eval  "+Hitarray[1].getE_val());
        System.out.println("Blast Desc  "+Hitarray[1].getBLAST_def());
        System.out.println("_______________________________");
        System.out.println("Blast ID    "+Hitarray[2].getBLAST_ID());
        System.out.println("BLAST Start "+Hitarray[2].getBLAST_start());
        System.out.println("Blast Stop  "+Hitarray[2].getBLAST_stop());
        System.out.println("Blast Iden  "+Hitarray[2].getPct_iden());
        System.out.println("Blast Eval  "+Hitarray[2].getE_val());
        System.out.println("Blast Desc  "+Hitarray[2].getBLAST_def());
        System.out.println("_______________________________");
        System.out.println("Blast ID    "+Hitarray[21].getBLAST_ID());
        System.out.println("BLAST Start "+Hitarray[21].getBLAST_start());
        System.out.println("Blast Stop  "+Hitarray[21].getBLAST_stop());
        System.out.println("Blast Iden  "+Hitarray[21].getPct_iden());
        System.out.println("Blast Eval  "+Hitarray[21].getE_val());
        System.out.println("Blast Desc  "+Hitarray[21].getBLAST_def());






    }

}
