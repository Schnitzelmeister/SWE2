package at.ac.univie.swe2.SS2017.team403;

public enum CellInputDataType {
	General(0), Boolean(1), Number(2), String(3), RANGE(4);
	
	private Integer ival;
	
	CellInputDataType(Integer ival) { this.ival = ival; }
	
    public Integer getNumVal() { return ival; }
}
