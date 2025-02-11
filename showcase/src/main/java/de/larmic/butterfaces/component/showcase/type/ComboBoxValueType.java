package de.larmic.butterfaces.component.showcase.type;

public enum ComboBoxValueType {
	STRING("string"),
	ENUM("enum"),
	OBJECT("object"),
	TEMPLATE("template");
	public final String label;

	ComboBoxValueType(final String label) {
		this.label = label;
	}
}
