package ld_split_graphy_interface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


class StreamCleaner extends Thread
{
	InputStream is;
	String type;

	StreamCleaner(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ( (line = br.readLine()) != null) {
				//System.out.println(type + ">" + line);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}


class RunLDhat
{
  private String exe_dir = null;
  private String rhomap_cmd = null;
  private String lkgen_cmd = null;
  private String stat_cmd = null;
  private String lkFileDir = null;

  private String sitesFile = null;
  private String locsFile = null;
  private String lkFile = null;
  private String Iteration = "10000";
  private String Rhomap_Samp = "10";
  private String Rhomap_Burn = "1000";
  private String Rhomap_bpen = "0";
  private String Rhomap_hpen = "0";

  private ArrayList<Float> meanRhoList = new ArrayList();

  private static File pRinFile = null;
  private static File LkinFile = null;
  private static Process proc = null;
  public static File runDir = null;

  public RunLDhat(String sites_file, String locs_file, String lk_file)
  {
    this.sitesFile = sites_file;
    this.locsFile = locs_file;
    this.lkFile = lk_file;

    if (ConfigureContainer.GetIterationTime().intValue() != 0)
      this.Iteration = Integer.toString(ConfigureContainer.GetIterationTime().intValue());
    if (ConfigureContainer.GetRhomap_Samp().intValue() != 0)
      this.Rhomap_Samp = Integer.toString(ConfigureContainer.GetRhomap_Samp().intValue());
    if (ConfigureContainer.GetRhomap_Burn().intValue() != 0)
      this.Rhomap_Burn = Integer.toString(ConfigureContainer.GetRhomap_Burn().intValue());
    if (ConfigureContainer.GetRhomap_bpen() != 0.0F)
      this.Rhomap_bpen = Float.toString(ConfigureContainer.GetRhomap_bpen());
    if (ConfigureContainer.GetRhomap_hpen() != 0.0F) {
      this.Rhomap_hpen = Float.toString(ConfigureContainer.GetRhomap_hpen());
    }

    initConfigure();

    Random generator = new Random();
    Integer r = new Integer(generator.nextInt());
    runDir = new File("run_" + r.toString());
    System.out.println("run_dir" + r.toString());

    if (!runDir.mkdir()) {
      System.out.println("Fail to create directory" + runDir);
      System.exit(1);
    }
  }

  public static void kissprocess()
  {
    try
    {
      Thread.sleep(500L);
    } catch (InterruptedException ex) {
      Logger.getLogger(RunLDhat.class.getName()).log(Level.SEVERE, null, ex);
    }
    proc.destroy();
  }

  private void initConfigure()
  {
    String osName = System.getProperty("os.name");
    System.out.println("OS: " + osName);

    String path = new File(getClass().getResource("").getPath()).getParentFile().getParentFile().getPath();
    
    path = path.substring(6, path.length());
    path = path.replaceAll("%20"," ");
    path = path.replaceAll("/", "\\\\");
    System.out.println("the path is: "+path);


    //if (osName.equals("Windows NT") || osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows 8")) {
    if(osName.indexOf("win") >= 0 ) {
    	exe_dir ="G:\\program_project\\LD_Split_software\\java_project\\ldhat_exe\\";

    	rhomap_cmd = path + "\\Win32\\rhomap.exe";
    	lkgen_cmd = path + "\\Win32\\lkgen_VS2.exe";
    	stat_cmd = path + "\\Win32\\stat.exe";
    	lkFileDir = path + "\\Lookup\\";
    	String command = lkgen_cmd + " -lk " + lkFileDir + " -nseq ";
    	System.out.println("the command is " + command);


    } //else if (osName.equals("Linux") || osName.equals("Unix")) {
    else if(osName.indexOf("linux")>=0){
    	//exe_dir = "/home/zhengj/project/LDsplit_java/ldhat_exe/";
    	String pathlinux = new File(getClass().getResource("").getPath()).getParentFile().getParentFile().getPath();
    	System.out.println("the route of LDhat is:///////////////////////// "+pathlinux);
    	pathlinux = pathlinux.substring(5, pathlinux.length());
    	pathlinux = pathlinux.replaceAll("%20"," ");
    	System.out.println("the path of LDhat under linux is:///////////////////////// "+pathlinux);
    	exe_dir = "/media/HENRY_YANG/";
    	rhomap_cmd = pathlinux + "/Linux/rhomap";
    	lkgen_cmd = pathlinux + "/Linux/lkgen";
    	stat_cmd = pathlinux + "/Linux/stat";
    	lkFileDir = pathlinux + "/Lookup/";
    } //else if (osName.equals("Mac")) {
    else if(osName.indexOf("mac")>=0) {
    	//exe_dir = "/home/zhengj/project/LDsplit_java/ldhat_exe/";
    	String pathMac = new File(getClass().getResource("").getPath()).getParentFile().getParentFile().getPath();
    	System.out.println("the route of LDhat is:///////////////////////// "+pathMac);
    	pathMac = pathMac.substring(5, pathMac.length());
    	pathMac = pathMac.replaceAll("%20"," ");
    	System.out.println("the path of LDhat under Mac is:///////////////////////// "+pathMac);
    	exe_dir = "/media/HENRY_YANG/";
    	rhomap_cmd = pathMac + "/Mac/rhomap";
    	lkgen_cmd = pathMac + "/Mac/lkgen";
    	stat_cmd = pathMac + "/Mac/stat";
    	lkFileDir = pathMac + "/Lookup/";

    }
    
    
  }

  private static boolean deleteDir(File dir)
  {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        if (!deleteDir(new File(dir, children[i]))) {
          return false;
        }
      }
    }
    return dir.delete();
  }

  private void runRhomap()
  {
    try
    {
      System.out.println("AAAAAA  " + this.lkFile);
      if (this.lkFile.equals("")) {
        System.out.println("calculate lk");
        LkinFile = new File(this.sitesFile);
        Scanner scanner = new Scanner(new FileReader(LkinFile));
        int numSeq = -1;

        if (scanner.hasNext()) {
          String line = scanner.nextLine();
          String[] items = line.split("\\s+");
          numSeq = Integer.parseInt(items[0]);
        } else {
          System.out.println("Cannot read site file!");
        }
        scanner.close();

        System.out.println("haplotypes: " + numSeq);
        String existLkFile = null;
        if (numSeq < 20)
          ParameterChecking.ErrorInforming("Haplotypes should be 20-192!");
        else if (numSeq <= 50)
          existLkFile = "lk_n50_t0.001";
        else if (numSeq <= 100)
          existLkFile = "lk_n100_t0.001";
        else if (numSeq <= 120)
          existLkFile = "lk_n120_t0.001";
        else if (numSeq <= 192)
          existLkFile = "lk_n192_t0.001";
        else {
          ParameterChecking.ErrorInforming("Haplotypes should be 20-192!");
        }

        String command = this.lkgen_cmd + " -lk " + this.lkFileDir.replaceAll(" ", "\" \"") + existLkFile + " -nseq " + numSeq;
        System.out.println("lkgen command is " + command);

        Runtime rt = Runtime.getRuntime();
        proc = rt.exec(command, null, runDir);

        StreamCleaner inputCleaner = new StreamCleaner(proc.getInputStream(), "INPUT");
        StreamCleaner errorCleaner = new StreamCleaner(proc.getErrorStream(), "ERROR");
        errorCleaner.start();
        inputCleaner.start();

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()));
        pw.write("\n");
        pw.flush();

        int exitVal = proc.waitFor();

        String osName = System.getProperty("os.name");
        if ((osName.equals("Windows NT")) || (osName.equals("Windows 7")) || (osName.equals("Windows XP"))) {
          if (exitVal == 0) {
            this.lkFile = (runDir.getAbsolutePath() + "\\new_lk.txt");
            System.out.println("finish lkgen! " + this.lkFile);
          } else {
            System.err.println("Process exitValue in lkgen");
          }
        } else if ((osName.equals("Linux")) || (osName.equals("Unix"))) {
          if (exitVal != 0) {
            this.lkFile = (runDir.getAbsolutePath() + "/new_lk.txt");
            System.out.println("finish lkgen! " + this.lkFile);
          } else {
            System.err.println("Process exitValue in lkgen");
          }
        }
      }

      System.out.println(this.Iteration + "\t" + this.Rhomap_Samp + "\t" + this.Rhomap_Burn + "\t" + this.Rhomap_bpen + "\n" + this.lkFile + "\n");
      String parameter = " -its " + this.Iteration + " -samp " + this.Rhomap_Samp + " -burn " + this.Rhomap_Burn + " -bpen " + this.Rhomap_bpen + " -hpen " + this.Rhomap_hpen + " -no_file >output";

      String command = this.rhomap_cmd + " -seq " + this.sitesFile.replaceAll(" ", "\" \"") + " -loc " + this.locsFile.replaceAll(" ", "\" \"") + 
        " -lk " + this.lkFile.replaceAll(" ", "\" \"") + parameter;
      System.out.println("command = " + command);

      Runtime rt = Runtime.getRuntime();
      proc = rt.exec(command, null, runDir);

      StreamCleaner outputCleaner = new StreamCleaner(proc.getInputStream(), "OUTPUT");
      StreamCleaner errorCleaner = new StreamCleaner(proc.getErrorStream(), "ERROR");
      errorCleaner.start();
      outputCleaner.start();

      PrintWriter pw = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()));
      pw.write(10);
      pw.flush();

      int exitVal = proc.waitFor();
      if (exitVal != 0)
        System.err.println("Process exitValue in runRhomap(): " + exitVal);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  private void runStat(String infile)
  {
    String command = this.stat_cmd + " -input " + infile.replaceAll(" ", "\" \"") + " -burn 10";
    System.out.println("stat command= " + command);
    try {
      Runtime rt = Runtime.getRuntime();
      proc = rt.exec(command, null, runDir);

      StreamCleaner outputCleaner = new StreamCleaner(proc.getInputStream(), "OUTPUT");
      StreamCleaner errorCleaner = new StreamCleaner(proc.getErrorStream(), "ERROR");
      errorCleaner.start();
      outputCleaner.start();

      int exitVal = proc.waitFor();
      if (exitVal != 0)
        System.err.println("Process exitValue in runStat(): " + exitVal);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  private void parseRho(String infileName) throws FileNotFoundException
  {
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

  public ArrayList<Float> getMeanRhoList()
  {
    return this.meanRhoList;
  }

  public void run()
  {
    runRhomap();
    String path2stat = runDir.getAbsolutePath() + "/rates.txt";

    String osName = System.getProperty("os.name");
    if ((osName.equals("Windows NT")) || (osName.equals("Windows 7")) || (osName.equals("Windows XP")))
      path2stat = runDir.getAbsolutePath() + "\\rates.txt";
    else if ((osName.equals("Linux")) || (osName.equals("Unix"))) {
      path2stat = runDir.getAbsolutePath() + "/rates.txt";
    }

    System.out.println("path to stat :  " + path2stat);
    runStat(path2stat);
    try
    {
      parseRho("res.txt");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    if (!deleteDir(runDir))
      System.out.println("Fail to delete directory " + runDir.toString());
  }
}