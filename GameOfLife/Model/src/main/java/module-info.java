module Model {
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires java.logging;
    requires jdk.jdi;
    requires org.postgresql.jdbc;
    requires java.sql;
    exports pl.lodz.p;
    exports exceptions;

}