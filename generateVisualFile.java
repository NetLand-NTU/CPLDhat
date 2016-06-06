import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import ld_split_graphy_interface.HotspotWindow;

public class generateVisualFile {
	private static HotspotWindow hotspotWindow_Run;
	
	private static ArrayList<Float> ReadStatFile(String statResultFile){
		ArrayList<Float> output = new ArrayList<Float>();
		
		File pRinFile = new File(statResultFile);
		Scanner scanner;
		try {
			scanner = new Scanner(new FileReader(pRinFile));
		} catch (FileNotFoundException e) {
			System.out.print("Error: cannot read the stat result!");
			return null;
		}
		try
		{
			String head = scanner.nextLine();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				String[] items = line.split("\\s+");
				output.add(Float.valueOf(items[2]));
			}
		} finally {
			scanner.close();
		}

		output.remove(0);
		return output;
	}
	
	public static void main(String[] args) throws FileNotFoundException {		
		hotspotWindow_Run = new HotspotWindow();
		
		//read command line
		Scanner sc=new Scanner(System.in);
		System.out.println("Please input the path of sitesFile:");
		String sitesFile=sc.nextLine();
		System.out.println("Please input the path of locsFile:");
		String locsFile=sc.nextLine();
		System.out.println("Please input the path of statResultFile:");
		String statResultFile=sc.nextLine();
		System.out.println("Please input the path of outputFile:");
		String outputFile=sc.nextLine();

		
		if ( !hotspotWindow_Run.loadSnp(sitesFile, locsFile)) {
			System.out.print("Error in reading sitesFile or locFile!\n");
			return;
		}

		if (!hotspotWindow_Run.test_snp()) {
			System.out.print("Input sequence number should be more than 20 and less than 192");
			return;
		}

		ArrayList<Float> whole = ReadStatFile(statResultFile);
	
		if( whole != null ){
			hotspotWindow_Run.SetWholeRhoMap(whole);
		
			//write file
			try
			{
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(outputFile.toString()));
				output.writeObject(hotspotWindow_Run);
				output.close();
				System.out.print("Done!");
			}
			catch (IOException ioException)
			{
				System.err.println("Error opening or closing file " + outputFile.toString());
			}
		}
		else{
			System.err.println("Error reading file " + statResultFile.toString());
		}
	}
}
