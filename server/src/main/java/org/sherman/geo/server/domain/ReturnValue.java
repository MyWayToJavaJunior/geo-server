package org.sherman.geo.server.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.lang.*;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnValue<T> {
    protected final T result;
    protected final Error error;

    public ReturnValue(T value) {
        this.result = value;
        error = null;
    }

    public ReturnValue(Error error) {
        this.error = error;
        result = null;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Error getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    @JsonIgnore
    public boolean hasError() {
        return error != null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null == object) {
            return false;
        }

        @SuppressWarnings("unchecked")
        ReturnValue<T> o = (ReturnValue<T>) object;

        return
                Objects.equal(result, o.result)
                        && Objects.equal(error, o.error);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(result, error);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(result)
                .addValue(error)
                .toString();
    }

    @JsonCreator
    public static <T> ReturnValue<T> create(
            @JsonProperty("result") T value,
            @JsonProperty("error") Error error
    ) {
        if (null != value) {
            return new ReturnValue<>(value);
        } else {
            return new ReturnValue<>(error);
        }
    }
}
