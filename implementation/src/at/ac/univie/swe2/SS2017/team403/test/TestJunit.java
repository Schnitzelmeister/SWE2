package at.ac.univie.swe2.SS2017.team403.test;

import at.ac.univie.swe2.SS2017.team403.Application;
import at.ac.univie.swe2.SS2017.team403.Workbook;
import at.ac.univie.swe2.SS2017.team403.Worksheet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;


public class TestJunit {

	@BeforeClass 
	public static void setUp() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		if (!wbk.getWorksheets().containsKey("sheet1"))
			wbk.addSheet("sheet1");
		if (!wbk.getWorksheets().containsKey("sheet2"))
			wbk.addSheet("sheet2");
	}
	
	@Test
	public void testFormulas() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		Worksheet sheet = wbk.getWorksheet("sheet1");
		sheet.getCell(1, 1).setNumericValue(11);
		sheet.getCell(2, 1).setNumericValue(12);
		sheet.getCell(3, 1).setNumericValue(13);

		sheet.getCell(1, 2).setFormula("=SUM(RC[-1]:R[3]C[-1])+COUNT(RC[-1]:R[3]C[-1])+MEAN(RC[-1]:R[3]C[-1])");
		//System.out.println(sheet.getCell(1, 2).getNumericValue());
		assertEquals(51.0d, sheet.getCell(1, 2).getNumericValue(), 0 );

		sheet.getCell(3, 1).setNumericValue(130);
		sheet.getCell(4, 1).setNumericValue(10);
		//System.out.println(sheet.getCell(1, 2).getNumericValue());
		assertEquals(207.75d, sheet.getCell(1, 2).getNumericValue(), 0 );
		
		sheet = wbk.getWorksheet("sheet2");
		
		sheet.getCell(1, 2).setFormula("=SUM(sheet1!RC[-1]:R[3]C[-1];RC[-1]:R[3]C[-1])+COUNT(sheet1!RC[-1]:R[3]C[-1];sheet2!RC[-1]:R[3]C[-1])+MEAN(sheet1!RC[-1]:R[3]C[-1])");
		//System.out.println(sheet.getCell(1, 2).getNumericValue());
		assertEquals(370.75d, sheet.getCell(1, 2).getNumericValue(), 0 );
		sheet.getCell(2, 2).setFormula("=SUM(RC[-1]:R[3]C[-1])+COUNT(RC[-1]:R[3]C[-1])+MEAN(RC[-1]:R[3]C[-1])");
		//System.out.println(sheet.getCell(2, 2).getNumericValue());
		assertEquals(0d, sheet.getCell(2, 2).getNumericValue(), 0 );

		sheet.getCell(1, 1).setFormula("=1");
		//System.out.println(sheet.getCell(1, 1).getNumericValue());
		assertEquals(1d, sheet.getCell(1, 1).getNumericValue(), 0 );

		sheet.getCell(1, 1).setFormula("=-1");
		//System.out.println(sheet.getCell(1, 1).getNumericValue());
		assertEquals(-1d, sheet.getCell(1, 1).getNumericValue(), 0 );

		sheet.getCell(2, 1).setFormula("=+3");
		//System.out.println(sheet.getCell(2, 1).getNumericValue());
		assertEquals(3d, sheet.getCell(2, 1).getNumericValue(), 0 );

		sheet.getCell(3, 1).setFormula("=+3-4");
		//System.out.println(sheet.getCell(3, 1).getNumericValue());
		assertEquals(-1d, sheet.getCell(3, 1).getNumericValue(), 0 );

		//System.out.println(sheet.getCell(2, 2).getNumericValue());
		assertEquals(5d, sheet.getCell(2, 2).getNumericValue(), 0 );

		//System.out.println(sheet.getCell(1, 2).getNumericValue());
		assertEquals(373.75d, sheet.getCell(1, 2).getNumericValue(), 0 );

		sheet.getCell(1, 2).setFormula("=SUM(sheet2!RC[-1]:R[3]C[-1])");
		//System.out.println(sheet.getCell(1, 2).getNumericValue());
		assertEquals(1d, sheet.getCell(1, 2).getNumericValue(), 0 );

		sheet.getCell(1, 1).setFormula("=10^2");
		//System.out.println(sheet.getCell(1, 2).getNumericValue());
		assertEquals(102d, sheet.getCell(1, 2).getNumericValue(), 0 );

		sheet.getCell(2, 2).setFormula("=RC[1]+RC[-1]");
		//System.out.println(sheet.getCell(2, 2).getNumericValue());
		assertEquals(3d, sheet.getCell(2, 2).getNumericValue(), 0 );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCircularReference1() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		Worksheet sheet = wbk.getWorksheet("sheet2");
		
		//Circular reference
		sheet.getCell(1, 2).setFormula("=rc[-1]");
		sheet.getCell(1, 3).setFormula("=rc[-1]");
		sheet.getCell(1, 1).setFormula("=rc3");
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testCircularReference2() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		Worksheet sheet = wbk.getWorksheet("sheet2");
		//Circular reference
		sheet.getCell(2, 2).setFormula("=sum(rc[1]:rc[-1])");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDivideByNill() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		Worksheet sheet = wbk.getWorksheet("sheet2");
		//div by 0
		sheet.getCell(2, 2).setFormula("=1/R10C10");
	}
	
	@Test(expected=ClassCastException.class)
	public void testGetTextAsNumber() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		Worksheet sheet = wbk.getWorksheet("sheet2");
		sheet.getCell(2, 2).setTextValue("text value");
		sheet.getCell(2, 2).getNumericValue();
	}
}
