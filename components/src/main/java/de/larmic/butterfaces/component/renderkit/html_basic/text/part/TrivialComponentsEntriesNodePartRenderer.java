/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.text.part;

import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.util.ReflectionUtil;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.html.HtmlInputText;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
public class TrivialComponentsEntriesNodePartRenderer {

    /**
     * Renders a list of entities required by trivial components tree.
     *
     * @param nodes         a list of nodes that should be rendered
     * @param index         index of corresponding node (should match cachedNodes
     * @param mustacheKeys  optional mustache keys
     * @param cachedNodes   a map of node number to node
     * @param stringBuilder the builder
     * @return the last rendered index + 1
     */
    public int renderNodes(final StringBuilder stringBuilder,
                           final List<Node> nodes,
                           final int index,
                           final List<String> mustacheKeys,
                           final Map<Integer, Node> cachedNodes) {
        int newIndex = index;

        final Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()) {
            final Node node = iterator.next();
            newIndex = this.renderNode(stringBuilder, mustacheKeys, cachedNodes, newIndex, node, true);

            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }

        return newIndex;
    }

    public String renderEntriesAsJSON(final List<Node> nodes,
                                      final List<String> mustacheKeys,
                                      final Map<Integer, Node> cachedNodes) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        new TrivialComponentsEntriesNodePartRenderer().renderNodes(stringBuilder, nodes, 0, mustacheKeys, cachedNodes);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public String renderNode(final List<String> mustacheKeys,
                             final Map<Integer, Node> cachedNodes,
                             final int index,
                             final Node node) {
        final StringBuilder renderString = new StringBuilder();

        this.renderNode(renderString, mustacheKeys, cachedNodes, index, node, false);

        return renderString.toString();
    }

    public static String getEditingMode(final HtmlInputText text) {
        if (text.isReadonly()) {
            return "readonly";
        } else if (text.isDisabled()) {
            return "disabled";
        }

        return "editable";
    }

    private int renderNode(final StringBuilder stringBuilder,
                           final List<String> mustacheKeys,
                           final Map<Integer, Node> cachedNodes,
                           final int index,
                           final Node node,
                           final boolean renderChildren) {
        int newIndex = index;

        stringBuilder.append("{");
        stringBuilder.append("\"id\": " + newIndex + ",");
        if (StringUtils.isNotEmpty(node.getStyleClass())) {
            stringBuilder.append("\"styleClass\": \"" + escape(readValue(node.getStyleClass(), "styleClass", node.getData())) + "\",");
        }
        if (StringUtils.isNotEmpty(node.getImageIcon())) {
            stringBuilder.append("\"imageStyle\": \"background-image: url(" + node.getImageIcon() + ")\",");
        } else if (StringUtils.isNotEmpty(node.getGlyphiconIcon())) {
            stringBuilder.append("\"imageClass\": \"" + node.getGlyphiconIcon() + " glyphicon-node\",");
        } else {
            stringBuilder.append("\"imageStyle\": \"display:none\",");
        }

        if (StringUtils.isNotEmpty(node.getDescription())) {
            stringBuilder.append("\"description\": \"" + escape(readValue(node.getDescription(), "description", node.getData())) + "\",");
        }

        for (String mustacheKey : mustacheKeys) {
            stringBuilder.append("\"" + mustacheKey + "\": \"" + escape(new ReflectionUtil().getValueFromObject(node.getData(), mustacheKey)) + "\",");
        }

        stringBuilder.append("\"expanded\": " + Boolean.toString(!cachedNodes.get(newIndex).isCollapsed()) + ",");
        // TODO could toString() to be too expensive?
        stringBuilder.append("\"butterObjectToString\": \"" + escape(node.getData() != null ? node.getData().toString() : "") + "\",");
        stringBuilder.append("\"title\": \"" + escape(readValue(node.getTitle(), "title", node.getData())) + "\"");

        newIndex++;

        if (node.getSubNodes().size() > 0 && renderChildren) {
            stringBuilder.append(",\"children\": [");
            newIndex = renderNodes(stringBuilder, node.getSubNodes(), newIndex, mustacheKeys, cachedNodes);
            stringBuilder.append("]");
        }

        stringBuilder.append("}");
        return newIndex;
    }

    private String readValue(final String value,
                             final String attributeName,
                             final Object nodeValue) {
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        if (nodeValue == null) {
            return "";
        }

        final String valueFromObject = new ReflectionUtil().getValueFromObject(nodeValue, attributeName);

        return StringUtils.getNotNullValue(valueFromObject, "");
    }

    private String escape(String value) {
        return StringUtils.isNotEmpty(value) ? value.replace("\"", "\\\"") : "";
    }
}