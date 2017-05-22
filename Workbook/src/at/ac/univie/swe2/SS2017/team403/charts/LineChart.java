package at.ac.univie.swe2.SS2017.team403.charts;

import org.jfree.chart.ChartPanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart extends ApplicationFrame implements Charts {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	File file;

	public LineChart(File file) {
		super("Line Chart");
		this.file = file;
		JFreeChart lineChart = ChartFactory.createLineChart(file.getName(), "X-Axis", "Y-Axis", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
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