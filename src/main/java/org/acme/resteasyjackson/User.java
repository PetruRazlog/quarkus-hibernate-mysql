package org.acme.resteasyjackson;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    @Column
    public String fname;

    @Column
    public String lname;


}
