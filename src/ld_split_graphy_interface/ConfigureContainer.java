package ld_split_graphy_interface;

public class ConfigureContainer
{
  private static String JudgeVisualization = null;
  private static String finalresultFile = null;
  private static String sitesFile = null;
  private static String locsFile = null;
  private static String lkFile = null;
  private static String OutputFile = "outputFile";
  private static String VisualizationResultFilePath = null;
  private static Integer RandomSampleNumber = Integer.valueOf(100);
  private static Integer IterationTime = Integer.valueOf(10000);
  private static Integer Rhomap_Samp = Integer.valueOf(10);
  private static Integer Rhomap_Burn = Integer.valueOf(1000);
  private static boolean RadioChoice = false;
  private static float Rhomap_bpen = 0.0F;
  private static float Rhomap_hpen = 0.0F;

  private static boolean lkCalchoosed = false;

  public static boolean CheckingParameter()
  {
    if (IterationTime.intValue() < 1) {
      ParameterChecking.ErrorInforming("IterationTime should be larger than 1");

      return false;
    }

    if (Rhomap_Samp.intValue() < 1) {
      ParameterChecking.ErrorInforming("Rhomap_Samp should be more than 1");

      return false;
    }

    if (Rhomap_Burn.intValue() <= 0) {
      ParameterChecking.ErrorInforming("Rhomap_Burn should be Positive Integer");
      return false;
    }

    if (Rhomap_bpen < 0.0F) {
      ParameterChecking.ErrorInforming("background penalty should be positive");
      return false;
    }

    if (Rhomap_hpen < 0.0F) {
      ParameterChecking.ErrorInforming("hotspot penalty should be positive");
      return false;
    }

    if ((sitesFile.equals("")) || (locsFile.equals(""))) {
      ParameterChecking.ErrorInforming("inputfile should not be null");

      return false;
    }

    if (!lkCalchoosed) {
      if (lkFile.equals("")) {
        ParameterChecking.ErrorInforming("input lkfile should not be null");
        return false;
      }

      return true;
    }

    return true;
  }

  public static void SetVisualizationChoice(String VisualizationChoice) {
    JudgeVisualization = VisualizationChoice;
  }

  public static String GetVisualizationChoice() {
    return JudgeVisualization;
  }

  public static void SetRadioChoice(boolean radioChoice) {
    RadioChoice = radioChoice;
  }

  public static boolean GetRadioChoice() {
    return RadioChoice;
  }

  public static void SetFinalResultFile(String finalresult) {
    finalresultFile = finalresult;
  }

  public static String GetFinalResultFile() {
    return finalresultFile;
  }

  public static void SetSitesFile(String sitesfile) {
    sitesFile = sitesfile;
  }

  public static String GetSitesFile() {
    return sitesFile;
  }

  public static void SetLocsFile(String locsfile) {
    locsFile = locsfile;
  }

  public static String GetLocsFile() {
    return locsFile;
  }

  public static void SetOutputFile(String outputfile) {
    OutputFile = outputfile;
  }

  public static String GetOutputFile() {
    return OutputFile;
  }

  public static void SetVisualizationResultFilePath(String Path) {
    VisualizationResultFilePath = Path;
  }

  public static String GetVisualizationResultFilePath() {
    return VisualizationResultFilePath;
  }

  public static void SetIterationTime(Integer iterationTime) {
    IterationTime = iterationTime;
  }

  public static Integer GetIterationTime() {
    return IterationTime;
  }

  public static void SetRhomap_Samp(Integer rhomap_Samp) {
    Rhomap_Samp = rhomap_Samp;
  }

  public static Integer GetRhomap_Samp() {
    return Rhomap_Samp;
  }

  public static void SetRhomap_Burn(Integer rhomap_Burn) {
    Rhomap_Burn = rhomap_Burn;
  }

  public static Integer GetRhomap_Burn() {
    return Rhomap_Burn;
  }

  public static void SetRandomSample(Integer sampleNumber) {
    RandomSampleNumber = sampleNumber;
  }

  public static float GetRhomap_bpen() {
    return Rhomap_bpen;
  }

  public static void SetRhomap_bpen(float bpen)
  {
    Rhomap_bpen = bpen;
  }

  public static void SetRhomap_hpen(float hpen) {
    Rhomap_hpen = hpen;
  }

  public static float GetRhomap_hpen() {
    return Rhomap_hpen;
  }

  public static String GetLkFile() {
    return lkFile;
  }

  public static void SetLkFile(String lkfile) {
    lkFile = lkfile;
  }

  public static void SetLkCalchoosed(boolean ifchoosed) {
    lkCalchoosed = ifchoosed;
  }

  public static boolean GetLkCalchoosed() {
    return lkCalchoosed;
  }
}