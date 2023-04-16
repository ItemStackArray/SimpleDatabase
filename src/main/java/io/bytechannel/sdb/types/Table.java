package io.bytechannel.sdb.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Table {

    @Getter
    private final String name;
    @Getter
    private final List<Column> columns;


    public Table(final String name) {
        this.name = name;
        this.columns = new LinkedList<>();
    }

    public void createColumn(final Column column) {
        this.columns.add(column);
    }

}
