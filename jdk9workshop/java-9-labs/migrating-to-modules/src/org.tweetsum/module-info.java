module org.tweetsum {
    requires java.base;
    requires jackson.core;
    requires jackson.databind;
    requires jackson.annotations;
    requires java.sql;

    opens org.tweetsum to jackson.databind;
}