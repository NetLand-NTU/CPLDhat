package ld_split_graphy_interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class generateVisualFile {
	private static HotspotWindow hotspotWindow_Run;
	
	
	public generateVisualFile(){
		
		
	}
	
	private void parseRho(String infileName) throws FileNotFoundException {
		pRinFile = new File(runDir, infileName);
		Scanner scanner = new Scanner(new FileReader(pRinFile));
		try
		{
			String head = scanner.nextLine();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				String[] items = line.split("\\s+");
				this.meanRhoList.add(Float.valueOf(items[2]));
			}
		} finally {
			scanner.close();
		}

		this.meanRhoList.remove(0);
	}
	
	public static void main(String[] args) throws FileNotFoundException {		
		hotspotWindow_Run = new HotspotWindow();
		
		//read command line
		Scanner sc=new Scanner(System.in);
		System.out.println("Please input the path of sitesFile：");
		String sitesFile=sc.next();
		System.out.println("Please input the path of locsFile：");
		String locsFile=sc.next();
		System.out.println("Please input the path of statResultFile：");
		String statResultFile=sc.next();

		
		if ( !hotspotWindow_Run.loadSnp(sitesFile, locsFile)) {
			System.out.print("Error in reading sitesFile or locFile!\n");
			return;
		}

		if (!hotspotWindow_Run.test_snp()) {
			ParameterChecking.ErrorInforming("Input sequence number should be more than 20 and less than 192");
			return;
		}

	
		hotspotWindow_Run.WholeRhoSnp(ConfigureContainer.GetSitesFile(), ConfigureContainer.GetLocsFile(), ConfigureContainer.GetLkFile(), 1);

		
	}
}
