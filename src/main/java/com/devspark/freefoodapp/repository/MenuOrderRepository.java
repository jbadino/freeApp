package com.devspark.freefoodapp.repository;

import com.devspark.freefoodapp.domain.MenuOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MenuOrder entity.
 */
@SuppressWarnings("unused")
public interface MenuOrderRepository extends JpaRepository<MenuOrder,Long> {

}
