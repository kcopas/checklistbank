package org.gbif.checklistbank.model;

import java.util.Date;
import java.util.UUID;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Different model for a verbatim name usage record as it is stored in postgres and used in the mybatis DAO layer.
 */
public class RawUsage {
    private Integer usageKey;
    private UUID datasetKey;
    private String json;
    private Date lastCrawled;

    public Integer getUsageKey() {
        return usageKey;
    }

    public void setUsageKey(Integer usageKey) {
        this.usageKey = usageKey;
    }

    public UUID getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(UUID datasetKey) {
        this.datasetKey = datasetKey;
    }

    public Date getLastCrawled() {
        return lastCrawled;
    }

    public void setLastCrawled(Date lastCrawled) {
        this.lastCrawled = lastCrawled;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RawUsage) {
            RawUsage that = (RawUsage) obj;
            return Objects.equal(this.usageKey, that.usageKey)
                    && Objects.equal(this.datasetKey, that.datasetKey)
                    && Objects.equal(this.lastCrawled, that.lastCrawled)
                    && Objects.equal(this.json, that.json);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usageKey, datasetKey, json, lastCrawled);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("usageKey", usageKey)
                .add("datasetKey", datasetKey)
                .add("json", json)
                .add("lastCrawled", lastCrawled)
                .toString();
    }
}
