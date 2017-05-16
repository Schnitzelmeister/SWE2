package at.ac.univie.swe2.SS2017.team403;

interface FunctionRangeStrategy {
	public double calculate(Range[] ranges, Cell[] cells);
}

class FunctionSUM implements FunctionRangeStrategy {
	@Override
	public double calculate(Range[] ranges, Cell[] cells) {
	   	double ret = 0;
	    for (Range range : ranges)
	    {
        	Worksheet sheet = range.getParent();

            for (int r = range.getR1(); r <= range.getR2(); ++r) {
                for (int c = range.getC1(); c <= range.getC2(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null)
                		ret += cell.getNumericValue();
                }
            }
	    }
	    
	    for (Cell cell : cells)
	    	if (cell != null)
	    		ret += cell.getNumericValue();
	    
	    return ret;
	}
}


class FunctionMEAN implements FunctionRangeStrategy {
	@Override
	public double calculate(Range[] ranges, Cell[] cells) {
	   	double ret = 0;
	   	int count = 0;
	    for (Range range : ranges)
	    {
        	Worksheet sheet = range.getParent();

            for (int r = range.getR1(); r <= range.getR2(); ++r) {
                for (int c = range.getC1(); c <= range.getC2(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null)
                	{
                		++count;
                		ret += cell.getNumericValue();
                	}
                }
            }
	    }
	    
	    for (Cell cell : cells) {
	    	if (cell != null) {
	    		++count;
	    		ret += cell.getNumericValue();
	    	}
	    }
	    
	    if (count == 0)
	    	return 0;
	    return ret / count;
	}
}

class FunctionCOUNT implements FunctionRangeStrategy {
	@Override
	public double calculate(Range[] ranges, Cell[] cells) {
	   	int ret = 0;
	    for (Range range : ranges)
	    {
        	Worksheet sheet = range.getParent();

            for (int r = range.getR1(); r <= range.getR2(); ++r) {
                for (int c = range.getC1(); c <= range.getC2(); ++c) {
                	Cell cell = sheet.getCell(r, c, false);
                	if (cell != null)
                		++ret;
                }
            }
	    }
	    
	    for (Cell cell : cells)
	    	if (cell != null)
        		++ret;
	    
	    return ret;
	}
}
