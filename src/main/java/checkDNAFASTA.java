import java.util.ArrayList;

/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */
public class checkDNAFASTA {

    /**
     * The function check looks if the arraylist contains an header in position 0 and a DNA sequence in postion 1.
     *
     * @param headerAndSequentie is an Arraylist containing the header and sequeunce of the chosen FASTA file.
     * @throws NotAnValidDNAFASTA if the arraylist doesn`t contain a header in postion 0 or if the arraylist doesn`t contain a DNA sequence in postion 1.
     */
    public static void check(ArrayList<String> headerAndSequentie) throws NotAnValidDNAFASTA {
        if (!headerAndSequentie.get(0).startsWith(">")) {
            throw new NotAnValidDNAFASTA();
        } else if (!headerAndSequentie.get(1).matches("[ATGCN]+")) {
            throw new NotAnValidDNAFASTA();
        }
    }
}

/**
 * @author Brian van Wessel <brianvanwessel1@gmail.com>
 * @version 1
 * @since 1
 */

class NotAnValidDNAFASTA extends Exception {

    /**
     * Custom exception.
     */
    public NotAnValidDNAFASTA() {

        super();

    }

    public NotAnValidDNAFASTA(String err) {
        super("This is not an valid DNA sequence");

    }
}

