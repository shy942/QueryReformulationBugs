package changeset.maker;

import java.util.ArrayList;
import utility.ContentLoader;
import utility.ContentWriter;

public class ChangesetMaker {

	String gitInfoFile;
	String changesetDir;

	public ChangesetMaker(String gitInfoFile) {
		this.gitInfoFile = gitInfoFile;
	}

	protected void makeChangeset() {
		ArrayList<String> fileList=ContentLoader.readContent("E:\\PhD\\Repo\\Eclipse\\data\\SourceFileNames.txt");
		ArrayList<String> lines = ContentLoader
				.getAllLinesOptList(this.gitInfoFile);
		
		for (int i = 0; i < lines.size();) {
			System.out.println(lines.get(i));
			String currentLine = lines.get(i);
			if (currentLine.matches("\\d+\\s+\\d+")) {
				String[] parts = currentLine.split("\\s+");
				int bugID = Integer.parseInt(parts[0].trim());
				int cfCount = Integer.parseInt(parts[1].trim());
				if (cfCount > 0) {
					ArrayList<String> cfiles = new ArrayList<>();
					ArrayList<String> tempList=new ArrayList<>();
					for (int j = i + 1; j <= i + cfCount; j++) {
						String fileURL = lines.get(j);
						cfiles.add(fileURL);
						if(IsFileExist(fileURL, fileList)){
							tempList.add(fileURL);
						}
					}
					if(tempList.size()==cfiles.size())
					{
					// now save the files
					//String outputFile = "/Users/user/Documents/Ph.D/2018/Data/changeset/" + bugID + ".txt";
					String outputFile = "E:\\PhD\\Repo\\Eclipse\\data\\changeset\\" + bugID + ".txt";
					ContentWriter.writeContent(outputFile, cfiles);
					}

					i = i + cfCount + 1;
					System.out.println("Done:" + bugID);
				} else {
					i++;
				}
			} else {
				break;
			}
		}
	}

	
	protected boolean IsFileExist(String file, ArrayList<String> list)
	{
		if(list.contains(file)) return true;
		else return false;
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String gitInfoFile = "E:\\PhD\\Repo\\Eclipse\\data\\gitInfoEclipse.txt";
		new ChangesetMaker(gitInfoFile).makeChangeset();
	}
}
