package corpus.maker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import stemmer.Stemmer;
import config.StaticData;
import utility.ContentLoader;
import utility.MiscUtility;

public class BugReportPreprocessor {

	String content;
	ArrayList<String> stopwords;
	Stemmer stemmer;

	public BugReportPreprocessor(String content) {
		this.content = content;
		this.stopwords = new ArrayList<String>();
		this.loadStopWords();
		this.stemmer = new Stemmer();
	}

	protected void loadStopWords() {
		this.stopwords = ContentLoader.readContent(".\\data\\stop_words.txt");
	}

	protected ArrayList<String> removeStopWords(ArrayList<String> words) {
		ArrayList<String> refined = new ArrayList<String>(words);
		for (String word : words) {
			if (this.stopwords.contains(word.toLowerCase())) {
				refined.remove(word);
			}
		}
		return refined;
	}

	protected ArrayList<String> splitContent(String content) {
		//System.out.println("Before split: ");
		//System.out.println(content);
		String[] words = content.split("(?=[A-Z])");
		//String[] words = content.split("\\s+|\\p{Punct}+|\\d+");
		//return new ArrayList<String>(Arrays.asList(words));
		ArrayList<String> list =new ArrayList<String>(Arrays.asList(words));
		String content2 = MiscUtility.list2Str(list);
		String[] words2 = content2.split("\\s+|\\p{Punct}+|\\d+");
		//System.out.println("After split: ");
		//System.out.println(new ArrayList<String>(Arrays.asList(words2)));
		return new ArrayList<String>(Arrays.asList(words2));
	}
	
	protected ArrayList<String> splitContentRecursive(String content) {
	    ArrayList<String> outputList=new ArrayList<>();
        //System.out.println("Before split: ");
        //System.out.println(content);
        String[] wordsWithoutSpace = content.split(" ");
        for(int i=0;i<wordsWithoutSpace.length;i++)
        {
            String wordprocessed="";
            String word=wordsWithoutSpace[i];
            String[] wordsRemovePunc = word.split("\\s+|\\p{Punct}+|\\d+");
            for(int j=0;j<wordsRemovePunc.length;j++)
            {
                String eachword=wordsRemovePunc[j];
                System.out.println(eachword);
              
                if(checkForAllUppercase(eachword)==true)
                {
                    //wordprocessed=wordprocessed+" "+eachword.toLowerCase().trim();
                    if(eachword.length()>1)outputList.add(eachword.toLowerCase().trim());
                }
                else{
                     String[] wordprocessed2ndStage=eachword.split("(?=[A-Z])");
                     String check="";
                     for(int k=0;k<wordprocessed2ndStage.length;k++)
                     {
                         String eachword2ndStage=wordprocessed2ndStage[k];
                         if(checkForAllUppercase(eachword2ndStage)==true)
                         {
                             //wordprocessed=eachword2ndStage.toLowerCase();
                             check=check+eachword2ndStage.toLowerCase();
                         }
                         else
                         {
                             if(check.length()>0){
                                 wordprocessed = wordprocessed+" "+check;
                                 if(wordprocessed.length()>1)outputList.add(wordprocessed.trim());
                                 check="";
                             }
                             
                                 wordprocessed= eachword2ndStage.toLowerCase();
                             
                                 if(wordprocessed.length()>1)outputList.add(wordprocessed.trim());
                             
                         }
                     }
                }
                System.out.println(wordprocessed);
            }
        }
       return outputList;
        //return new ArrayList<String>(Arrays.asList(content));
    }

	protected Boolean checkForAllUppercase(String word)
	{
	    String allCapitalWord="";
	    //String[] words = word.split("\\s+|\\p{Punct}+|\\d+");
	    int len=0;
	    //for(int j=0;j<words.length;j++)
	    {
	        //String eachword=words[j];
	        char[] charArray=word.toCharArray();
	      
	        for(int i=0;i<charArray.length;i++)
	        {
	            char ch=charArray[i];
	            if(Character.isUpperCase(ch))len++;
	        }
	        //if(len==word.length()-1) return true;
	    }
	    if(len==word.length()) return true;
	    else
	    return false;
	}

	protected String performStemming(String word) {
		return stemmer.stripAffixes(word);
		//return word;
	}
	
	protected ArrayList<String> splitLines(String content)
	{
		
		String[] contentByLine=content.split("\\r?\\n");
		//return new ArrayList<String>(Arrays.asList(contentByLine));
		ArrayList<String> list =new ArrayList<String>(Arrays.asList(contentByLine));
		String content2 = MiscUtility.list2Str(list);
		String[] words = content2.split("(?=[A-Z])");
		
		return new ArrayList<String>(Arrays.asList(words));
		
	}
	
	public String performNLPforAllContent() {
		// performing NLP operations
		ArrayList<String> str=splitContentRecursive(this.content);
		ArrayList<String> processed = new ArrayList<String>();
		//for (String indLine : lineOfContent) 
		//{
			
		//ArrayList<String> words = splitContent(indLine);
		//ArrayList<String> words = splitContent(this.content);
		ArrayList<String> refined = removeStopWords(str);
		//ArrayList<String> stemmed = new ArrayList<String>();
		int found=0;
		for (String word : refined) {
			if (!word.trim().isEmpty()) {
				/*String stemmedWord = performStemming(word.trim());
				if (stemmedWord.length() >= 3) {
					stemmedWord=stemmedWord.toLowerCase(Locale.ENGLISH);
					stemmedWord=stemmedWord.trim();
					stemmedWord=stemmedWord.replaceAll("�", "");
					stemmedWord=stemmedWord.replaceAll("�", "");
					if(!stemmedWord.isEmpty())
						{
							stemmed.add(stemmedWord.trim());
							found=1;
						}
					}
				}*/
				
				if (word.length() > 2) {
					word=word.toLowerCase(Locale.ENGLISH);
					word=word.trim();
					word=word.replaceAll("�", "");
					word=word.replaceAll("�", "");
					if(!word.isEmpty())
						{
						processed.add(word.trim());
							found=1;
						}
					}
				}
			}
		//if(found>0)stemmed.add("\n");
		//}
		System.out.println(processed);
		return MiscUtility.list2Str(processed);

	}

	public String performNLP() {
		// performing NLP operations
		ArrayList<String> lineOfContent=splitLines(content);
		ArrayList<String> stemmed = new ArrayList<String>();
		for (String indLine : lineOfContent) 
		{
			
		ArrayList<String> words = splitContent(indLine);
		//ArrayList<String> words = splitContent(this.content);
		ArrayList<String> refined = removeStopWords(words);
		//ArrayList<String> stemmed = new ArrayList<String>();
		int found=0;
		for (String word : refined) {
			if (!word.trim().isEmpty()) {
				String stemmedWord = performStemming(word.trim());
				if (stemmedWord.length() >= 3) {
					stemmedWord=stemmedWord.toLowerCase(Locale.ENGLISH);
					stemmedWord=stemmedWord.trim();
					stemmedWord=stemmedWord.replaceAll("�", "");
					stemmedWord=stemmedWord.replaceAll("�", "");
					if(!stemmedWord.isEmpty())
						{
							stemmed.add(stemmedWord.trim());
							found=1;
						}
					}
				}
			}
		if(found>0)stemmed.add("\n");
		}
		return MiscUtility.list2Str(stemmed);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
