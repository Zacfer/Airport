import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Glossary I/O
 * 
 * @author 2011SEGgp04 - Nafiseh, David
 *
 */
public class GlossaryIO {

	public static String[][] readGlossary(String fileName) throws IOException{
		//Create a reader to read from the file
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		int noEntries = Integer.valueOf(reader.readLine());
		String[][] entries = new String[2][noEntries];
		
		for(int i=0; i<noEntries; i++){
			entries[0][i] = reader.readLine();
			entries[1][i] = reader.readLine();
		}
		
		return entries;
	}
}
