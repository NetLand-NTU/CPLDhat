/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ld_split_graphy_interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yang Peng
 */
public class TranformationFormatOfSnip
{

    static ArrayList<String> hapSeq = new ArrayList<String>();
    static ArrayList<Float> snpPos = new ArrayList<Float>(); // in unit of kb
    static ArrayList<String> newhapSeq = new ArrayList<String>();

    public static boolean loadSnp(String sitesFileName, String locsFileName) throws FileNotFoundException
    {

	File sitesFile = new File(sitesFileName);
	Scanner scanner = new Scanner(new FileReader(sitesFile));
	int numSeq = 0;
        int numSNIP = 0;
	try {
		if(scanner.hasNext()) {
		String line = scanner.nextLine();
		String[] items = line.split("\\s+");
		numSeq = Integer.parseInt(items[0]);
                numSNIP = Integer.parseInt(items[1]);
                System.out.println("numSeq: "+numSeq);
		}

		int seqCount = 0;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if ( line.startsWith(">") == false ) { // not a headline
				hapSeq.add(line);
				seqCount ++;
                        }
		}
                System.out.println("seqCount: "+seqCount);
                if(seqCount != numSeq){
                         ParameterChecking.ErrorInforming("number of Permutation unequal to annotation in site file");
                         return false;
                }
                assert(seqCount == numSeq);

	} finally {
		scanner.close();
	}

	// parse locs file
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
      		    snpPos.add(new Float(line));
		    snpCount ++;
		}
                if(numSnp != snpCount){
                        ParameterChecking.ErrorInforming("number of SNIP unequal to annotation in locs file");
                        return false;
                 }
                System.out.println("numSNIP: "+snpCount);
                if(numSNIP != snpCount){
                        ParameterChecking.ErrorInforming("number of SNIP in locs file unequal to one in site file");
                        return false;
                }
                assert(numSnp == snpCount);

        } finally {
		scanner.close();
	}

	// check consistency of data
	Iterator<String> itr = hapSeq.iterator();
        while (itr.hasNext()) {
		String hap = (String)itr.next();
		if (hap.length() != snpPos.size()) {
			ParameterChecking.ErrorInforming("Haplotype length unequal to number of SNP");
                        System.out.println("Haplotype length unequal to number of SNP");
			return false;
		}
	}
	return true;
    }

    
    public static void Transformation()
    {
	
        for (int i=0; i < snpPos.size(); ++i) {

        Map<Character, Integer> IndexWord = new HashMap<Character, Integer>();
        
        for (int j = 0; j < hapSeq.size(); ++j) {
		String hap = hapSeq.get(j);
                if(IndexWord.containsKey(hap.charAt(i)) == false)
                    IndexWord.put(hap.charAt(i), 0);
                Integer value = IndexWord.get(hap.charAt(i));
                IndexWord.put(hap.charAt(i), value+1);
        }



        char SNIPone = 0, SNIPtwo = 0;
        int SNIPoneCount = 0, SNIPtwoCount = 0;
        Iterator SnipIter = IndexWord.entrySet().iterator();
        while(SnipIter.hasNext()){
            Entry  entry=(Entry)SnipIter.next();
            Integer count = (Integer) entry.getValue();
            System.out.println(entry.getKey()+" "+ count+"//////////////////////////////////////");
            if(count > SNIPoneCount){
                SNIPtwoCount = SNIPoneCount;
                SNIPoneCount = count;
                SNIPtwo = SNIPone;
                SNIPone = (Character)entry.getKey();
            }else{
                if(count > SNIPtwoCount){
                    SNIPtwoCount = count;
                    SNIPtwo = (Character)entry.getKey();
                }
            }
                
        }
        
        for (int j = 0; j < hapSeq.size(); ++j) {
                String hap = hapSeq.get(j);
                char targetchar = hap.charAt(i);
                if ( SNIPone == targetchar ){
                     char [] charArray = hap.toCharArray();
                     charArray[i] = '1';
                     hap = new String(charArray);
                }else{
                    if(SNIPtwo == targetchar){
                        char [] charArray = hap.toCharArray();
                        charArray[i] = '0';
                        hap = new String(charArray);
                    }else{
                        Random rand = new Random();
                        char RandomChar = '1';
                        int RandomNumber =rand.nextInt()%2;
                        if(RandomNumber == 1)
                            RandomChar = '1';
                        else
                            RandomChar = '0';
                        char [] charArray = hap.toCharArray();
                        charArray[i] = RandomChar;
                        hap = new String(charArray);
                    }
                }
                hapSeq.set(j, hap);
        }

        IndexWord.clear();

      }
   }

    public static boolean test_snp()
	{
		System.out.println("number of haplotypes: " + hapSeq.size());
		if(hapSeq.size() < 20 || hapSeq.size() > 192)  return false;

                for (int i = 0; i < hapSeq.size(); ++i) {
			System.out.println(hapSeq.get(i));
		}

                return true;
	}
}
