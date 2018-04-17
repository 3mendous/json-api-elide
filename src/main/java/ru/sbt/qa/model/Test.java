package ru.sbt.qa.model;

import com.yahoo.elide.annotation.Include;
import javax.persistence.*;

@Entity
@Table(name = "test")
@Include(rootLevel = true)
public class Test {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String owner;
    private String info;

    public Test() {
    }

    @Id
    @Column(unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getInfo() {
        return this.info;
    }
    public void setInfo(String info) {
        this.info = info;
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getOwner() {
        return this.owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
