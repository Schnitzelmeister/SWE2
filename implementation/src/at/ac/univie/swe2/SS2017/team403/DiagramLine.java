package at.ac.univie.swe2.SS2017.team403;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class DiagramLine extends Diagram {
	private static final long serialVersionUID = 1L;

	public DiagramLine(String name, Workbook workBook, DiagramChangedCallback diagramChangedCallback) {
		super(name, workBook, diagramChangedCallback);
	}

	DiagramLine(Diagram diagram, Workbook workBook, DiagramChangedCallback diagramChangedCallback) {
		super(diagram.getName(), workBook, diagramChangedCallback);
		values = ((DiagramLine) diagram).values;
	}

	private Area values;

	public Area getValues() {
		return values;
	}

	public void setValues(Area values) {
		this.values = values;
		this.parent.removeReferenceDependencies(this);
		if (this.parent.getAutoCalculate())
			this.parent.addDependency(this, values);
		diagramChangedCallback.afterDiagramChanged(this.name);
	}

	@Override
	public void calculate() {
		this.parent.removeReferenceDependencies(this);
		this.parent.addDependency(this, values);
	}

	// Externalizable
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeObject(values);
	}

	// Externalizable
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		values = (Area) in.readObject();
	}
}

class DiagramLineCreator extends DiagramCreator {
	@Override
	public Diagram factoryMethod(String name, Workbook workBook, DiagramChangedCallback diagramChangedCallback) {
		return new DiagramLine(name, workBook, diagramChangedCallback);
	}
}
