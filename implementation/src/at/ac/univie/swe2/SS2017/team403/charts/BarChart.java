package at.ac.univie.swe2.SS2017.team403.charts;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BarChart extends ApplicationFrame implements Charts {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	File file;

	public BarChart(File file) {
		super("Bar Chart");
		this.file = file;
		JFreeChart barChart = ChartFactory.createBarChart(file.getName(), "X-Axis", "Y-Axis", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);
	}

	public CategoryDataset createDataset() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		try {
			Scanner inputStream = new Scanner(file);
			String firstRow = inputStream.next();
			String[] title = firstRow.split(";");
			NumberFormat format = NumberFormat.getInstance();

			while (inputStream.hasNext()) {
				String data = inputStream.next();
				String[] values = data.split(";");
				System.out.println(values[0]);

				for (int j = 0; j < title.length - 1; j++) {
					Number number;
					try {
						number = format.parse(values[j + 1]);
						double d = number.doubleValue();

						dataset.addValue(d, title[j + 1], values[0]);
					} catch (ParseException e) {

						e.printStackTrace();
					}

				}

			}

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return dataset;
	}

}