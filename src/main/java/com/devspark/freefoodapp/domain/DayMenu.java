package com.devspark.freefoodapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DayMenu.
 */
@Entity
@Table(name = "day_menu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DayMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 10)
    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @ManyToOne
    private WeekMenu weekMenu;

    @OneToMany(mappedBy = "dayMenu")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuItem> menuItems = new HashSet<>();

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

    public WeekMenu getWeekMenu() {
        return weekMenu;
    }

    public void setWeekMenu(WeekMenu weekMenu) {
        this.weekMenu = weekMenu;
    }

    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DayMenu dayMenu = (DayMenu) o;
        if(dayMenu.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dayMenu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DayMenu{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
