package com.devspark.freefoodapp.repository;

import com.devspark.freefoodapp.domain.WeekMenu;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WeekMenu entity.
 */
@SuppressWarnings("unused")
public interface WeekMenuRepository extends JpaRepository<WeekMenu,Long> {

}
