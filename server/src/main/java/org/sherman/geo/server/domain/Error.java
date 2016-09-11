package org.sherman.geo.server.domain;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public class Error {
    private final ErrorCode code;
    private final String message;
    private final String originalMessage;

    public Error(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
        this.originalMessage = null;
    }

    public Error(ErrorCode code, String message, String originalMessage) {
        this.code = code;
        this.message = message;
        this.originalMessage = originalMessage;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null == object) {
            return false;
        }

        if (!(object instanceof Error)) {
            return false;
        }

        Error o = (Error) object;

        return equal(code, o.code)
                && equal(message, o.message)
                && equal(originalMessage, o.originalMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code, message, originalMessage);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(code)
                .addValue(message)
                .addValue(originalMessage)
                .toString();
    }

    public static Error createError(@NotNull ErrorCode errorCode, @NotNull String errorMessage) {
        return new Error(errorCode, errorMessage);
    }
}
