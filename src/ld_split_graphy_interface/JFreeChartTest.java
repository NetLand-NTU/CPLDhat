package ld_split_graphy_interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JFreeChartTest
{
  private int LocationID = 0;

  private final JCheckBox wholerhomapcheckBox = new JCheckBox("Profile of whole population");
  private boolean checkboxselected = false;
  public final JPanel southJPanel = new JPanel();
  private JFreeChart chart;
  public ChartPanel chartPanel;
  private JRadioButton LoadLDHatRawRadioButton = new JRadioButton("load LD Hat raw data");
  private XYSeries LDHatseries = new XYSeries("Recombination Profile");

  private ArrayList<Float> snppos = new ArrayList();

  private final JButton loadRandomResultJButton = new JButton("LoadRandomFile");
  private final JFrame loadfileframe = new JFrame();
  public static HotspotWindow hotspotRandomWindow;
  private ArrayList<Integer> LDHatRhoMaps = new ArrayList();

  private float MaximalPosition = 0.0F;
  private boolean PVALUEGENERATION = false;

  private void GetMaximalPosition()
  {
    Float id = (Float)this.snppos.get(0);
    for (int i = 0; i < this.snppos.size(); i++)
    {
      id = (Float)this.snppos.get(i);
      if (this.MaximalPosition < id.floatValue())
        this.MaximalPosition = id.floatValue();
    }
  }

  private void AssignLDHatSeries()
  {
    ArrayList LDHatArraylist = hotspotRandomWindow.GetWholeRhoMap();

    Float id = (Float)this.snppos.get(0);

    for (int i = 0; (i < LDHatArraylist.size()) && (i < this.snppos.size()); i++)
    {
      this.LDHatseries.add(id, (Number)LDHatArraylist.get(i));
      id = (Float)this.snppos.get(i);
      this.LDHatseries.add(id, (Number)LDHatArraylist.get(i));
    }
  }

  private XYSeries AssginSeries(Integer StringID, Integer list)
  {
    System.out.println("assignSeries");

    XYSeries series = new XYSeries("Profile of allele " + list.toString());

    return series;
  }

  public XYDataset createxydataset()
  {
    DefaultXYDataset xydataset = new DefaultXYDataset();

    ListIterator iterator = this.snppos.listIterator();
    double[][] datas = new double[2][this.snppos.size()];
    for (int i = 0; (i < this.snppos.size()) && (iterator.hasNext()); i++)
    {
      datas[0][i] = ((Float)iterator.next()).floatValue();
      datas[1][i] = -1.0D;
    }

    xydataset.addSeries("SNPs", datas);
    return xydataset;
  }

  public JFreeChartTest(ArrayList<Float> snpPos, HotspotWindow hotspotrandomwindow)
  {
    hotspotRandomWindow = hotspotrandomwindow;
    this.snppos = snpPos;

    AssignLDHatSeries();

    this.checkboxselected = true;

    XYSeriesCollection dataset = new XYSeriesCollection();

    dataset.addSeries(this.LDHatseries);

    this.chart = ChartFactory.createXYLineChart("", "Physical locations(kb)", "Recombination rate(cM/Mb)", dataset, PlotOrientation.VERTICAL, true, true, false);
    XYPlot plot = this.chart.getXYPlot();

    ArrayList wholeRhoMap = hotspotRandomWindow.GetWholeRhoMap();
    float max = ((Float)wholeRhoMap.get(0)).floatValue();
    for (int i = 1; i < wholeRhoMap.size(); i++) {
      if (((Float)wholeRhoMap.get(i)).floatValue() > max) {
        max = ((Float)wholeRhoMap.get(i)).floatValue();
      }
    }

    ValueAxis rangeAxis = plot.getRangeAxis();
    rangeAxis.setUpperBound(max + 20.0F);
    rangeAxis.setLowerBound(-3.0D);

    this.chartPanel = new ChartPanel(this.chart);

    this.chartPanel.removeAll();
    this.chartPanel.revalidate();
    this.chartPanel = null;
    this.chartPanel = new ChartPanel(this.chart);

    ConfigureFrame.JfreechartPanel.removeAll();
    ConfigureFrame.JfreechartPanel.revalidate();
    ConfigureFrame.JfreechartPanel.setLayout(new BorderLayout());
    ConfigureFrame.JfreechartPanel.add(this.chartPanel, "Center");
    ConfigureFrame.configureframe.add(ConfigureFrame.JfreechartPanel, "Center");

    initialJButton();
  }

  private void initialJButton()
  {
    this.wholerhomapcheckBox.addActionListener(
      new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent) {
        AbstractButton abstractButton = (AbstractButton)actionEvent.getSource();
        JFreeChartTest.this.checkboxselected = abstractButton.getModel().isSelected();
        XYSeries series1 = JFreeChartTest.this.AssginSeries(Integer.valueOf(JFreeChartTest.this.LocationID), Integer.valueOf(0));
        XYSeries series2 = JFreeChartTest.this.AssginSeries(Integer.valueOf(JFreeChartTest.this.LocationID), Integer.valueOf(1));

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        if (JFreeChartTest.this.checkboxselected)
          dataset.addSeries(JFreeChartTest.this.LDHatseries);
        JFreeChartTest.this.chart = ChartFactory.createXYLineChart("", "Physical locations(kb)", "Recombination rate(cM/Mb)", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = JFreeChartTest.this.chart.getXYPlot();

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setUpperBound(80.0D);
        rangeAxis.setLowerBound(-3.0D);

        JFreeChartTest.this.chartPanel.removeAll();
        JFreeChartTest.this.chartPanel.revalidate();
        JFreeChartTest.this.chartPanel = null;
        JFreeChartTest.this.chartPanel = new ChartPanel(JFreeChartTest.this.chart);

        ConfigureFrame.JfreechartPanel.removeAll();
        ConfigureFrame.JfreechartPanel.revalidate();
        ConfigureFrame.JfreechartPanel.setLayout(new BorderLayout());
        ConfigureFrame.JfreechartPanel.add(JFreeChartTest.this.chartPanel, "Center");
        ConfigureFrame.JfreechartPanel.add(JFreeChartTest.this.southJPanel, "South");
        ConfigureFrame.configureframe.add(ConfigureFrame.JfreechartPanel, "Center");
      }
    });
  }
}