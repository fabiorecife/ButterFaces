package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlCommandLink;

/**
 * Created by larmic on 16.09.14.
 */
@FacesComponent(HtmlGlyphiconCommandLink.COMPONENT_TYPE)
public class HtmlGlyphiconCommandLink extends HtmlCommandLink{

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.glyphiconCommandLink";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.GlyphiconCommandLinkRenderer";

    protected static final String PROPERTY_GLYPHICON = "glyphicon";

    public HtmlGlyphiconCommandLink() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getGlyphicon() {
        return (String) this.getStateHelper().eval(PROPERTY_GLYPHICON);
    }

    public void setGlyphicon(final String glyphicon) {
        this.updateStateHelper(PROPERTY_GLYPHICON, glyphicon);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
