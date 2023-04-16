package io.bytechannel.sdb.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@AllArgsConstructor
public class Database {

    @Getter
    private final String name;
    @Getter
    private final Table[] tables;
}
