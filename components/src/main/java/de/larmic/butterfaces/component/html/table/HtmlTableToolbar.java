package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.util.StringUtils;
import de.larmic.butterfaces.model.table.TableToolbarRefreshListener;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
       @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-table-toolbar.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-bootstrap-fixes.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-overlay.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-guid.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-overlay.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-ajax.js", target = "head")
})
@FacesComponent(HtmlTableToolbar.COMPONENT_TYPE)
public class HtmlTableToolbar extends UIComponentBase implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.table.toolbar";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TableHeaderRenderer";

    public static final String EVENT_REFRESH_TABLE = "refresh";
    public static final String EVENT_TOGGLE_COLUMN = "toggle";
    public static final String EVENT_ORDER_COLUMN = "order";

    protected static final String PROPERTY_TABLE_ID = "tableId";
    protected static final String PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST = "ajaxDisableRenderRegionsOnRequest";
    protected static final String PROPERTY_TOOLBAR_REFRESH_LISTENER = "refreshListener";
    protected static final String PROPERTY_REFRESH_TOOLTIP = "refreshTooltip";
    protected static final String PROPERTY_COLUMN_OPTIONS_TOOLTIP = "columnOptionsTooltip";


    public HtmlTableToolbar() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList(EVENT_REFRESH_TABLE, EVENT_TOGGLE_COLUMN, EVENT_ORDER_COLUMN);
    }

    @Override
    public String getDefaultEventName() {
        return "refresh";
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getTableId() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_ID);
    }

    public void setTableId(String tableId) {
        this.updateStateHelper(PROPERTY_TABLE_ID, tableId);
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST, ajaxDisableRenderRegionsOnRequest);
    }

    public TableToolbarRefreshListener getTableToolbarRefreshListener() {
        return (TableToolbarRefreshListener) this.getStateHelper().eval(PROPERTY_TOOLBAR_REFRESH_LISTENER);
    }

    public void setTableToolbarRefreshListener(TableToolbarRefreshListener tableToolbarRefreshListener) {
        this.updateStateHelper(PROPERTY_TOOLBAR_REFRESH_LISTENER, tableToolbarRefreshListener);
    }

    public String getRefreshTooltip() {
        final String value = (String) this.getStateHelper().eval(PROPERTY_REFRESH_TOOLTIP);
        return StringUtils.isNotEmpty(value) ? value : "Refresh table";
    }

    public void setRefreshTooltip(String refreshTooltip) {
        this.updateStateHelper(PROPERTY_REFRESH_TOOLTIP, refreshTooltip);
    }

    public String getColumnOptionsTooltip() {
        final String value = (String) this.getStateHelper().eval(PROPERTY_COLUMN_OPTIONS_TOOLTIP);
        return StringUtils.isNotEmpty(value) ? value : "Column options";
    }

    public void setColumnOptionsTooltip(String columnOptionsTooltip) {
        this.updateStateHelper(PROPERTY_COLUMN_OPTIONS_TOOLTIP, columnOptionsTooltip);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
