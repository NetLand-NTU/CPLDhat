package ld_split_graphy_interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Preprosessing
{
  ArrayList<String> hapSeq = new ArrayList();

  ArrayList<Float> snpPos = new ArrayList();

  public boolean loadSnp(String sitesFileName, String locsFileName)
    throws FileNotFoundException
  {
    File sitesFile = new File(sitesFileName);
    Scanner scanner = new Scanner(new FileReader(sitesFile));
    int numSeq = 0;
    try {
      if (scanner.hasNext()) {
        String line = scanner.nextLine();
        String[] items = line.split("\\s+");
        numSeq = Integer.parseInt(items[0]);
      }

      int seqCount = 0;
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        if (!line.startsWith(">")) {
          this.hapSeq.add(line);
          seqCount++;
        }
        assert (seqCount == numSeq);
      }
    } finally {
      scanner.close();
    }

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
      assert(numSnp == snpCount);
    }
    finally { scanner.close(); } scanner.close();

    Iterator itr = this.hapSeq.iterator();
    while (itr.hasNext()) {
      String hap = (String)itr.next();

      if (hap.length() != this.snpPos.size()) {
        System.out.println("Haplotype length unequal to number of SNP");
        return false;
      }
    }
    return true;
  }

  public void Transformation() throws FileNotFoundException
  {
    test_snp();
    for (int i = 0; i < this.snpPos.size(); i++)
      splitPopu(i);
  }

  public void test_snp()
  {
    System.out.println("number of haplotypes: " + this.hapSeq.size());
    for (int i = 0; i < this.hapSeq.size(); i++) {
      System.out.println(">Seq" + String.valueOf(i));
      System.out.println((String)this.hapSeq.get(i));
    }

    System.out.println("SNP locations: ");
    for (int i = 0; i < this.snpPos.size(); i++)
      System.out.println(((Float)this.snpPos.get(i)).toString());
  }

  private void splitPopu(int snpId)
  {
    HashMap SNIP_Percentage_Statistics = new HashMap();
    for (int i = 0; i < this.hapSeq.size(); i++) {
      String hap = (String)this.hapSeq.get(i);
      char[] charsequence = hap.toCharArray();
      char allele = hap.charAt(snpId);
      if ((allele != '0') && (allele != '1'))
      {
        if ((allele == 'N') || (allele == '?')) {
          Random generator = new Random();
          charsequence[snpId] = Character.forDigit(generator.nextInt(2), 10);
        } else {
          if (SNIP_Percentage_Statistics.get(Character.valueOf(charsequence[snpId])) == null)
            SNIP_Percentage_Statistics.put(Character.valueOf(charsequence[snpId]), Integer.valueOf(0));
          Integer count = (Integer)SNIP_Percentage_Statistics.get(Character.valueOf(charsequence[snpId]));
          count = Integer.valueOf(count.intValue() + 1);
          SNIP_Percentage_Statistics.put(Character.valueOf(charsequence[snpId]), count);
        }
      }

      hap = String.valueOf(charsequence);
      this.hapSeq.set(i, hap);
    }

    System.out.println("dasdsadadadad");

    if ((SNIP_Percentage_Statistics.size() == 2) || (SNIP_Percentage_Statistics.size() == 1)) {
      Iterator Iter = SNIP_Percentage_Statistics.keySet().iterator();
      Character FirstKey = (Character)Iter.next();
      for (int i = 0; i < this.hapSeq.size(); i++) {
        String hap = (String)this.hapSeq.get(i);
        char[] charsequence = hap.toCharArray();
        char allele = hap.charAt(snpId);
        if ((allele != '0') && (allele != '1')) {
          if (allele == FirstKey.charValue()) {
            charsequence[snpId] = '0';
          }
          else {
            charsequence[snpId] = '1';
          }
        }
        hap = String.valueOf(charsequence);
        this.hapSeq.set(i, hap);
      }
    }
    else {
      Iterator Iter = SNIP_Percentage_Statistics.keySet().iterator();
      double Minimal_Percentage = 1.0D;
      Character Minimal_Char = Character.valueOf(' ');

      while (Iter.hasNext()) {
        Character key = (Character)Iter.next();
        Integer value = (Integer)SNIP_Percentage_Statistics.get(key);
        if (value.intValue() / this.hapSeq.size() < Minimal_Percentage) {
          Minimal_Percentage = value.intValue() / this.hapSeq.size();
          Minimal_Char = key;
        }
      }

      if (Minimal_Percentage > 0.05D) {
        for (int i = 0; i < this.hapSeq.size(); i++) {
          String hap = (String)this.hapSeq.get(i);
          char[] charsequence = hap.toCharArray();
          charsequence[snpId] = '0';
          hap = String.valueOf(charsequence);
          this.hapSeq.set(i, hap);
        }
      } else {
        int j = 0;
        while ((((String)this.hapSeq.get(j)).charAt(snpId) == '0') || (((String)this.hapSeq.get(j)).charAt(snpId) == '1') || 
          (((String)this.hapSeq.get(j)).charAt(snpId) == Minimal_Char.charValue())) {
          j++;
        }
        Character First_Char = Character.valueOf(((String)this.hapSeq.get(j)).charAt(snpId));

        for (int i = 0; i < this.hapSeq.size(); i++) {
          String hap = (String)this.hapSeq.get(i);
          char[] charsequence = hap.toCharArray();
          if ((charsequence[snpId] != '0') && (charsequence[snpId] != '1')) {
            if (charsequence[snpId] == Minimal_Char.charValue()) {
              Random generator = new Random();
              charsequence[snpId] = Character.forDigit(generator.nextInt(2), 10);
            }
            else if (charsequence[snpId] == First_Char.charValue()) {
              charsequence[snpId] = '1';
            } else {
              charsequence[snpId] = '0';
            }
          }
          hap = String.valueOf(charsequence);
          this.hapSeq.set(i, hap);
        }
      }

    }

    System.out.println("finishing....");
    test_snp();
  }
}