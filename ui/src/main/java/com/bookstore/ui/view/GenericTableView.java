package com.bookstore.ui.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * Generic, reusable TableView wrapper.
 * Supply column definitions and a filter predicate; call setItems() to populate.
 *
 * @param <T> the row model type
 */
public class GenericTableView<T> extends VBox {

    private final ObservableList<T>  items;
    private final FilteredList<T>    filtered;
    private final TableView<T>       tableView;
    private final StringProperty     countText = new SimpleStringProperty("0 kayıt");

    private BiPredicate<T, String>   filterPredicate;

    @SafeVarargs
    public GenericTableView(String emptyPlaceholder, TableColumn<T, ?>... columns) {
        this.items     = FXCollections.observableArrayList();
        this.filtered  = new FilteredList<>(items, t -> true);
        this.tableView = new TableView<>(filtered);

        tableView.getColumns().addAll(columns);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.setPlaceholder(new Label(emptyPlaceholder));

        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);
        getChildren().add(tableView);
    }

    public void setItems(List<T> newItems) {
        items.setAll(newItems);
        refreshCount();
    }

    /** Predicate receives the row item and the lower-cased search string. */
    public void setFilterPredicate(BiPredicate<T, String> predicate) {
        this.filterPredicate = predicate;
    }

    public void applyFilter(String query) {
        if (filterPredicate == null || query == null || query.isBlank()) {
            filtered.setPredicate(t -> true);
        } else {
            String lower = query.toLowerCase();
            filtered.setPredicate(t -> filterPredicate.test(t, lower));
        }
        refreshCount();
    }

    public void clearFilter() {
        filtered.setPredicate(t -> true);
        refreshCount();
    }

    private void refreshCount() {
        int shown = filtered.size(), total = items.size();
        countText.set(shown == total ? total + " kayıt" : shown + " / " + total + " kayıt");
    }

    public T getSelectedItem() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    public TableView<T>      getTableView()  { return tableView; }
    public ObservableList<T> getItems()      { return items; }
    public StringProperty    countProperty() { return countText; }
    public String            getCountText()  { return countText.get(); }
}
