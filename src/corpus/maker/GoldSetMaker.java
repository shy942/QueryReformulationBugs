package corpus.maker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import BugLocator.SourceFileExistTest;
import utility.ContentLoader;
import utility.ContentWriter;

public class GoldSetMaker {
	HashMap<String,ArrayList<String>> gitInfoMap;
	ArrayList<String> listFromSFolder;
	ArrayList<String> listFromProS;
	
	
	public GoldSetMaker()
	{
		this.gitInfoMap=new HashMap<>();
		this.listFromSFolder=new ArrayList<>();
		this.listFromProS=new ArrayList<>();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String gitPath="./data/gitInfoFile2.txt";
		GoldSetMaker maker=new GoldSetMaker();
		maker.goldSetMaker(gitPath);
	}

	
	
	public void goldSetMaker(String gitFilePath)
	{
		ArrayList <String> writeContent=new ArrayList<>();
		this.loadFilesAndLists(gitFilePath);
		
		boolean String;
		for(String bugID:this.gitInfoMap.keySet())
		{
			ArrayList<String> listS=gitInfoMap.get(bugID);
			ArrayList<String> tempList=new ArrayList<>();
			for(String sourcePath:listS)
			{
				if(listFromProS.contains(sourcePath)&&listFromSFolder.contains(sourcePath))
				{
					tempList.add(sourcePath);
				}
			}
			if(tempList.size()>0)
			{
				writeContent.add(bugID+" "+tempList.size());
				for(int i=0;i<tempList.size();i++)
					
				{
					writeContent.add(tempList.get(i));
				}
			}
		}
		
		ContentWriter.writeContent("./data/gitInfoNew.txt", writeContent);
	}
	
	public void loadFilesAndLists(String gitFilePath)
	{
		
		ArrayList <String> gitContent=ContentLoader.getAllLinesList(gitFilePath);
		//Create a hushMap to contain git information for easier further processing 
		for(int i=0;i<gitContent.size();i++)
		{
			 ArrayList <String> sourceList=new ArrayList<String>();
			 
			 String[] spilter=gitContent.get(i).split("\\s+");
			 String bugID=spilter[0];
			 int nnoOfFixFiles=Integer.valueOf(spilter[1]);
			 
			 for(int j=i+1;j<=i+nnoOfFixFiles;j++)
			 {
				 String sourceFile=gitContent.get(j);
				 int index = nthOccurrence(sourceFile, '/', 3);
				    
				 sourceFile=sourceFile.substring(index+1, sourceFile.length());
				 sourceFile=sourceFile.replaceAll("/", ".");
				 System.out.println(sourceFile);
				 sourceList.add(sourceFile);
			 }
			 gitInfoMap.put(bugID, sourceList); 
			 i=i+nnoOfFixFiles;
		 }
		
		
		String folderPath="/Users/user/Downloads/buglocator1/Src/eclipse.platform.ui-master";
		SourceFileExistTest objS1=new SourceFileExistTest(folderPath);
		File currentDir = new File(folderPath); // current directory
		objS1.displayDirectoryContents(currentDir,objS1,1);
	    this.listFromSFolder=objS1.returnList();
		 
		String folderPath2="./data/processed";
		SourceFileExistTest objS2=new SourceFileExistTest(folderPath2);
		File currentDir2 = new File(folderPath2); // current directory
		objS2.displayDirectoryContents(currentDir2,objS2,0);
		this.listFromProS=objS2.returnList();
		 
		 
		 System.out.println("========================"+listFromSFolder);
		 System.out.println("========================"+listFromProS);
	}
	
	
	public static int nthOccurrence(String s, char c, int occurrence) {
	    return nthOccurrence(s, 0, c, 0, occurrence);
	}

	public static int nthOccurrence(String s, int from, char c, int curr, int expected) {
	    final int index = s.indexOf(c, from);
	    if(index == -1) return -1;
	    return (curr + 1 == expected) ? index : 
	        nthOccurrence(s, index + 1, c, curr + 1, expected);
	}
}