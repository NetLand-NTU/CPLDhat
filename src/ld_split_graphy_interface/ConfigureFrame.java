package ld_split_graphy_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ConfigureFrame extends JFrame
{
  private JButton SettingJButton = new JButton("Load");
  private JButton cancelJButton = new JButton("Cancel");

  public static JRadioButton LoadRawButton = new JRadioButton("Rhomap method");
  public static JRadioButton LoadConvergenceButton = new JRadioButton("CLDhat method");

  public static JRadioButton LoadResultButton = new JRadioButton("Load recombination profiles");
  private JPanel panelRadio = new JPanel();

  public static JPanel JfreechartPanel = new JPanel();
  public static JPanel HotspotPanel = new JPanel();

  public static LDsplitRunner ldsplitRunner = null;
  public static LDsplitRunner1 ldsplitRunner1 = null;

  public static JFrame configureframe = new JFrame("LDhat-convergence-parallel: estimation of Recombination Rates");
  private JFreeChartTest jfreechart;

  private void Init()
  {
    configureframe.setLayout(new BorderLayout());
    this.panelRadio.add(LoadRawButton);
    this.panelRadio.add(LoadConvergenceButton);

    this.panelRadio.add(LoadResultButton);

    ActionListener sliceActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        AbstractButton RadioButton = (AbstractButton)actionEvent.getSource();
        if (actionEvent.getSource() == ConfigureFrame.LoadRawButton) {
          ConfigureContainer.SetRadioChoice(false);
          ConfigureFrame.LoadRawButton.setEnabled(false);
          ConfigureFrame.LoadConvergenceButton.setEnabled(false);

          ConfigureFrame.LoadResultButton.setEnabled(false);

          ConfigureFrame.ldsplitRunner = new LDsplitRunner();
        } else if (actionEvent.getSource() == ConfigureFrame.LoadConvergenceButton) {
          ConfigureContainer.SetRadioChoice(false);
          ConfigureFrame.LoadRawButton.setEnabled(false);
          ConfigureFrame.LoadConvergenceButton.setEnabled(false);

          ConfigureFrame.LoadResultButton.setEnabled(false);
          ConfigureFrame.ldsplitRunner1 = new LDsplitRunner1();
        }
        else if (actionEvent.getSource() == ConfigureFrame.LoadResultButton)
        {
          JFrame ft = new JFrame();

          FileDialog filedialog = new FileDialog(ft, "open data file dialog ", 0);
          filedialog.setVisible(true);
          String FileResultStr = filedialog.getDirectory() + filedialog.getFile();
          System.out.println(FileResultStr);
          ConfigureContainer.SetRadioChoice(true);
          ConfigureContainer.SetFinalResultFile(FileResultStr);
          FileInputStream inputfile = null;
          ObjectInputStream objectinput = null;
          try
          {
            inputfile = new FileInputStream(FileResultStr.toString());
            objectinput = new ObjectInputStream(inputfile);
            JFreeChartTest.hotspotRandomWindow = (HotspotWindow)objectinput.readObject();
          } catch (FileNotFoundException ex) {
            Logger.getLogger(JFreeChartTest.class.getName()).log(Level.SEVERE, null, ex);
          } catch (IOException ex) {
            Logger.getLogger(JFreeChartTest.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ClassNotFoundException ex) {
            Logger.getLogger(JFreeChartTest.class.getName()).log(Level.SEVERE, null, ex);
          }

          ArrayList snpPos = JFreeChartTest.hotspotRandomWindow.GetSNPMAF();

          ConfigureFrame.this.jfreechart = new JFreeChartTest(snpPos, JFreeChartTest.hotspotRandomWindow);

          ConfigureFrame.JfreechartPanel.removeAll();
          ConfigureFrame.JfreechartPanel.revalidate();

          ConfigureFrame.JfreechartPanel.setLayout(new BorderLayout());
          ConfigureFrame.JfreechartPanel.add(ConfigureFrame.this.jfreechart.chartPanel, "Center");
          ConfigureFrame.JfreechartPanel.add(ConfigureFrame.this.jfreechart.southJPanel, "South");
          ConfigureFrame.JfreechartPanel.setVisible(true);
          ConfigureFrame.configureframe.add(ConfigureFrame.JfreechartPanel, "Center");

          ConfigureFrame.configureframe.add(ConfigureFrame.HotspotPanel, "East");

          ConfigureFrame.configureframe.setVisible(true);
        }
      }
    };
    this.SettingJButton.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
      }
    });
    LoadRawButton.addActionListener(sliceActionListener);
    LoadConvergenceButton.addActionListener(sliceActionListener);

    LoadResultButton.addActionListener(sliceActionListener);

    configureframe.add(this.panelRadio, "North");

    JfreechartPanel.setPreferredSize(new Dimension(800, 400));
    JfreechartPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Recombination Profiles"));
    configureframe.add(JfreechartPanel, "Center");

    configureframe.add(HotspotPanel, "East");

    configureframe.setSize(800, 700);
    configureframe.setVisible(true);
  }

  public ConfigureFrame()
  {
    Init();
    String path_dir_2 = getClass().getResource("").getFile();
    System.out.println(path_dir_2);
    String osName = System.getProperty("os.name");
    if ((osName.equals("Windows NT")) || (osName.equals("Windows 7")) || (osName.equals("Windows XP"))) {
      path_dir_2 = path_dir_2.substring(1, path_dir_2.length());
      path_dir_2 = path_dir_2.replaceAll("/", "\\\\");
    }

    System.out.println("the path_2 is: " + path_dir_2);

    String path_dir_3 = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    if ((osName.equals("Windows NT")) || (osName.equals("Windows 7")) || (osName.equals("Windows XP"))) {
      path_dir_3 = path_dir_3.substring(1, path_dir_3.length());
      path_dir_3 = path_dir_3.replaceAll("/", "\\\\");
    }
    System.out.println("the path_3 is: " + path_dir_3);

    String path = new File(getClass().getResource("").getPath()).getParentFile().getPath();
    System.out.println("the path is: " + path);
  }

  public static void main(String[] args)
  {
    int Length = args.length;

    if (Length <= 0) {
      ConfigureFrame jradiobutton = new ConfigureFrame();
      configureframe.setDefaultCloseOperation(3);
      jradiobutton.setDefaultCloseOperation(3);
    }
  }
}