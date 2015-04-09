package fi.reaktor.android.rx.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.totallylazy.Sequence;

public class ImageSearch {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageSearchResponse {
        public final ResponseData responseData;

        @JsonCreator
        public ImageSearchResponse(@JsonProperty("responseData") ResponseData responseData) {
            this.responseData = responseData;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseData {
        public final Sequence<Image> results;

        @JsonCreator
        public ResponseData(@JsonProperty("results") Sequence<Image> results) {
            this.results = results;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        public final String url;

        @JsonCreator
        public Image(@JsonProperty("url") String url) {
            this.url = url;
        }
    }
}
