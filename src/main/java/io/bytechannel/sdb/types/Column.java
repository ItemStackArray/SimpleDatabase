package io.bytechannel.sdb.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Column {

    @Getter
    private final String name;
    @Getter
    private String data;
}
