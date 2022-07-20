package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class collectionElement {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Integer position;

    @Column
    private Integer attribute_id;

    @Column
    private String dataType;

    @Column
    private String value;

    @Column
    private Integer elementObjectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public collectionElement() {}

    public collectionElement(Integer position,Integer attribute_id,String dataType,String value,Integer elementObjectId) {
        this.position=position;
        this.attribute_id=attribute_id;
        this.dataType=dataType;
        this.value=value;
        this.elementObjectId=elementObjectId;
    }


    public Integer getPosition() {return position;}

    public String getDataType() {return dataType;}

    public Integer getAttribute_id() {return attribute_id;}

    public Integer getElementObjectId() {return elementObjectId;}

    public String getValue() {return value;}

}
