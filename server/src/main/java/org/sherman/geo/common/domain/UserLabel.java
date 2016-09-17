package org.sherman.geo.common.domain;

import com.github.davidmoten.geo.LatLong;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public class UserLabel {
    private final long userId;
    private final LatLong coords;

    public UserLabel(long userId, LatLong coords) {
        this.userId = userId;
        this.coords = coords;
    }

    public long getUserId() {
        return userId;
    }

    public LatLong getCoords() {
        return coords;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLabel that = (UserLabel) o;

        return Objects.equal(this.userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("coords", coords)
                .toString();
    }
}
