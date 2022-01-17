import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadWords {
	
	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		long start2 = System.currentTimeMillis();
		Map<String,String> variableMap = getExcelDataAsMap("french_dictionary","Sheet1");
		Path path = Paths.get("C:\\Users\\user\\Downloads\\TranslateWords Challenge\\t8.shakespeare.txt");
		Path newFilePath = Paths.get("C:\\Users\\user\\Downloads\\TranslateWords Challenge\\t8.shakespeare.translated.txt");
		Stream<String> lines;
		try {
			lines = Files.lines(path,Charset.forName("UTF-8"));
			List<String> replacedLines = lines.map(line -> replaceTag(line,variableMap))
                    .collect(Collectors.toList());
			Files.write(newFilePath, replacedLines, Charset.forName("UTF-8"));
			System.out.println("Find and replace done");
			Map<String,Integer> words=new HashMap<String, Integer>();
			ReadWords rw = new ReadWords();
			rw.CountWords("C:\\Users\\user\\Downloads\\TranslateWords Challenge\\t8.shakespeare.translated.txt",words);
			File file = new File("C:\\Users\\user\\Downloads\\TranslateWords Challenge\\frequency.txt");
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
			for (Map.Entry<String, Integer> entry :
                words.entrySet()) {
 
               // put key and value separated by a colon
               bf.write(entry.getKey() + ":"
                        + entry.getValue());
 
               // new line
               bf.newLine();
           }
			bf.flush();
			bf.close();
	           lines.close();
		      long end2 = System.currentTimeMillis();
		      System.out.println("Elapsed Time in milli seconds: "+ (end2-start2));
		      long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		      long actualMemUsed=afterUsedMem-beforeUsedMem;
		      System.out.println("Memory used is: "+ actualMemUsed);
		      FileWriter fWriter1 = new FileWriter(
		                "C:\\Users\\user\\Downloads\\TranslateWords Challenge\\performance.txt");
		            fWriter1.write((int) (end2-start2));
		            fWriter1.write((int) actualMemUsed); 
		            fWriter1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static LinkedHashMap<String, String> getExcelDataAsMap(String excelFileName, String sheetName) throws EncryptedDocumentException, IOException, InvalidFormatException {
		// Create a Workbook
		Workbook wb = WorkbookFactory.create(new File("C:\\Users\\user\\Downloads\\TranslateWords Challenge\\"+excelFileName+".xlsx"));
		// Get sheet with the given name "Sheet1"
		Sheet s = wb.getSheet(sheetName);
		// Initialized an empty LinkedHashMap which retain order
		LinkedHashMap<String, String> data = new LinkedHashMap<>();
		// Get total row count
		int rowCount = s.getPhysicalNumberOfRows();
		// Skipping first row as it contains headers
		for (int i = 1; i < rowCount; i++) {
			// Get the row
			Row r = s.getRow(i);
			// Since every row has two cells, first is field name and another is value.
			String fieldName = r.getCell(0).getStringCellValue();
			String fieldValue = r.getCell(1).getStringCellValue();
			data.put(fieldName, fieldValue);
		}
		return data;
	}
	private static String replaceTag(String str, Map<String,String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (str.contains(entry.getKey())) {
				str = str.replace(entry.getKey(), entry.getValue());
			}
		}
		return str;
	}
	
	void CountWords(String filename, Map< String, Integer> words) throws FileNotFoundException
	{
	Scanner file=new Scanner (new File(filename));
	while(file.hasNext()){
	String word=file.next();
	Integer count=words.get(word);
	if(count!=null)
	count++;
	else
	count=1;
	words.put(word,count);
	}
	file.close();
	}

}
