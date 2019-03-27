/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */
public class ORF implements Comparable<ORF> {
    private Integer orf_start;
    private Integer orf_stop;
    private String reading_Frame;

    /**
     * This is the constructor for the ORF object.
     * @param ORF_start
     * @param ORF_stop
     * @param readingFrame
     */
    public ORF(Integer ORF_start, Integer ORF_stop, String readingFrame) {
        this.orf_start = ORF_start;
        this.orf_stop = ORF_stop;
        reading_Frame = readingFrame;
    }

    public Integer getORF_start() {
        return orf_start;
    }

    public void setORF_start(Integer orf_start) {
        this.orf_start = orf_start;
    }

    public Integer getORF_stop() {
        return orf_stop;
    }

    public void setORF_stop(Integer orf_stop) {
        this.orf_stop = orf_stop;
    }

    public String getReading_Frame() {
        return reading_Frame;
    }

    public void setReading_Frame(String reading_Frame) {
        reading_Frame = reading_Frame;
    }


    /**
     * The function toString changes the ORF visual ouput to an String.
     * @return String containing ORF properties.
     */
    @Override
    public String toString() {
        return "ORF{" +
                ", ORF_start=" + orf_start +
                ", ORF_stop=" + orf_stop +
                ", Reading_Frame='" + reading_Frame + '\'' +
                '}';
    }

    /**
     * The function comparTo compares 2 ORF objects using the length of the ORF.
     * @param compareORF the ORF that you want to compare.
     * @return return`s an int, the value depends on which ORF is bigger;
     */
    public int compareTo(ORF compareORF){
        if((this.getORF_stop() - this.getORF_start()) > (compareORF.getORF_stop() - compareORF.getORF_start())){
            return -1;}
        else if((this.getORF_stop() - this.getORF_start()) < (compareORF.getORF_stop() - compareORF.getORF_start())) {
            return 1;
        } else{return 0;}

    }
}
