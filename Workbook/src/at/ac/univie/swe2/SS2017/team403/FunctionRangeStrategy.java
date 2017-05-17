package at.ac.univie.swe2.SS2017.team403;

interface FunctionRangeStrategy {
	public double calculate(Range range);
}

class FunctionSUM implements FunctionRangeStrategy {
	@Override
	public double calculate(Range range) {
	   	double ret = 0;
	    for (Area a : range.getAreas())
	    {
        	Worksheet sheet = a.getParent();

            for (int r = a.getR1(); r <= a.getR2(); ++r) {
                for (int c = a.getC1(); c <= a.getC2(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null && cell.getValue() != null)
                		ret += cell.getNumericValue();
                }
            }
	    }
	    
	    for (Cell c : range.getCells())
	    	if (c != null && c.getValue() != null)
	    		ret += c.getNumericValue();
	    
	    return ret;
	}
}


class FunctionMEAN implements FunctionRangeStrategy {
	@Override
	public double calculate(Range range) {
	   	double ret = 0;
	   	int count = 0;
	    for (Area a : range.getAreas())
	    {
        	Worksheet sheet = a.getParent();

            for (int r = a.getR1(); r <= a.getR2(); ++r) {
                for (int c = a.getC1(); c <= a.getC2(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null && cell.getValue() != null)
                	{
                		++count;
                		ret += cell.getNumericValue();
                	}
                }
            }
	    }
	    
	    for (Cell c : range.getCells())
	    	if (c != null && c.getValue() != null) {
        		++count;
	    		ret += c.getNumericValue();
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
	    for (Area a : range.getAreas())
	    {
        	Worksheet sheet = a.getParent();

            for (int r = a.getR1(); r <= a.getR2(); ++r) {
                for (int c = a.getC1(); c <= a.getC2(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null && cell.getValue() != null)
                		++ret;
                }
            }
	    }
	    
	    for (Cell c : range.getCells())
	    	if (c != null && c.getValue() != null)
	    		++ret;

	    return ret;
	}
}
