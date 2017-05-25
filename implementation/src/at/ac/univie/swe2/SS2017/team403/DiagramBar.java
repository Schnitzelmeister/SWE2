package at.ac.univie.swe2.SS2017.team403;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class DiagramBar extends Diagram {
	public DiagramBar() {
		values = null;
	}
	
	public DiagramBar(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback) {
		super(name, wbk, diagramChangedCallback);
	}
	
	DiagramBar(Diagram diagram, Workbook wbk, DiagramChangedCallback diagramChangedCallback) {
		super(diagram.getName(), wbk, diagramChangedCallback);
		values = ((DiagramBar)diagram).values;
	}
	
	private String values;
	public String getValues() { return values; }
	public void setValues(String values) {
		this.values = values; 
		this.parent.removeReferenceDependencies(this); 
		if (this.parent.getAutoCalculate())
			calculate(); 
		diagramChangedCallback.afterDiagramChanged(this.name); 
	}
	
	@Override
	public void calculate() {
		this.parent.removeReferenceDependencies(this);
		if (values != null)
			this.parent.addDependency(this, Range.getRangeByAddress(values, null, this.parent).getWorksheetAreas().firstKey()); 
	}
	
	//Externalizable
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeObject(values);
	}

	//Externalizable
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		values = (String)in.readObject();
	}

}

class DiagramBarCreator extends DiagramCreator {
    @Override
    public Diagram factoryMethod(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback) { return new DiagramBar(name, wbk, diagramChangedCallback); }
}