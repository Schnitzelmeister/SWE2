package at.ac.univie.swe2.SS2017.team403;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * 
 *
 */
public abstract class Diagram implements Externalizable {
	public Diagram() {
		diagramChangedCallback = null;
	}

	protected String name;
	protected Workbook parent;
	protected DiagramChangedCallback diagramChangedCallback;

	Diagram(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback) {
		this();
		this.name = name;
		this.parent = wbk;
		this.diagramChangedCallback = diagramChangedCallback;
	}

	public String getName() {
		return name;
	}

	public abstract void calculate();

	public void setName(String name) throws IllegalArgumentException {
		if (this.name.equals(name))
			return;
		// check if diagram with new name exists
		if (parent.getDiagrams().containsKey(name))
			throw new IllegalArgumentException("Diagram with name " + name + " already exists");

		// check if sheet with new name exists
		if (parent.getWorksheets().containsKey(name))
			throw new IllegalArgumentException("Sheet with name " + name + " already exists");

		String oldName = this.name;
		this.name = name;

		// inform workbook
		if (diagramChangedCallback != null)
			diagramChangedCallback.afterDiagramRenamed(oldName, this);

	}

	// Externalizable
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(name);
	}

	// Externalizable
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		name = in.readUTF();
	}
}

abstract class DiagramCreator {
	public abstract Diagram factoryMethod(String name, Workbook wbk, DiagramChangedCallback diagramChangedCallback);
}