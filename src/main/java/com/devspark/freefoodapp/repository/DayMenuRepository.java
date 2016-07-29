package com.devspark.freefoodapp.repository;

import com.devspark.freefoodapp.domain.DayMenu;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DayMenu entity.
 */
@SuppressWarnings("unused")
public interface DayMenuRepository extends JpaRepository<DayMenu,Long> {

}
