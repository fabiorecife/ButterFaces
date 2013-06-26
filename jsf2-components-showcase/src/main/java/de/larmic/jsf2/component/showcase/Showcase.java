package de.larmic.jsf2.component.showcase;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class Showcase implements Serializable {

	private boolean renderedTextComponent = true;
	private boolean readonlyTextComponent;
	private boolean requiredTextComponent;
	private boolean floatingTextComponent;
	private boolean validateTextComponent;
	private boolean ajaxTextComponent;
	private String tooltipTextComponent;
	private String labelTextComponent = "label";
	private String valueTextComponent = "value";

	public boolean isReadonlyTextComponent() {
		return this.readonlyTextComponent;
	}

	public void setReadonlyTextComponent(final boolean readonlyTextComponent) {
		this.readonlyTextComponent = readonlyTextComponent;
	}

	public boolean isRequiredTextComponent() {
		return this.requiredTextComponent;
	}

	public void setRequiredTextComponent(final boolean requiredTextComponent) {
		this.requiredTextComponent = requiredTextComponent;
	}

	public boolean isFloatingTextComponent() {
		return this.floatingTextComponent;
	}

	public void setFloatingTextComponent(final boolean floatingTextComponent) {
		this.floatingTextComponent = floatingTextComponent;
	}

	public String getLabelTextComponent() {
		return this.labelTextComponent;
	}

	public void setLabelTextComponent(final String labelTextComponent) {
		this.labelTextComponent = labelTextComponent;
	}

	public String getValueTextComponent() {
		return this.valueTextComponent;
	}

	public void setValueTextComponent(final String valueTextComponent) {
		this.valueTextComponent = valueTextComponent;
	}

	public boolean isValidateTextComponent() {
		return this.validateTextComponent;
	}

	public void setValidateTextComponent(final boolean validateTextComponent) {
		this.validateTextComponent = validateTextComponent;
	}

	public String getTooltipTextComponent() {
		return this.tooltipTextComponent;
	}

	public void setTooltipTextComponent(final String tooltipTextComponent) {
		this.tooltipTextComponent = tooltipTextComponent;
	}

	public boolean isRenderedTextComponent() {
		return this.renderedTextComponent;
	}

	public void setRenderedTextComponent(final boolean renderedTextComponent) {
		this.renderedTextComponent = renderedTextComponent;
	}

	public boolean isAjaxTextComponent() {
		return this.ajaxTextComponent;
	}

	public void setAjaxTextComponent(final boolean ajaxTextComponent) {
		this.ajaxTextComponent = ajaxTextComponent;
	}

	public String getTextComponentCode() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<l:text id=\"input\"\n");
		sb.append("        label=\"" + this.labelTextComponent + "\"\n");
		sb.append("        value=\"" + this.valueTextComponent + "\"\n");
		if (this.tooltipTextComponent != null && !"".equals(this.tooltipTextComponent)) {
			sb.append("        tooltip=\"" + this.tooltipTextComponent + "\"\n");
		}
		sb.append("        readonly=\"" + this.readonlyTextComponent + "\"\n");
		sb.append("        required=\"" + this.requiredTextComponent + "\"\n");
		sb.append("        floating=\"" + this.floatingTextComponent + "\"\n");
		sb.append("        rendered=\"" + this.renderedTextComponent + "\">\n");
		if (this.ajaxTextComponent) {
			sb.append("    <f:ajax event=\"keyup\" \n");
			sb.append("            execute=\"input\"\n");
			sb.append("            render=\"output\"/>\n");
		}
		if (this.validateTextComponent) {
			sb.append("    <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("</l:text>");

		if (this.ajaxTextComponent) {
			sb.append("\n");
			sb.append("<h:outputText id=\"output\" value=\"" + this.valueTextComponent + "\"/>");
		}
		return sb.toString();
	}

	public String getTextComponentCSS() {
		final StringBuilder sb = new StringBuilder();

		sb.append(".larmic-component-label {\n");
		sb.append("    width: 50px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-required {\n");
		sb.append("    /* nothing */\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-input {\n");
		sb.append("    font-size: 14px !important;\n");
		sb.append("    height: 18px;\n");
		sb.append("    width: 150px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".input-invalid {\n");
		sb.append("    background-color: #FFEBDA !important;\n");
		sb.append("    border-color: #FF0044;\n");
		sb.append("    border-style: solid;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-tooltip {\n");
		sb.append("    border-radius: 3px;\n");
		sb.append("    box-shadow: 2px 2px 3px #AAAAAA;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-error-message {\n");
		sb.append("    background-color: #FFEBDA;\n");
		sb.append("    border: 1px solid #FF0044;\n");
		sb.append("    border-radius: 3px;\n");
		sb.append("    margin: 5px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-error-message li {\n");
		sb.append("    list-style: disc outside none;\n");
		sb.append("    margin-left: 20px;\n");
		sb.append("    color: #555555;\n");
		sb.append("    font-style: italic;\n");
		sb.append("}\n");

		return sb.toString();
	}
}