package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ResourceDependencies({
        @ResourceDependency(library = "css", name = "butterfaces.css", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.2.0.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.2.0.min.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlNumber.COMPONENT_TYPE)
public class HtmlNumber extends HtmlInputText implements HtmlInputComponent {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.number";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.NumberRenderer";

    protected static final String PROPERTY_TOOLTIP = "tooltip";
    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_STYLE_CLASS = "componentStyleClass";
    protected static final String PROPERTY_INPUT_STYLE_CLASS = "inputStyleClass";
    protected static final String PROPERTY_LABEL_STYLE_CLASS = "labelStyleClass";
    protected static final String PROPERTY_HTML5_PLACEHOLDER = "placeholder";
    protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";
    protected static final String PROPERTY_HTML5_MIN = "min";
    protected static final String PROPERTY_HTML5_MAX = "max";

    public HtmlNumber() {
        super();
        this.setRendererType(RENDERER_TYPE);

        addValidator(new Validator() {
            @Override
            public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
                try {
                    final long longValue = Long.parseLong(value.toString());

                    if (getMin() != null && !"".equals(getMin()) && longValue < Integer.valueOf(getMin())) {
                        throw new ValidatorException(new FacesMessage("Number is to small", String.format("%s is to small", value)));
                    }

                    if (getMax() != null && !"".equals(getMax()) && longValue > Integer.valueOf(getMax())) {
                        throw new ValidatorException(new FacesMessage("Number is to big", String.format("%s is to big", value)));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidatorException(new FacesMessage("No number", String.format("%s is no number", value)));
                }
            }
        });
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getTooltip() {
        return (String) this.getStateHelper().eval(PROPERTY_TOOLTIP);
    }

    public void setTooltip(final String tooltip) {
        this.updateStateHelper(PROPERTY_TOOLTIP, tooltip);
    }

    @Override
    public boolean getHideLabel() {
        return (Boolean) this.getStateHelper().eval(PROPERTY_HIDE_LABEL, false);
    }

    public void setHideLabel(final String hideLabel) {
        this.updateStateHelper(PROPERTY_HIDE_LABEL, hideLabel);
    }

    public String getPlaceholder() {
        return (String) this.getStateHelper().eval(PROPERTY_HTML5_PLACEHOLDER);
    }

    public void setPlaceholder(final String placeholder) {
        this.updateStateHelper(PROPERTY_HTML5_PLACEHOLDER, placeholder);
    }

    public String getMin() {
        return (String) this.getStateHelper().eval(PROPERTY_HTML5_MIN);
    }

    public void setMin(final String min) {
        this.updateStateHelper(PROPERTY_HTML5_MIN, min);
    }

    public String getMax() {
        return (String) this.getStateHelper().eval(PROPERTY_HTML5_MAX);
    }

    public void setMax(final String max) {
        this.updateStateHelper(PROPERTY_HTML5_MAX, max);
    }

    public boolean getAutoFocus() {
        return (Boolean) this.getStateHelper().eval(PROPERTY_HTML5_AUTO_FOCUS, false);
    }

    public void setAutoFocus(final Boolean autoFocus) {
        this.updateStateHelper(PROPERTY_HTML5_AUTO_FOCUS, autoFocus);
    }

    @Override
    public String getComponentStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
    }

    public void setComponentStyleClass(final String styleClass) {
        this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
    }

    @Override
    public String getInputStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_INPUT_STYLE_CLASS);
    }

    public void setInputStyleClass(final String inputStyleClass) {
        this.updateStateHelper(PROPERTY_INPUT_STYLE_CLASS, inputStyleClass);
    }

    @Override
    public String getLabelStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_LABEL_STYLE_CLASS);
    }

    public void setLabelStyleClass(final String labelStyleClass) {
        this.updateStateHelper(PROPERTY_LABEL_STYLE_CLASS, labelStyleClass);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
