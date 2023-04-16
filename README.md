# SimpleDatabase
SimpleDatabase -> easily to create databases, tables, columns, update and get them.



# Simple Guide how to use it:
        final SimpleDatabase simpleDatabase = new SimpleDatabase("127.0.0.1", 3306, "admin", "admin"); // SETS THE DATABASE CONNECTION

        final Table usersTable = new Table("users"); // TABLE INSTANCE
        final Column nameColumn = new Column("NAME", "VARCHAR(16),"); // COLUMN INSTANCE
        final Column killsColumn = new Column("MONEY", "BIGINT NOT NULL DEFAULT 0"); // ANOTHER COLUMN INSTANCE
        usersTable.createColumn(nameColumn); // CREATES THE COLUMN IN THE TABLE
        usersTable.createColumn(killsColumn); // CREATES THE COLUMN IN THE TABLE


        simpleDatabase.addDatabase(new Database("SDBTest", new Table[]{usersTable})); // FINALIZES / SETS THE DATABASE
        simpleDatabase.postInit(); // CREATES THE DATABASE WITH THE TABLES


        if(simpleDatabase.columnExists(usersTable, "NAME", "FRANZ")) { // CHECKS IF THE COLUMN "NAME" OF "FRANZ" IS NULL / EXISTING
            simpleDatabase.addColumn(usersTable, "NAME", "FRANZ"); // FILLS THE COLUM WITH "FRANZ"
        }
        simpleDatabase.updateColumn(usersTable, "MONEY", "NAME", "FRANZ", (long)100); // UPDATES THE COLUMN "MONEY" FROM "FRANZ" TO "500"

        System.out.println(simpleDatabase.getColumn(usersTable, "MONEY", "NAME", "FRANZ")); // RETURNS THE "MONEY" WHERE THE "NAME" IS "FRANZ"
        
