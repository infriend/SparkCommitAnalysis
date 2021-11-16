package com.nju.sparkcommitanalysis.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tbPersioncommit
 */
@Data
public class Tbpersioncommit implements Serializable {
    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String commitYear;

    /**
     * 
     */
    private Integer commitCounter;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Tbpersioncommit other = (Tbpersioncommit) that;
        return (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getCommitYear() == null ? other.getCommitYear() == null : this.getCommitYear().equals(other.getCommitYear()))
            && (this.getCommitCounter() == null ? other.getCommitCounter() == null : this.getCommitCounter().equals(other.getCommitCounter()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getCommitYear() == null) ? 0 : getCommitYear().hashCode());
        result = prime * result + ((getCommitCounter() == null) ? 0 : getCommitCounter().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", name=").append(name);
        sb.append(", commitYear=").append(commitYear);
        sb.append(", commitCounter=").append(commitCounter);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}