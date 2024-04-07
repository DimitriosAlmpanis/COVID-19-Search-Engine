package searchEngine;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SearchEngine {
	
	String indexDir = System.getProperty("user.dir") + "\\index";
	String dataDir = System.getProperty("user.dir") + "\\data";
	
	String[] globalTitles;
	String[] globalContents;
	
	int newArraySize;
	
	int page; // The current page we are on.
	int maxPage;
	
	boolean searchedFlag = false;
	
	ArrayList<String> history = new ArrayList<>(); // An ArrayList that contains all the previous queries.
	
	Indexer indexer;
	Searcher searcher;

	private JFrame frame; // Main window.
	
	private JFrame articleFrame; // The window that will pop up when an article is clicked.

	private JTextField textField;
	
	JButton btnNewButton_1;
	JButton btnNewButton_2;
	JButton btnNewButton_3;
	JButton btnNewButton_4;
	JButton btnNewButton_5;
	JButton btnNewButton_6;
	JButton btnNewButton_7;
	JButton btnNewButton_8;
	JButton btnNewButton_9;
	JButton btnNewButton_10;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchEngine window = new SearchEngine();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchEngine() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Covid-19 Search Engine");
		frame.setBounds(100, 100, 700, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 11, 288, 23);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		/*try {
			createIndex(); // If we run this function again, duplicates will be created so it is a comment at the moment.
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String queryText = textField.getText();
				
				try {
					search(queryText);
				} catch (IOException er) {
			         er.printStackTrace();
			    } catch (ParseException er) {
			         er.printStackTrace();
			    }
			}
		});
		btnSearch.setBounds(308, 11, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		JButton btnNewButton = new JButton("Search & Sort by Date");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String queryText = textField.getText();
				
				try {
					searchAndSort(queryText);
				} catch (IOException er) {
			         er.printStackTrace();
			    } catch (ParseException er) {
			         er.printStackTrace();
			    }
			}
		});
		btnNewButton.setBounds(407, 11, 168, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnSuggest = new JButton("Suggest");
		btnSuggest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				textField.setText(suggestQuery());
			}
		});
		btnSuggest.setBounds(585, 11, 89, 23);
		frame.getContentPane().add(btnSuggest);
		
		btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(0);
			}
		});
		btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_1.setBounds(0, 60, 684, 25);
		frame.getContentPane().add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(1);
			}
		});
		btnNewButton_2.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_2.setBounds(0, 83, 684, 25);
		frame.getContentPane().add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(2);
			}
		});
		btnNewButton_3.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_3.setBounds(0, 106, 684, 25);
		frame.getContentPane().add(btnNewButton_3);
		
		btnNewButton_4 = new JButton("");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(3);
			}
		});
		btnNewButton_4.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_4.setBounds(0, 129, 684, 25);
		frame.getContentPane().add(btnNewButton_4);
		
		btnNewButton_5 = new JButton("");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(4);
			}
		});
		btnNewButton_5.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_5.setBounds(0, 152, 684, 25);
		frame.getContentPane().add(btnNewButton_5);
		
		btnNewButton_6 = new JButton("");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(5);
			}
		});
		btnNewButton_6.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_6.setBounds(0, 175, 684, 25);
		frame.getContentPane().add(btnNewButton_6);
		
		btnNewButton_7 = new JButton("");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(6);
			}
		});
		btnNewButton_7.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_7.setBounds(0, 198, 684, 25);
		frame.getContentPane().add(btnNewButton_7);
		
		btnNewButton_8 = new JButton("");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(7);
			}
		});
		btnNewButton_8.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_8.setBounds(0, 221, 684, 25);
		frame.getContentPane().add(btnNewButton_8);
		
		btnNewButton_9 = new JButton("");
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(8);
			}
		});
		btnNewButton_9.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_9.setBounds(0, 244, 684, 25);
		frame.getContentPane().add(btnNewButton_9);
		
		btnNewButton_10 = new JButton("");
		btnNewButton_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				showArticle(9);
			}
		});
		btnNewButton_10.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_10.setBounds(0, 267, 684, 25);
		frame.getContentPane().add(btnNewButton_10);
		
		JButton btnPrev = new JButton("Previous");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (page > 0)
				{
					page--;
					loadPage(page);
				}
			}
		});
		btnPrev.setBounds(10, 307, 89, 23);
		frame.getContentPane().add(btnPrev);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (page < maxPage)
				{
					page++;
					loadPage(page);
				}
			}
		});
		btnNext.setBounds(585, 307, 89, 23);
		frame.getContentPane().add(btnNext);
	}
	
	@SuppressWarnings("unused")
	private void createIndex() throws IOException {
	      indexer = new Indexer(indexDir);
	      indexer.createIndex(dataDir);
	      indexer.close();  
	}
	
	private void search(String searchQuery) throws IOException, ParseException {
		   
		searcher = new Searcher(indexDir);
	      
		TopDocs hits = searcher.search(searchQuery);
	      
		maxPage = hits.scoreDocs.length / 10;
		  
		newArraySize = (maxPage * 10) + 10;
	      
		// If there are 24 results, an array of size 30 is created where the 24 first elements are the results and the remaining 6 are empty.
		String[] titles = new String[newArraySize];
		String[] contents = new String[newArraySize];
	      
		int j = 0;
	      
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			titles[j] = doc.get("title");
			contents[j] = doc.get("contents");
			j++;
		}
	      
		for (int i = j; i < newArraySize; i++)
		{
			titles[i] = "";
			contents[i] = "";
		}
	      
		globalTitles = titles;
		globalContents = contents;
	      
		page = 0;
		searchedFlag = true;
		
		addToHistory(searchQuery);
		
		loadPage(page);
	}
	
	private void searchAndSort(String searchQuery) throws IOException, ParseException {   
		searcher = new Searcher(indexDir);
	      
		TopDocs hits = searcher.searchAndSort(searchQuery);
	      
		int nDiv = hits.scoreDocs.length / 10;
		maxPage = nDiv;
	      
		newArraySize = (nDiv * 10) + 10;
	      
		String[] titles = new String[newArraySize];
		String[] contents = new String[newArraySize];
	      
		int j = 0;
	      
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			titles[j] = doc.get("title");
			contents[j] = doc.get("contents");
			j++;
		}
	      
		for (int i = j; i < newArraySize; i++)
		{
			titles[i] = "";
			contents[i] = "";
		}
	      
		globalTitles = titles;
		globalContents = contents;
	      
		page = 0;
		searchedFlag = true;
		
		addToHistory(searchQuery);
		
		loadPage(page);
	}
	
	

	
	private void loadPage(int p)
	{
		// Used to change the text of the buttons that show the results.
		btnNewButton_1.setText(globalTitles[p*10]);
		btnNewButton_2.setText(globalTitles[p*10 + 1]);
		btnNewButton_3.setText(globalTitles[p*10 + 2]);
		btnNewButton_4.setText(globalTitles[p*10 + 3]);
		btnNewButton_5.setText(globalTitles[p*10 + 4]);
		btnNewButton_6.setText(globalTitles[p*10 + 5]);
		btnNewButton_7.setText(globalTitles[p*10 + 6]);
		btnNewButton_8.setText(globalTitles[p*10 + 7]);
		btnNewButton_9.setText(globalTitles[p*10 + 8]);
		btnNewButton_10.setText(globalTitles[p*10 + 9]);
	}
	
	private void showArticle(int b)
	{
		// Used to create a new window, where the article is displayed.
		if (searchedFlag == false) {
			return;
		}
		
		if (globalContents[page*10 + b].equals("")) {
			return;
		}
		EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
            	String text = globalContents[page*10 + b];
            	text = text.replace(".",".\n");
                articleFrame = new JFrame();
                articleFrame.setTitle(globalTitles[page*10 + b]);
                articleFrame.setSize(990,990);
                articleFrame.getContentPane().setLayout(null);
                articleFrame.setVisible(true);
                
                JTextArea articleContents = new JTextArea();
                articleContents.setSize(1000, 1000);
                articleContents.setText(text);
                articleContents.setWrapStyleWord(true);
                articleContents.setLineWrap(true);
                articleContents.setEditable(false);
                
                articleFrame.getContentPane().add(articleContents);
                
            }
        });
	}
	
	private void addToHistory(String queryString)
	{
		boolean existsFlag = false;
		
		for(String hString : history) {
			if(queryString.equals(hString)) 
			{
				existsFlag = true;
			}
		}
		
		if (existsFlag == false)
		{
			history.add(queryString);
		}
	}
	
	private String suggestQuery() 
	{
		if (history.size() == 0) {
			return "";
		}
		
		boolean queryAdded = false;
		String newQuery = "";
		
		Random rand = new Random();
		
		int chance = 100/history.size(); // If there are two elements, the chance is 50%.
		
		while(queryAdded == false) 
		{
			for(String hString : history) 
			{
				int randomNumber = rand.nextInt(100) + 1;
				if (randomNumber <= chance)
				{
					newQuery += " " + hString;
					queryAdded = true;
				}
			}
			
		}
		return newQuery;
	}
}
