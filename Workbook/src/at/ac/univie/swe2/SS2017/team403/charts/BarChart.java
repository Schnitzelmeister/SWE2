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
	
public BarChart ( File file) {
      super( "Bar Chart" );  
      this.file=file;
      JFreeChart barChart = ChartFactory.createBarChart(
         file.getName(),           
         "X",            
         "Y",            
         createDataset(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
      setContentPane( chartPanel ); 
   }
   
   public CategoryDataset createDataset( ) {
    /**  
	  final String fiat = "FIAT";        
      final String audi = "AUDI";        
      final String ford = "FORD";        
      final String speed = "Speed";        
      final String millage = "Millage";        
      final String userrating = "User Rating";        
      final String safety = "safety";        
      final DefaultCategoryDataset dataset = 
      new DefaultCategoryDataset( );  

      dataset.addValue( 1.0 , fiat , speed );        
      dataset.addValue( 3.0 , fiat , userrating );        
      dataset.addValue( 5.0 , fiat , millage ); 
      dataset.addValue( 5.0 , fiat , safety );           

      dataset.addValue( 5.0 , audi , speed );        
      dataset.addValue( 6.0 , audi , userrating );       
      dataset.addValue( 10.0 , audi , millage );        
      dataset.addValue( 4.0 , audi , safety );

      dataset.addValue( 4.0 , ford , speed );        
      dataset.addValue( 2.0 , ford , userrating );        
      dataset.addValue( 3.0 , ford , millage );        
      dataset.addValue( 6.0 , ford , safety );               

      return dataset; 
   } */
	   
	   DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		try {
			Scanner inputStream = new Scanner(file);
			String firstRow = inputStream.next();
			String[] title = firstRow.split(";");
			NumberFormat format = NumberFormat.getInstance();

			// for(int i=0; inputStream.hasNext();i++){
			while (inputStream.hasNext()) {
				String data = inputStream.next();
				String[] values = data.split(";");
				System.out.println(values[0]);

				for (int j = 0; j < title.length - 1; j++) {
					Number number;
					try {
						number = format.parse(values[j + 1]);
						double d = number.doubleValue();
						// dataset.addValue(d,values[0],title[j+1]);
						dataset.addValue(d, title[j + 1], values[0]);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
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