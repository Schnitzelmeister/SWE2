package at.ac.univie.swe2.SS2017.team403;

import java.util.Iterator;
import java.util.Map;

interface FunctionRangeStrategy {
	public double calculate(Range range);
}

class FunctionSUM implements FunctionRangeStrategy {
	@Override
	public double calculate(Range range) {
	   	double ret = 0;
	   
	    for (Iterator< Map.Entry<Area, Integer > > iter = range.getAreas().entrySet().iterator(); iter.hasNext();)
	    {
	    	Map.Entry<Area, Integer > e = iter.next();
	    	Area a = e.getKey();
	    	
	    	double tmp = 0;
	    	
        	Worksheet sheet = a.getParent();

            for (int r = a.getFirstRow(); r <= a.getLastRow(); ++r) {
                for (int c = a.getFirstColumn(); c <= a.getLastColumn(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null && cell.getCellValue() != null)
                		tmp += cell.getNumericValue();
                }
            }
            
            ret += (tmp * e.getValue());
	    }
	    
	    for (Iterator< Map.Entry<Cell, Integer > > iter = range.getCells().entrySet().iterator(); iter.hasNext();)
	    {
	    	Map.Entry<Cell, Integer > e = iter.next();
	    	Cell c = e.getKey();
	    	if (c != null && c.getCellValue() != null)
	    		ret += (c.getNumericValue() * e.getValue());
	    }
	    
	    return ret;
	}
}


class FunctionMEAN implements FunctionRangeStrategy {
	@Override
	public double calculate(Range range) {
	   	double ret = 0;
	   	int count = 0;

	    for (Iterator< Map.Entry<Area, Integer > > iter = range.getAreas().entrySet().iterator(); iter.hasNext();)
	    {
	    	Map.Entry<Area, Integer > e = iter.next();
	    	Area a = e.getKey();
	    	
	    	double tmp = 0;
		   	int tmp2 = 0;
	    	
        	Worksheet sheet = a.getParent();

            for (int r = a.getFirstRow(); r <= a.getLastRow(); ++r) {
                for (int c = a.getFirstColumn(); c <= a.getLastColumn(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null && cell.getCellValue() != null)
                	{
                		++tmp2;
                		ret += cell.getNumericValue();
                	}
                }
            }
            
            count += (tmp2 * e.getValue());
            ret += (tmp * e.getValue());
	    }
	    
	    for (Iterator< Map.Entry<Cell, Integer > > iter = range.getCells().entrySet().iterator(); iter.hasNext();)
	    {
	    	Map.Entry<Cell, Integer > e = iter.next();
	    	Cell c = e.getKey();
	    	if (c != null && c.getCellValue() != null) {
	    		count += e.getValue();
	    		ret += (c.getNumericValue() * e.getValue());
	    	}
	    }
	   	
	    if (count == 0)
	    	return 0;
	    return ret / count;
	}
}

class FunctionCOUNT implements FunctionRangeStrategy {
	@Override
	public double calculate(Range range) {
	   	int ret = 0;
	    for (Iterator< Map.Entry<Area, Integer > > iter = range.getAreas().entrySet().iterator(); iter.hasNext();)
	    {
	    	Map.Entry<Area, Integer > e = iter.next();
	    	Area a = e.getKey();
	    	
	    	int tmp = 0;
	    	
        	Worksheet sheet = a.getParent();

            for (int r = a.getFirstRow(); r <= a.getLastRow(); ++r) {
                for (int c = a.getFirstColumn(); c <= a.getLastColumn(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null && cell.getCellValue() != null)
                		++tmp;
                }
            }
            
            ret += (tmp * e.getValue());
	    }
	    
	    for (Iterator< Map.Entry<Cell, Integer > > iter = range.getCells().entrySet().iterator(); iter.hasNext();)
	    {
	    	Map.Entry<Cell, Integer > e = iter.next();
	    	Cell c = e.getKey();
	    	if (c != null && c.getCellValue() != null)
	    		ret += e.getValue();
	    }
	   	
	    return ret;
	}
}
