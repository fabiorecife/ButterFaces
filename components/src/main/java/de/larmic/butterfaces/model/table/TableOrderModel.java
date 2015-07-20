package de.larmic.butterfaces.model.table;

/**
 * Table model to handle column orderings (left and right). If model is not used it is not possible to change column
 * ordering by user interaction.
 */
public interface TableOrderModel {

    /**
     * Updates table column ordering. At all times it contains complete ordering information.
     */
    void update(final TableColumnOrdering ordering);

    /**
     * @return null if column identifier is not known, table identifier is not matching or no ordering is set.
     */
    Integer getOrderPosition(final String tableUniqueIdentifier, final String columnUniqueIdentifier);


}
