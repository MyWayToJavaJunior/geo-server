package org.sherman.geo.common.domain;

import com.github.davidmoten.geo.GeoHash;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public class IndexedUserLabel {
    private final UserLabel label;
    private final String hash;

    public IndexedUserLabel(UserLabel label, int maxLength) {
        this.label = label;
        this.hash = GeoHash.encodeHash(label.getCoords(), maxLength);
    }

    public UserLabel getLabel() {
        return label;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedUserLabel that = (IndexedUserLabel) o;

        return Objects.equal(this.label, that.label) &&
                Objects.equal(this.hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label, hash);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("label", label)
                .add("hash", hash)
                .toString();
    }
}
