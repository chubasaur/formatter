package utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

/** This class is called by runTBFormatter and converts .dat files output by
 *  UCSF's tb-analysis program into the format specified by Lauren in her
 *  excel spreadsheet.
 *  @author Paul Wang
 */
public class Preformatter {
    private Scanner _scanner;

    /*Formatter expects to receive a reader that has 
     * P    Type    Timeline    Image-file   Trabecular-ROI-file      
     * Slice-#    ROI-#   FuzzyBVF-[fraction]      TbTh-[mm]      
     * TbSp-[mm]   TbN-[1/mm]         E#  Gender  Age P
     * as fields
     * WARNING: INPUT does not provide E# Gender Age P fields
     * If you add additional patients to the study, you will need to
     * find this information elsewhere
     */
    public Preformatter(BufferedReader reader) {
        _scanner = new Scanner(reader);
    }

    
}
