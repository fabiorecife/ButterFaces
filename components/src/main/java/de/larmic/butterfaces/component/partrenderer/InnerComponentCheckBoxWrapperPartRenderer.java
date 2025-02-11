package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlRadioBox;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class InnerComponentCheckBoxWrapperPartRenderer {

    public void renderInnerWrapperBegin(final HtmlCheckBox component, final ResponseWriter writer)
            throws IOException {
        if (!component.isReadonly()) {
            final StringBuilder defaultStyleClass = new StringBuilder();
            if (component.isHideLabel()) {
                defaultStyleClass.append("butter-component-value-hiddenLabel");
            } else {
                defaultStyleClass.append("butter-component-value");
            }

            defaultStyleClass.append(" butter-component-checkbox");

            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
            writer.writeAttribute("class", defaultStyleClass.toString(), null);

            if (!StringUtils.isEmpty(component.getDescription())) {
                writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
                writer.writeAttribute("class", "checkbox withDescription", null);
                writer.startElement("label", component);
            }
        }
    }

    public void renderInnerWrapperEnd(final HtmlCheckBox component, final ResponseWriter writer)
            throws IOException {
        if (!component.isReadonly()) {
            if (!StringUtils.isEmpty(component.getDescription())) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "butter-component-checkbox-description", null);
                writer.writeText(component.getDescription(), null);
                writer.endElement("span");
                writer.endElement("label");
                writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
            }

            renderMojarraFix(component, writer);

            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }

    public static void renderMojarraFix(UIComponent component, ResponseWriter writer) throws IOException {
        if (component instanceof HtmlComboBox || component instanceof HtmlRadioBox) {
            final Set<String> eventNames = ((UIInput) component).getClientBehaviors().keySet();
            final Iterator<String> eventNamesIterator = eventNames.iterator();

            if (eventNamesIterator.hasNext()) {
                final StringBuilder sb = new StringBuilder("[");

                while (eventNamesIterator.hasNext()) {
                    sb.append("'");
                    sb.append(eventNamesIterator.next());
                    sb.append("'");

                    if (eventNamesIterator.hasNext()) {
                        sb.append(", ");
                    }
                }

                sb.append("]");

                final String function = "butter.fix.updateMojarraScriptSourceId('" + component.getClientId() + "', " + sb.toString() + ");";
                RenderUtils.renderJavaScriptCall(function, writer, component);
            }
        }
    }
}
