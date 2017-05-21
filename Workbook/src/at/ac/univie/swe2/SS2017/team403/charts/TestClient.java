package at.ac.univie.swe2.SS2017.team403.charts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jfree.ui.RefineryUtilities;

public class TestClient {

	public static void main(String[] args) {

		String filename = "monatsmitteltemperaturen-seit-1981.csv";
		File file = new File(filename);

		/**
		LineChart lchart = new LineChart(file);

		lchart.pack();
		RefineryUtilities.centerFrameOnScreen(lchart);
		lchart.setVisible(true);
		
		*/
		
		BarChart bchart=new BarChart(file);
		bchart.pack();
		RefineryUtilities.centerFrameOnScreen(bchart);
		bchart.setVisible(true);

	}
}
