package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class CollectionElement extends PersistentEntity {
    @Column
    private String dataType;
    @Column
    private String value;
    @Column
    private Integer elementObjectId;

    public CollectionElement() {}

    public CollectionElement(String dataType, String value, Integer elementObjectId) {
        this.dataType=dataType;
        this.value=value;
        this.elementObjectId=elementObjectId;
    }

    public String getDataType() {
        return dataType;
    }

    public String getValue() {
        return value;
    }

    public Integer getElementObjectId() {
        return elementObjectId;
    }
}
