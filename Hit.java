/**
 * @author Bram Lobker
 * @Version 1.0
 * @Since 22-03-2019
 */

public class Hit {


    public Hit(int blast_id, int blast_start, int blast_stop, int pct_iden, float e_val, String blast_def) {
        this.blast_id = blast_id;
        this.blast_start = blast_start;
        this.blast_stop = blast_stop;
        this.pct_iden = pct_iden;
        this.e_val = e_val;
        this.blast_def = blast_def;
    }
    private int blast_id;
    private int blast_start;
    private int blast_stop;
    private int pct_iden;
    private float e_val;
    private String blast_def;

    public String getBlast_def() {
        return blast_def;
    }

    public void setBlast_def(String blast_def) {
        this.blast_def = blast_def;
    }

    public float getE_val() {return e_val; }

    public void setE_val(float e_val) {
        this.e_val = e_val;
    }


    public int getBlast_id() {
        return blast_id;
    }

    public void setBlast_id(int blast_id) {
        this.blast_id = blast_id;
    }

    public int getBlast_start() {
        return blast_start;
    }

    public void setBlast_start(int blast_start) {
        this.blast_start = blast_start;
    }

    public int getBlast_stop() {
        return blast_stop;
    }

    public void setBlast_stop(int blast_stop) {
        this.blast_stop = blast_stop;
    }

    public int getPct_iden() {
        return pct_iden;
    }

    public void setPct_iden(int pct_iden) {
        this.pct_iden = pct_iden;
    }
}
