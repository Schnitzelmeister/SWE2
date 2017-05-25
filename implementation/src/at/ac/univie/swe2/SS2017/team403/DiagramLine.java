package at.ac.univie.swe2.SS2017.team403;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class DiagramLine extends Diagram {
	private static final long serialVersionUID = 1L;
	
	public DiagramLine(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback) {
		super(name, wbk, diagramChangedCallback);
	}
	
	
	DiagramLine(Diagram diagram, Workbook wbk, DiagramChangedCallback diagramChangedCallback) {
		super(diagram.getName(), wbk, diagramChangedCallback);
		values = ((DiagramLine)diagram).values;
	}
	
	private Area values;
	public Area getValues() { return values; }
	public void setValues(Area values) {
		this.values = values; 
		this.parent.removeReferenceDependencies(this); 
		this.parent.addDependency(this, values); 
		diagramChangedCallback.afterDiagramChanged(this.name); 
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
		values = (Area)in.readObject();
	}

}


class DiagramLineCreator extends DiagramCreator {
    @Override
    public Diagram factoryMethod(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback) { return new DiagramLine(name, wbk, diagramChangedCallback); }
}
