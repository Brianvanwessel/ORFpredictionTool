public class Hit {


    public Hit(int BLAST_ID, int BLAST_start, int BLAST_stop, int pct_iden, float e_val, String BLAST_def) {
        this.BLAST_ID = BLAST_ID;
        this.BLAST_start = BLAST_start;
        this.BLAST_stop = BLAST_stop;
        this.pct_iden = pct_iden;
        this.e_val = e_val;
        this.BLAST_def = BLAST_def;
    }
    int BLAST_ID;
    int BLAST_start;
    int BLAST_stop;
    int pct_iden;
    float e_val;
    String BLAST_def;

    public String getBLAST_def() {
        return BLAST_def;
    }

    public void setBLAST_def(String BLAST_def) {
        this.BLAST_def = BLAST_def;
    }

    public float getE_val() {return e_val; }

    public void setE_val(float e_val) {
        this.e_val = e_val;
    }


    public int getBLAST_ID() {
        return BLAST_ID;
    }

    public void setBLAST_ID(int BLAST_ID) {
        this.BLAST_ID = BLAST_ID;
    }

    public int getBLAST_start() {
        return BLAST_start;
    }

    public void setBLAST_start(int BLAST_start) {
        this.BLAST_start = BLAST_start;
    }

    public int getBLAST_stop() {
        return BLAST_stop;
    }

    public void setBLAST_stop(int BLAST_stop) {
        this.BLAST_stop = BLAST_stop;
    }

    public int getPct_iden() {
        return pct_iden;
    }

    public void setPct_iden(int pct_iden) {
        this.pct_iden = pct_iden;
    }



}
