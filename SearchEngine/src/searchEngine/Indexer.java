package searchEngine;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.opencsv.CSVReader;

public class Indexer {
	
	private IndexWriter writer;

	public Indexer(String indexDirectoryPath) throws IOException
	{
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath)); // Add the path of the index folder.

		StandardAnalyzer analyzer = new StandardAnalyzer();
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		writer = new IndexWriter(indexDirectory, config);
	}
	
	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}
	
	private Document getDocument(String title,String content,int date) throws IOException {
		
		Document document = new Document();

		TextField contentField = new TextField("contents",content,TextField.Store.YES);

		TextField titleField = new TextField("title",title,TextField.Store.YES);
		
		NumericDocValuesField dateField = new NumericDocValuesField("date",date);
		

		document.add(contentField);
		document.add(titleField);
		document.add(dateField);

		return document;
	}

	public int createIndex(String dataDir) throws IOException {
		int articleCounter = 0;
		String dataPath = dataDir + "\\covid_19_articles.csv";
		
		try {
			CSVReader reader = new CSVReader(new FileReader(dataPath));
			String[] nextLine;
			
			while((nextLine = reader.readNext()) != null)
			{
				if (nextLine != null)
				{
					//String author = nextLine[0];
					String date = nextLine[1];
					//String domain = nextLine[2];
					String title = nextLine[3];
					//String url = nextLine[4];
					String content = nextLine[5];
					//String topic = nextLine[6];

					String dateString[] = date.split("/");
					
					if (dateString[1].length() == 1){
						dateString[1] = "0" + dateString[1]; // If the month is between 1 and 9, convert it to 01 or 09.
					}
					
					String dateCombined = dateString[2] + dateString[1] + dateString[0]; 
					
					int dateNum = Integer.parseInt(dateCombined);
					
					Document document = getDocument(title,content,dateNum);
					writer.addDocument(document);
					articleCounter++;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return articleCounter;
	}
}
