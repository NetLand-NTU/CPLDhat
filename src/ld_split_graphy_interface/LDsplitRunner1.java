package ld_split_graphy_interface;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class LDsplitRunner1 extends JFrame
{
  private final JButton startJButton = new JButton("Start");
  private final JButton cancelJButton = new JButton("Cancel");

  private final JButton showresult = new JButton("Show");

  final JFrame loadfileframe = new JFrame();

  private final JLabel siteFileLabel = new JLabel("   Path to sites file:");
  private final JTextField SiteFiletxtfiled = new JTextField(ConfigureContainer.GetSitesFile());
  private JButton SiteFileButton = new JButton("Load");
  private JLabel LocsFileLabel = new JLabel("     Path to locs file:");
  private final JTextField LocsFiletxtfiled = new JTextField(ConfigureContainer.GetLocsFile());
  private JButton LocsFileButton = new JButton("Load");
  private JLabel LkFileLabel = new JLabel("                 Path to lk file:");
  private final JTextField LkFiletxtfiled = new JTextField(ConfigureContainer.GetLkFile());
  private JRadioButton LkCalButton = new JRadioButton("I want to calculate lk file!");
  private JButton LkFileButton = new JButton("Load");

  private JLabel IterationLabel = new JLabel("CLDhat   -convergence   ");

  private JLabel RandomSampleLabel = new JLabel("   -bpen:");
  private final JTextField RandomSampletxtfiled = new JTextField(Float.toString(ConfigureContainer.GetRhomap_bpen()));

  private JLabel MAFLabel = new JLabel("   -hpen:");
  private final JTextField MAFtxtfiled = new JTextField(Float.toString(ConfigureContainer.GetRhomap_hpen()));

  private JPanel panelRouter = new JPanel();
  private JPanel panelParameter = new JPanel();
  private JPanel panelRunning = new JPanel();

  private static String work_dir = null;
  private static String sitesFile = null;
  private static String locsFile = null;
  private static String lkFile = null;
  private static String resultFile;
  private boolean LDsplitFrameClose = false;

  private HotspotWindow hotspotWindow_Run = null;
  private LDsplitCaller caller;
  private float bpen = 0.0F;
  private float hpen = 0.0F;

  private void initConfigure()
  {
//    String osName = System.getProperty("os.name");
//    if ((osName.equals("Windows NT")) || (osName.equals("Windows 7")) || (osName.equals("Windows XP"))) {
//      work_dir = "D:\\netbeansprogramme\\JUnitTest\\src\\ld_split_graphy_interface";
//    }
//    else if ((osName.equals("Linux")) || (osName.equals("Unix"))) {
//      work_dir = "/home/zhengj/project/LDsplit_java";
//    }

    sitesFile = ConfigureContainer.GetSitesFile();
    locsFile = ConfigureContainer.GetLocsFile();
    lkFile = ConfigureContainer.GetLkFile();
    resultFile = ConfigureContainer.GetFinalResultFile();
    this.bpen = ConfigureContainer.GetRhomap_bpen();
    this.hpen = ConfigureContainer.GetRhomap_hpen();
    System.out.println("startrhomap");
  }

  public void SetFile_dir(String strdir)
  {
    sitesFile = strdir;
  }

  public LDsplitRunner1()
  {
    super("Run method");

    setLayout(new BorderLayout());

    this.panelRouter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Input Files"));

    this.SiteFiletxtfiled.setColumns(40);
    this.panelRouter.add(this.siteFileLabel);
    this.panelRouter.add(this.SiteFiletxtfiled);
    this.panelRouter.add(this.SiteFileButton);
    this.SiteFileButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Frame loadfileframe = new Frame();
        FileDialog filedialog = new FileDialog(loadfileframe, "open final data file dialog", 0);
        filedialog.setVisible(true);
        String str = filedialog.getDirectory() + filedialog.getFile();
        System.out.println(str);
        LDsplitRunner1.this.SiteFiletxtfiled.setText(str);
      }
    });
    this.LocsFiletxtfiled.setColumns(40);
    this.panelRouter.add(this.LocsFileLabel);
    this.panelRouter.add(this.LocsFiletxtfiled);
    this.panelRouter.add(this.LocsFileButton);
    this.LocsFileButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Frame loadfileframe = new Frame();
        FileDialog filedialog = new FileDialog(loadfileframe, "open final data file dialog", 0);
        filedialog.setVisible(true);
        String str = filedialog.getDirectory() + filedialog.getFile();
        System.out.println(str);
        LDsplitRunner1.this.LocsFiletxtfiled.setText(str);
      }
    });
    this.LkFiletxtfiled.setColumns(40);
    this.panelRouter.add(this.LkCalButton);
    this.panelRouter.add(this.LkFileLabel);
    this.panelRouter.add(this.LkFiletxtfiled);
    this.panelRouter.add(this.LkFileButton);

    this.LkCalButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (LDsplitRunner1.this.LkCalButton.isSelected()) {
          LDsplitRunner1.this.LkFiletxtfiled.setEditable(false);
          LDsplitRunner1.this.LkFiletxtfiled.setText("");
          LDsplitRunner1.this.LkFileButton.setEnabled(false);
          ConfigureContainer.SetLkCalchoosed(true);
        } else {
          LDsplitRunner1.this.LkFiletxtfiled.setEditable(true);
          LDsplitRunner1.this.LkFileButton.setEnabled(true);
          ConfigureContainer.SetLkCalchoosed(false);
        }
      }
    });
    this.LkFileButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Frame loadfileframe = new Frame();
        FileDialog filedialog = new FileDialog(loadfileframe, "open final data file dialog", 0);
        filedialog.setVisible(true);
        String str = filedialog.getDirectory() + filedialog.getFile();
        System.out.println(str);
        LDsplitRunner1.this.LkFiletxtfiled.setText(str);
      }
    });
    this.panelParameter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Parameter Setting"));

    this.panelParameter.add(this.IterationLabel);

    this.RandomSampletxtfiled.setColumns(5);
    this.panelParameter.add(this.RandomSampleLabel);
    this.panelParameter.add(this.RandomSampletxtfiled);

    this.MAFtxtfiled.setColumns(5);
    this.panelParameter.add(this.MAFLabel);
    this.panelParameter.add(this.MAFtxtfiled);

    this.panelRunning.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Running Processing"));
    this.panelRunning.setLayout(new BorderLayout());

    JPanel southJPanel = new JPanel();
    southJPanel.add(this.startJButton);
    this.startJButton.setEnabled(true);

    southJPanel.add(this.cancelJButton);
    this.cancelJButton.setEnabled(false);

    this.panelRunning.add(southJPanel, "South");

    this.startJButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ConfigureContainer.SetLocsFile(LDsplitRunner1.this.LocsFiletxtfiled.getText());
        ConfigureContainer.SetSitesFile(LDsplitRunner1.this.SiteFiletxtfiled.getText());
        ConfigureContainer.SetLkFile(LDsplitRunner1.this.LkFiletxtfiled.getText());

        ConfigureContainer.SetRhomap_bpen(Float.parseFloat(LDsplitRunner1.this.RandomSampletxtfiled.getText()));
        ConfigureContainer.SetRhomap_hpen(Float.parseFloat(LDsplitRunner1.this.MAFtxtfiled.getText()));

        LDsplitRunner1.this.initConfigure();

        LDsplitRunner1.this.caller = new LDsplitRunner1.LDsplitCaller();

        LDsplitRunner1.this.startJButton.setEnabled(false);
        LDsplitRunner1.this.cancelJButton.setEnabled(true);
        LDsplitRunner1.this.SiteFileButton.setEnabled(false);
        LDsplitRunner1.this.LocsFileButton.setEnabled(false);
        LDsplitRunner1.this.LkFileButton.setEnabled(false);
        LDsplitRunner1.this.caller.execute();
      }
    });
    this.cancelJButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JFrame frame = new JFrame();
        int res = JOptionPane.showConfirmDialog(frame, "Would ou like to cancel the process?", 
          "Cancel Process", 0);
        if (res == 0) {
          RunLDhat.kissprocess();
          LDsplitRunner1.this.caller.cancel();
          LDsplitRunner1.this.caller = null;
        }
      }
    });
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        LDsplitRunner1.this.LDsplitFrameClose = true;
        ConfigureFrame.LoadRawButton.setEnabled(true);
        ConfigureFrame.LoadConvergenceButton.setEnabled(true);

        ConfigureFrame.LoadResultButton.setEnabled(true);

        ConfigureFrame.LoadRawButton.setSelected(false);
        ConfigureFrame.LoadConvergenceButton.setSelected(false);

        ConfigureFrame.LoadResultButton.setSelected(false);
      }
    });
    add(this.panelRouter, "Center");
    add(this.panelParameter, "North");
    add(this.panelRunning, "South");

   
    
    setSize(750, 300);
    setResizable(false);
    setVisible(true);
  }

  class LDsplitCaller extends SwingWorker<Integer, Integer>
  {
    private boolean stopped = false;
    private boolean LDsplitFram = false;

    public LDsplitCaller()
    {
    	
    }

    protected Integer doInBackground()
      throws Exception
    {
    	
    	
      if (!ConfigureContainer.CheckingParameter()) {
        cancel();
        return Integer.valueOf(-1);
      }

      LDsplitRunner1.this.hotspotWindow_Run = new HotspotWindow();
      if (!LDsplitRunner1.this.hotspotWindow_Run.loadSnp(LDsplitRunner1.sitesFile, LDsplitRunner1.locsFile)) {
        cancel();
        return Integer.valueOf(-1);
      }

      if (!LDsplitRunner1.this.hotspotWindow_Run.test_snp()) {
        ParameterChecking.ErrorInforming("Input sequence number should be more than 20 and less than 192");
        cancel();
        return Integer.valueOf(-1);
      }

      if ((this.stopped) || (LDsplitRunner1.this.LDsplitFrameClose)) {
        return Integer.valueOf(-1);
      }

      LDsplitRunner1.this.hotspotWindow_Run.WholeRhoSnp(ConfigureContainer.GetSitesFile(), ConfigureContainer.GetLocsFile(), ConfigureContainer.GetLkFile(), 1);

      return Integer.valueOf(1);
    }

    protected void done()
    {
      LDsplitRunner1.this.startJButton.setEnabled(true);
      LDsplitRunner1.this.cancelJButton.setEnabled(false);

      if ((this.stopped) || (LDsplitRunner1.this.LDsplitFrameClose)) {
        return;
      }

      JFrame frame = new JFrame();
      int n = JOptionPane.showConfirmDialog(frame, "Would ou like to save result to a file?", 
        "export LDsplit result", 0);
      if (n == 0) {
        JFileChooser fc = new JFileChooser();
        int retVal = fc.showSaveDialog(frame);
        if (retVal == 0) {
          File resultFile = fc.getSelectedFile();
          try
          {
            ObjectOutputStream output = new ObjectOutputStream(
              new FileOutputStream(resultFile.toString()));
            output.writeObject(LDsplitRunner1.this.hotspotWindow_Run);
            if (output != null)
              output.close();
            else
              System.err.println("No data is written to file " + resultFile.toString());
          }
          catch (IOException ioException)
          {
            System.err.println("Error opening or closing file " + resultFile.toString());
          }
        }
        else {
          System.out.println("save file cancelled by user");
        }
      } else if (n == 1) {
        System.out.println("Don't save it!");
      }
      LDsplitRunner1.this.hotspotWindow_Run = null;
    }

    public void cancel()
    {
      this.stopped = true;

      LDsplitRunner1.this.startJButton.setEnabled(true);
      LDsplitRunner1.this.cancelJButton.setEnabled(false);
      LDsplitRunner1.this.SiteFileButton.setEnabled(true);
      LDsplitRunner1.this.LocsFileButton.setEnabled(true);
      LDsplitRunner1.this.LkFileButton.setEnabled(true);
      LDsplitRunner1.this.hotspotWindow_Run = null;
    }
  }
}