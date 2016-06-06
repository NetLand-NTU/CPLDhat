package ld_split_graphy_interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class HotspotWindow implements Serializable
{
	private ArrayList<String> hapSeq = new ArrayList();
	private ArrayList<Float> snpPos = new ArrayList();

	private ArrayList<Float> wholeRhoMap = new ArrayList();

	public ArrayList<Float> GetWholeRhoMap()
	{
		return this.wholeRhoMap;
	}

	public ArrayList<Float> GetSNPMAF()
	{
		return this.snpPos;
	}

	public boolean loadSnp(String sitesFileName, String locsFileName) throws FileNotFoundException
	{
		File sitesFile = new File(sitesFileName);
		Scanner scanner = new Scanner(new FileReader(sitesFile));
		int numSeq = 0;
		int numSNIP = 0;
		try {
			if (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] items = line.split("\\s+");
				numSeq = Integer.parseInt(items[0]);
				numSNIP = Integer.parseInt(items[1]);
				System.out.println("numSeq: " + numSeq);
			}

			int seqCount = 0;
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (!line.startsWith(">")) {
					this.hapSeq.add(line);
					seqCount++;
				}
			}
			System.out.println("seqCount: " + seqCount);
			if (seqCount != numSeq) {
				ParameterChecking.ErrorInforming("number of Permutation unequal to annotation in site file");
				return false;
			}
			assert(seqCount == numSeq);
		}
		finally
		{
			scanner.close(); } scanner.close();

			File locsFile = new File(locsFileName);
			scanner = new Scanner(new FileReader(locsFile));
			try {
				int numSnp = 0;
				if (scanner.hasNext()) {
					String line = scanner.nextLine();
					String[] items = line.split("\\s+");
					numSnp = Integer.parseInt(items[0]);
				}

				int snpCount = 0;
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					this.snpPos.add(new Float(line));
					snpCount++;
				}
				if (numSnp != snpCount){
					ParameterChecking.ErrorInforming("number of SNP unequal to annotation in locs file"); 
					return false;
				}

				System.out.println("numSNIP: " + snpCount);
				if (numSNIP != snpCount){
					ParameterChecking.ErrorInforming("number of SNP in locs file unequal to one in site file");
					return false;
				}

				assert(numSnp == snpCount);
			}
			finally { scanner.close(); } scanner.close();

			Iterator itr = this.hapSeq.iterator();
			while (itr.hasNext()) {
				String hap = (String)itr.next();

				if (hap.length() != this.snpPos.size()) {
					ParameterChecking.ErrorInforming("Haplotype length unequal to number of SNP");
					System.out.println("Haplotype length unequal to number of SNP");
					return false;
				}
			}
			return true;
	}

	public boolean test_snp()
	{
		System.out.println("number of haplotypes: " + this.hapSeq.size());
		if ((this.hapSeq.size() < 20) || (this.hapSeq.size() > 192)) return false;

		for (int i = 0; i < this.hapSeq.size(); i++) {
			System.out.println((String)this.hapSeq.get(i));
		}

		System.out.println("SNP locations: ");
		for (int i = 0; i < this.snpPos.size(); i++) {
			System.out.println(((Float)this.snpPos.get(i)).toString());
		}

		return true;
	}

	private void splitPopu(int snpId, ArrayList<String> hap0, ArrayList<String> hap1)
	{
		char OneType = ((String)this.hapSeq.get(0)).charAt(snpId);
		for (int i = 0; i < this.hapSeq.size(); i++) {
			String hap = (String)this.hapSeq.get(i);
			char allele = hap.charAt(snpId);
			if (allele == '0')
				hap0.add(hap);
			else if (allele == '1') {
				hap1.add(hap);
			}
			if ((allele != '0') && (allele != '1'))
			{
				if (allele == OneType)
					hap0.add(hap);
				else
					hap1.add(hap);
			}
		}
	}

	private void splitPopuRandom(int snpId, ArrayList<String> hap0, ArrayList<String> hap1)
	{
		ArrayList RandomList = new ArrayList();
		Random generator = new Random();
		Integer scalenumber = new Integer(this.hapSeq.size());

		while (RandomList.size() < scalenumber.intValue()) {
			Integer randomInt = new Integer(generator.nextInt(this.hapSeq.size()));
			if (!RandomList.contains(randomInt)) {
				RandomList.add(randomInt);
			}
		}

		Integer SplitNum = new Integer(generator.nextInt((int)(this.hapSeq.size() * 0.4D)) + (int)(this.hapSeq.size() * 0.3D));

		Integer RandomIndex = new Integer(0);
		while (RandomIndex.intValue() < SplitNum.intValue()) {
			Integer HapMapIndex = (Integer)RandomList.get(RandomIndex.intValue());
			hap0.add((String)this.hapSeq.get(HapMapIndex.intValue()));
			RandomIndex = Integer.valueOf(RandomIndex.intValue() + 1);
		}

		while (RandomIndex.intValue() < scalenumber.intValue()) {
			Integer HapMapIndex = (Integer)RandomList.get(RandomIndex.intValue());
			hap1.add((String)this.hapSeq.get(HapMapIndex.intValue()));
			RandomIndex = Integer.valueOf(RandomIndex.intValue() + 1);
		}
	}

	public ArrayList<Float> subRhomap(ArrayList<String> subHap, String subSiteFile, String subLocFile, String subLkFile, int flag)
	{
		Random generator = new Random();
		Integer randInt = new Integer(generator.nextInt());
		String uniqueSuffix = randInt.toString();
		try
		{
			exportLDhatData(subHap, this.snpPos, subSiteFile, subLocFile, subLkFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("runldhat start");

		if (flag == 0) {
			RunLDhat mLDhat_runner = new RunLDhat(subSiteFile, subLocFile, subLkFile);
			mLDhat_runner.run();
			System.out.println("runldhat end");

			return mLDhat_runner.getMeanRhoList();
		}
		if (flag == 1) {
			RunLDhat1 mLDhat_runner = new RunLDhat1(subSiteFile, subLocFile, subLkFile);
			mLDhat_runner.run();
			System.out.println("runldhat1 end");

			return mLDhat_runner.getMeanRhoList();
		}

		RunLDhat mLDhat_runner = new RunLDhat(subSiteFile, subLocFile, subLkFile);
		mLDhat_runner.run();
		System.out.println("runldhat2 end");

		return mLDhat_runner.getMeanRhoList();
	}

	public void exportLDhatData(ArrayList<String> hapSeq, ArrayList<Float> snpPos, String newSiteFile, String newLocFile, String newLkFile)
			throws FileNotFoundException, IOException
			{
			}

	public void WholeRhoSnp(String sitefile, String locfile, String lkfile, int flag)
	{
		ArrayList hap0 = new ArrayList();

		this.wholeRhoMap = subRhomap(hap0, sitefile, locfile, lkfile, flag);

		System.out.println("wholeRhoMap: " + this.wholeRhoMap.size());
		Iterator iterator = this.wholeRhoMap.iterator();
		while (iterator.hasNext())
		{
			System.out.print(iterator.next() + " ");
		}
		System.out.println();
	}

	public void SetWholeRhoMap(ArrayList<Float> whole)
	{
		this.wholeRhoMap = whole;
	}

	public void SetSNPPos(ArrayList<Float> pos)
	{
		this.snpPos = pos;
	}
}