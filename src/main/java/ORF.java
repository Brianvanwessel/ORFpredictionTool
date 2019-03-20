/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */
public class ORF implements Comparable<ORF> {
    private Integer ORF_start;
    private Integer ORF_stop;
    private String Reading_Frame;

    public ORF(Integer ORF_start, Integer ORF_stop, String reading_Frame) {
        this.ORF_start = ORF_start;
        this.ORF_stop = ORF_stop;
        Reading_Frame = reading_Frame;
    }

    public Integer getORF_start() {
        return ORF_start;
    }

    public void setORF_start(Integer ORF_start) {
        this.ORF_start = ORF_start;
    }

    public Integer getORF_stop() {
        return ORF_stop;
    }

    public void setORF_stop(Integer ORF_stop) {
        this.ORF_stop = ORF_stop;
    }

    public String getReading_Frame() {
        return Reading_Frame;
    }

    public void setReading_Frame(String reading_Frame) {
        Reading_Frame = reading_Frame;
    }

    @Override
    public String toString() {
        return "ORF{" +
                ", ORF_start=" + ORF_start +
                ", ORF_stop=" + ORF_stop +
                ", Reading_Frame='" + Reading_Frame + '\'' +
                '}';
    }

    public int compareTo(ORF compareORF){
        if((this.getORF_stop() - this.getORF_start()) > (compareORF.getORF_stop() - compareORF.getORF_start())){
            return -1;}
        else if((this.getORF_stop() - this.getORF_start()) < (compareORF.getORF_stop() - compareORF.getORF_start())) {
            return 1;
        } else{return 0;}

    }
}
