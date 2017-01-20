import com.ampl.*;
import com.sun.xml.internal.rngom.digested.DDataPattern;

import java.io.IOException;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Test {

    public static void main(String[] args){

        AMPL ampl  = new AMPL();
        try {
            ampl.readData("../pedibus_10.dat");
            Parameter alpha =ampl.getParameter("alpha");
            System.out.println(alpha);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
