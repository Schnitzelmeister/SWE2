package at.ac.univie.swe2.SS2017.team403;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;

interface DiagramInvalidateStrategy {
	public void Initialize(Diagram diagram, JPanel container);

	public void Invalidate();
}

class LineDiagramInvalidateStrategy implements DiagramInvalidateStrategy {
	private JFreeChart chart;
	private DiagramLine diagram;

	public void Initialize(Diagram diagram, JPanel container) {

		this.diagram = (DiagramLine) diagram;
		Area dataArea = this.diagram.getValues();

		DefaultXYDataset dataSet = new DefaultXYDataset();
		if (dataArea != null) {
			for (int r = dataArea.getFirstRow() + 1; r <= dataArea.getLastRow(); ++r) {
				double[][] data = new double[2][dataArea.getLastColumn() - dataArea.getFirstColumn()];

				int counter = 0;
				// X-Values
				for (int c = dataArea.getFirstColumn() + 1; c <= dataArea.getLastColumn(); ++c)
					data[0][counter++] = dataArea.getParent().getCell(dataArea.getFirstRow(), c).getNumericValue();

				counter = 0;
				// Y-Values
				for (int c = dataArea.getFirstColumn() + 1; c <= dataArea.getLastColumn(); ++c) {
					data[1][counter++] = dataArea.getParent().getCell(r, c).getNumericValue();
				}

				dataSet.addSeries(dataArea.getParent().getCell(r, dataArea.getFirstColumn()).getTextValue(), data);
			}
		}
		// Generate the graph
		chart = ChartFactory.createXYLineChart(diagram.getName(), "x", "y", dataSet, PlotOrientation.VERTICAL, true,
				true, false);

		container.setLayout(new java.awt.BorderLayout());
		ChartPanel CP = new ChartPanel(chart);
		container.add(CP, BorderLayout.CENTER);
		container.validate();
	}

	public void Invalidate() {
		Area dataArea = this.diagram.getValues();

		DefaultXYDataset dataSet = new DefaultXYDataset();
		if (dataArea != null) {
			for (int r = dataArea.getFirstRow() + 1; r <= dataArea.getLastRow(); ++r) {
				double[][] data = new double[2][dataArea.getLastColumn() - dataArea.getFirstColumn()];

				int counter = 0;
				// X-Values
				for (int c = dataArea.getFirstColumn() + 1; c <= dataArea.getLastColumn(); ++c)
					data[0][counter++] = dataArea.getParent().getCell(dataArea.getFirstRow(), c).getNumericValue();

				counter = 0;
				// Y-Values
				for (int c = dataArea.getFirstColumn() + 1; c <= dataArea.getLastColumn(); ++c) {
					data[1][counter++] = dataArea.getParent().getCell(r, c).getNumericValue();
				}

				dataSet.addSeries(dataArea.getParent().getCell(r, dataArea.getFirstColumn()).getTextValue(), data);
			}
		}

		chart.getXYPlot().setDataset(dataSet);
	}

}

class BarDiagramInvalidateStrategy implements DiagramInvalidateStrategy {
	private JFreeChart chart;
	private DiagramBar diagram;

	public void Initialize(Diagram diagram, JPanel container) {

		this.diagram = (DiagramBar) diagram;
		Area dataArea = this.diagram.getValues();

		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		if (dataArea != null) {
			for (int r = dataArea.getFirstRow() + 1; r <= dataArea.getLastRow(); ++r) {
				for (int c = dataArea.getFirstColumn() + 1; c <= dataArea.getLastColumn(); ++c) {
					dataSet.addValue(dataArea.getParent().getCell(r, c).getNumericValue(),
							dataArea.getParent().getCell(r, dataArea.getFirstColumn()).getTextValue(),
							dataArea.getParent().getCell(dataArea.getFirstRow(), c).getTextValue());
				}
			}
		}

		// Generate the graph
		chart = ChartFactory.createBarChart(diagram.getName(), "x", "y", dataSet);

		container.setLayout(new java.awt.BorderLayout());
		ChartPanel CP = new ChartPanel(chart);
		container.add(CP, BorderLayout.CENTER);
		container.validate();
	}

	public void Invalidate() {
		Area dataArea = this.diagram.getValues();

		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		if (dataArea != null) {
			for (int r = dataArea.getFirstRow() + 1; r <= dataArea.getLastRow(); ++r) {
				for (int c = dataArea.getFirstColumn() + 1; c <= dataArea.getLastColumn(); ++c) {
					dataSet.addValue(dataArea.getParent().getCell(r, c).getNumericValue(),
							dataArea.getParent().getCell(r, dataArea.getFirstColumn()).getTextValue(),
							dataArea.getParent().getCell(dataArea.getFirstRow(), c).getTextValue());
				}
			}
		}

		chart.getCategoryPlot().setDataset(dataSet);
	}

}
