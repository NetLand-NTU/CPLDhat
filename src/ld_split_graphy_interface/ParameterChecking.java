package ld_split_graphy_interface;

import java.awt.Frame;
import javax.swing.JOptionPane;

public class ParameterChecking
{
  public static void ErrorInforming(String ErrorInformation)
  {
    Frame notice = new Frame();
    JOptionPane.showMessageDialog(notice, ErrorInformation);
  }
}