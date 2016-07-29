package com.devspark.freefoodapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Provider.
 */
@Entity
@Table(name = "provider")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(name = "responsible_person", length = 128, nullable = false)
    private String responsiblePerson;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(name = "phone", length = 128, nullable = false)
    private String phone;

    @Column(name = "service_hours")
    private String serviceHours;

    @Column(name = "address")
    private String address;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "whatsapp")
    private String whatsapp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(String serviceHours) {
        this.serviceHours = serviceHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Provider provider = (Provider) o;
        if(provider.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, provider.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Provider{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", responsiblePerson='" + responsiblePerson + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", serviceHours='" + serviceHours + "'" +
            ", address='" + address + "'" +
            ", facebook='" + facebook + "'" +
            ", whatsapp='" + whatsapp + "'" +
            '}';
    }
}
