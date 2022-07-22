package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class CollectionElement extends PersistentEntity {
    /*@Column
    private Integer position;*/
    @Column
    private String dataType;
    @Column
    private String value;
    @Column
    private Integer elementObjectId;

    public CollectionElement() {}

    public CollectionElement(String dataType, String value, Integer elementObjectId) {
//        this.dataType=dataType;
        this.value=value;
        this.elementObjectId=elementObjectId;
    }


    //public Integer getPosition() {return position;}

//    public String getDataType() {return dataType;}
//
//    public Integer getAttribute_id() {return attribute_id;}

    public Integer getElementObjectId() {return elementObjectId;}

    public String getValue() {return value;}

}
