package org.tweetsum;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

class Tweet {

    private String text;

    public String getText() { return text; }

    // Sun Apr 03 20:24:49 +0000 2011
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "EEE MMM dd HH:mm:ss Z yyyy",
                locale = "en",
                timezone = "GMT")
    private Timestamp time;

    public Timestamp getTime() { return time; }

    public String toString() { return time + ": " + text; }
}