package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.html.table.HtmlTableToolbar;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.model.json.JsonToModelConverter;
import de.larmic.butterfaces.model.table.TableColumnOrdering;
import de.larmic.butterfaces.model.table.TableColumnVisibility;
import de.larmic.butterfaces.resolver.AjaxRequest;
import de.larmic.butterfaces.resolver.AjaxRequestFactory;
import de.larmic.butterfaces.resolver.UIComponentResolver;
import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlTableToolbar.COMPONENT_FAMILY, rendererType = HtmlTableToolbar.RENDERER_TYPE)
public class TableToolbarRenderer extends HtmlBasicRenderer {

    private HtmlTable cachedTableComponent;
    private WebXmlParameters webXmlParameters;

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlTableToolbar tableHeader = (HtmlTableToolbar) component;
        final ResponseWriter responseWriter = context.getResponseWriter();
        this.cachedTableComponent = new UIComponentResolver().findComponent(tableHeader.getTableId(), HtmlTable.class);

        if (cachedTableComponent == null) {
            throw new IllegalStateException("Could not find table component with id '" + tableHeader.getTableId() + "'.");
        }

        webXmlParameters = new WebXmlParameters(context.getExternalContext());

        responseWriter.startElement("div", tableHeader);
        this.writeIdAttribute(context, responseWriter, tableHeader);
        responseWriter.writeAttribute("class", "butter-table-toolbar", null);
        responseWriter.writeAttribute("data-table-html-id", cachedTableComponent.getClientId(), null);
    }

    @Override
    public void encodeChildren(final FacesContext context,
                               final UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            final ResponseWriter responseWriter = context.getResponseWriter();

            responseWriter.startElement("div", component);
            responseWriter.writeAttribute("class", "butter-table-toolbar-custom pull-left", null);
            super.encodeChildren(context, component);
            responseWriter.endElement("div");
        }
    }

    @Override
    public void encodeEnd(final FacesContext context,
                          final UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final HtmlTableToolbar tableHeader = (HtmlTableToolbar) component;
        final ResponseWriter responseWriter = context.getResponseWriter();

        responseWriter.startElement("div", tableHeader); // start right toolbar
        responseWriter.startElement("div", tableHeader); // start button group
        responseWriter.writeAttribute("class", "btn-group pull-right table-toolbar-default", null);

        this.renderFacet(context, component, "default-options-left");
        this.renderTableToolbarRefreshButton(responseWriter, tableHeader);
        this.renderFacet(context, component, "default-options-center");
        this.renderTableToolbarToggleColumnButton(responseWriter, tableHeader);
        this.renderFacet(context, component, "default-options-right");

        responseWriter.endElement("div"); // end button group
        responseWriter.endElement("div"); // end right toolbar

        responseWriter.endElement("div");

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "fixBootstrapDropDown()", responseWriter, component);
    }

    private void renderFacet(final FacesContext context, final UIComponent component, final String facetName) throws IOException {
        final UIComponent leftFacet = this.getFacet(component, facetName);

        if (leftFacet != null) {
            leftFacet.encodeAll(context);
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final HtmlTableToolbar htmlTableHeader = (HtmlTableToolbar) component;
        final Map<String, List<ClientBehavior>> behaviors = htmlTableHeader.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");
        final String tableUniqueIdentifier = cachedTableComponent.getModelUniqueIdentifier();

        if (behaviorEvent != null) {
            if (HtmlTableToolbar.EVENT_TOGGLE_COLUMN.equals(behaviorEvent) && this.cachedTableComponent.getTableColumnVisibilityModel() != null) {
                final TableColumnVisibility visibility = new JsonToModelConverter().convertTableColumnVisibility(tableUniqueIdentifier, params.get("params"));
                cachedTableComponent.getTableColumnVisibilityModel().update(visibility);
            } else if (behaviorEvent.equals(HtmlTableToolbar.EVENT_REFRESH_TABLE)) {
                if (htmlTableHeader.getTableToolbarRefreshListener() != null) {
                    htmlTableHeader.getTableToolbarRefreshListener().onPreRefresh();
                }
            } else if (HtmlTableToolbar.EVENT_ORDER_COLUMN.equals(behaviorEvent) && cachedTableComponent.getTableOrderModel() != null) {
                final TableColumnOrdering ordering = new JsonToModelConverter().convertTableColumnOrdering(tableUniqueIdentifier, params.get("params"));
                cachedTableComponent.getTableOrderModel().update(ordering);
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void renderTableToolbarToggleColumnButton(final ResponseWriter writer,
                                                      final HtmlTableToolbar tableToolbar) throws IOException {
        final AjaxRequest toggleAjaxRequest = new AjaxRequestFactory().createRequest(tableToolbar, HtmlTableToolbar.EVENT_TOGGLE_COLUMN);
        final AjaxRequest orderAjaxRequest = new AjaxRequestFactory().createRequest(tableToolbar, HtmlTableToolbar.EVENT_ORDER_COLUMN);

        if (toggleAjaxRequest != null || orderAjaxRequest != null) {
            if (toggleAjaxRequest != null) {
                toggleAjaxRequest.getRenderIds().add(cachedTableComponent.getClientId());
            }
            if (orderAjaxRequest != null) {
                orderAjaxRequest.getRenderIds().add(cachedTableComponent.getClientId());
            }

            writer.startElement("div", tableToolbar);
            writer.writeAttribute("class", "btn-group", null);

            // show and hide option toggle
            writer.startElement("a", tableToolbar);
            writer.writeAttribute("class", "btn btn-default dropdown-toggle", null);
            writer.writeAttribute("data-toggle", "dropdown", null);
            writer.writeAttribute("title", tableToolbar.getColumnOptionsTooltip(), null);
            writer.writeAttribute("role", "button", null);
            writer.startElement("i", tableToolbar);
            writer.writeAttribute("class", webXmlParameters.getOptionsGlyphicon(), null);
            writer.endElement("i");
            writer.startElement("span", tableToolbar);
            writer.writeAttribute("class", "caret", null);
            writer.endElement("span");
            writer.endElement("a");

            // show and hide option content
            writer.startElement("ul", tableToolbar);
            writer.writeAttribute("class", "dropdown-menu dropdown-menu-form butter-table-toolbar-columns", null);
            writer.writeAttribute("role", "menu", null);

            int columnNumber = 0;
            for (HtmlColumn cachedColumn : this.cachedTableComponent.getCachedColumns()) {
                writer.startElement("li", tableToolbar);
                writer.writeAttribute("class", "butter-table-toolbar-column-option", "styleClass");
                writer.writeAttribute("data-original-column", columnNumber, null);
                writer.writeAttribute("data-column-model-identifier", cachedColumn.getModelUniqueIdentifier(), null);

                if (toggleAjaxRequest != null) {
                    this.renderToggleColumnInput(writer, tableToolbar, toggleAjaxRequest.getRenderIds(), cachedColumn);
                }

                writer.startElement("label", tableToolbar);
                writer.writeAttribute("class", "checkbox", "styleClass");
                writer.writeAttribute("title", cachedColumn.getLabel(), "title");
                writer.writeText(cachedColumn.getLabel(), null);
                writer.endElement("label");

                if (orderAjaxRequest != null && cachedTableComponent.getTableOrderModel() != null) {
                    this.renderOrderColumnSpan(writer, tableToolbar, orderAjaxRequest.getRenderIds(), columnNumber);
                }

                writer.endElement("li");
                columnNumber++;
            }
            writer.endElement("ul");

            writer.endElement("div");
        }
    }

    private void renderOrderColumnSpan(final ResponseWriter writer,
                                       final HtmlTableToolbar tableToolbar,
                                       final List<String> renderIds,
                                       final int columnNumber) throws IOException {
        final String ajaxColumnOrderLeft = createModelJavaScriptCall(tableToolbar, renderIds, "orderColumn", "true, " + columnNumber);
        final String ajaxColumnOrderRight = createModelJavaScriptCall(tableToolbar, renderIds, "orderColumn", "false, " + columnNumber);

        writer.startElement("span", tableToolbar);
        writer.writeAttribute("class", "butter-table-toolbar-column-order-item butter-table-toolbar-column-order-item-up " + webXmlParameters.getOrderLeftGlyphicon(), "styleClass");
        writer.writeAttribute("onclick", ajaxColumnOrderLeft, null);
        writer.endElement("span");
        writer.startElement("span", tableToolbar);
        writer.writeAttribute("class", "butter-table-toolbar-column-order-item butter-table-toolbar-column-order-item-down " + webXmlParameters.getOrderRightGlyphicon(), "styleClass");
        writer.writeAttribute("onclick", ajaxColumnOrderRight, null);
        writer.endElement("span");
    }

    private void renderToggleColumnInput(final ResponseWriter writer,
                                         final HtmlTableToolbar tableToolbar,
                                         final List<String> renderIds,
                                         final HtmlColumn cachedColumn) throws IOException {
        writer.startElement("input", tableToolbar);
        writer.writeAttribute("type", "checkbox", null);

        final String ajax = createModelJavaScriptCall(tableToolbar, renderIds, "toggleColumnVisibilty", null);

        writer.writeAttribute("onclick", ajax, null);

        if (!this.isHideColumn(this.cachedTableComponent, cachedColumn)) {
            writer.writeAttribute("checked", "checked", null);
        }
        writer.endElement("input");
    }

    private String createModelJavaScriptCall(final HtmlTableToolbar tableToolbar,
                                             final List<String> renderIds,
                                             final String javaScriptMethodName,
                                             final String optionalParameter) {
        final StringBuilder ajax = new StringBuilder("jQuery(document.getElementById('");
        ajax.append(tableToolbar.getClientId());
        ajax.append("'))." + javaScriptMethodName + "([");
        for (String renderId : renderIds) {
            ajax.append("'");
            ajax.append(renderId);
            ajax.append("'");
        }
        if (StringUtils.isNotEmpty(optionalParameter)) {
            ajax.append("], " + tableToolbar.isAjaxDisableRenderRegionsOnRequest() + ", " + optionalParameter + ");");
        } else {
            ajax.append("], " + tableToolbar.isAjaxDisableRenderRegionsOnRequest() + ");");
        }
        return ajax.toString();
    }

    private boolean isHideColumn(final HtmlTable table, final HtmlColumn column) {
        if (table.getTableColumnVisibilityModel() != null) {
            final String tableUniqueIdentifier = table.getModelUniqueIdentifier();
            final String columnUniqueIdentifier = column.getModelUniqueIdentifier();
            final Boolean hideColumn = table.getTableColumnVisibilityModel().isColumnHidden(tableUniqueIdentifier, columnUniqueIdentifier);
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
    }

    private void renderTableToolbarRefreshButton(final ResponseWriter writer,
                                                 final HtmlTableToolbar tableToolbar) throws IOException {
        final AjaxRequest ajaxRequest = new AjaxRequestFactory().createRequest(tableToolbar, "refresh");


        if (ajaxRequest != null) {
            ajaxRequest.getRenderIds().add(cachedTableComponent.getClientId());
            final String ajaxCall = ajaxRequest.createJavaScriptCall("refresh", tableToolbar.isAjaxDisableRenderRegionsOnRequest());

            writer.startElement("a", tableToolbar);
            writer.writeAttribute("class", "btn btn-default", null);
            writer.writeAttribute("role", "button", null);
            writer.writeAttribute("title", tableToolbar.getRefreshTooltip(), null);
            writer.writeAttribute("onclick", ajaxCall, null);

            writer.startElement("i", tableToolbar);
            writer.writeAttribute("class", webXmlParameters.getRefreshGlyphicon(), null);
            writer.endElement("i");

            writer.endElement("a");
        }
    }
}
