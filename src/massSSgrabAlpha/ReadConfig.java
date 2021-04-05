package massSSgrabAlpha;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class responsible for reading a text file containing all the 'queries' we 
 * would like to run on the Oracle server.
 * @author op7ic
 */
public class ReadConfig {    
    
    private BufferedReader reader= null;
    private Vector queries = null;

    public Vector getQueries()
    {
        return queries;
    }
   
    public ReadConfig(String filename)
    {
        queries = new <String> Vector();
        try
        {   
            reader = new BufferedReader(new FileReader(filename));         
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            queries.add(line);
            while(line != null)
            {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
                queries.add(line);
            }
        } catch (Exception ex)
        {
            Logger.getLogger(ReadConfig.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("[!] Unable to parse file config " + filename);
        }
    }

}
