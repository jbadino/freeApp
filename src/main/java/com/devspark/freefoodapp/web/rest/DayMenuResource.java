package com.devspark.freefoodapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.devspark.freefoodapp.domain.DayMenu;
import com.devspark.freefoodapp.repository.DayMenuRepository;
import com.devspark.freefoodapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DayMenu.
 */
@RestController
@RequestMapping("/api")
public class DayMenuResource {

    private final Logger log = LoggerFactory.getLogger(DayMenuResource.class);
        
    @Inject
    private DayMenuRepository dayMenuRepository;
    
    /**
     * POST  /day-menus : Create a new dayMenu.
     *
     * @param dayMenu the dayMenu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dayMenu, or with status 400 (Bad Request) if the dayMenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/day-menus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayMenu> createDayMenu(@Valid @RequestBody DayMenu dayMenu) throws URISyntaxException {
        log.debug("REST request to save DayMenu : {}", dayMenu);
        if (dayMenu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dayMenu", "idexists", "A new dayMenu cannot already have an ID")).body(null);
        }
        DayMenu result = dayMenuRepository.save(dayMenu);
        return ResponseEntity.created(new URI("/api/day-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dayMenu", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /day-menus : Updates an existing dayMenu.
     *
     * @param dayMenu the dayMenu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dayMenu,
     * or with status 400 (Bad Request) if the dayMenu is not valid,
     * or with status 500 (Internal Server Error) if the dayMenu couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/day-menus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayMenu> updateDayMenu(@Valid @RequestBody DayMenu dayMenu) throws URISyntaxException {
        log.debug("REST request to update DayMenu : {}", dayMenu);
        if (dayMenu.getId() == null) {
            return createDayMenu(dayMenu);
        }
        DayMenu result = dayMenuRepository.save(dayMenu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dayMenu", dayMenu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /day-menus : get all the dayMenus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dayMenus in body
     */
    @RequestMapping(value = "/day-menus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DayMenu> getAllDayMenus() {
        log.debug("REST request to get all DayMenus");
        List<DayMenu> dayMenus = dayMenuRepository.findAll();
        return dayMenus;
    }

    /**
     * GET  /day-menus/:id : get the "id" dayMenu.
     *
     * @param id the id of the dayMenu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dayMenu, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/day-menus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayMenu> getDayMenu(@PathVariable Long id) {
        log.debug("REST request to get DayMenu : {}", id);
        DayMenu dayMenu = dayMenuRepository.findOne(id);
        return Optional.ofNullable(dayMenu)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /day-menus/:id : delete the "id" dayMenu.
     *
     * @param id the id of the dayMenu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/day-menus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDayMenu(@PathVariable Long id) {
        log.debug("REST request to delete DayMenu : {}", id);
        dayMenuRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dayMenu", id.toString())).build();
    }

}
