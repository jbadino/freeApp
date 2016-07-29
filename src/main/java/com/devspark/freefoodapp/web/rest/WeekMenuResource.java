package com.devspark.freefoodapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.devspark.freefoodapp.domain.WeekMenu;
import com.devspark.freefoodapp.repository.WeekMenuRepository;
import com.devspark.freefoodapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WeekMenu.
 */
@RestController
@RequestMapping("/api")
public class WeekMenuResource {

    private final Logger log = LoggerFactory.getLogger(WeekMenuResource.class);
        
    @Inject
    private WeekMenuRepository weekMenuRepository;
    
    /**
     * POST  /week-menus : Create a new weekMenu.
     *
     * @param weekMenu the weekMenu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weekMenu, or with status 400 (Bad Request) if the weekMenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/week-menus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekMenu> createWeekMenu(@RequestBody WeekMenu weekMenu) throws URISyntaxException {
        log.debug("REST request to save WeekMenu : {}", weekMenu);
        if (weekMenu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weekMenu", "idexists", "A new weekMenu cannot already have an ID")).body(null);
        }
        WeekMenu result = weekMenuRepository.save(weekMenu);
        return ResponseEntity.created(new URI("/api/week-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weekMenu", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /week-menus : Updates an existing weekMenu.
     *
     * @param weekMenu the weekMenu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weekMenu,
     * or with status 400 (Bad Request) if the weekMenu is not valid,
     * or with status 500 (Internal Server Error) if the weekMenu couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/week-menus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekMenu> updateWeekMenu(@RequestBody WeekMenu weekMenu) throws URISyntaxException {
        log.debug("REST request to update WeekMenu : {}", weekMenu);
        if (weekMenu.getId() == null) {
            return createWeekMenu(weekMenu);
        }
        WeekMenu result = weekMenuRepository.save(weekMenu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weekMenu", weekMenu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /week-menus : get all the weekMenus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of weekMenus in body
     */
    @RequestMapping(value = "/week-menus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WeekMenu> getAllWeekMenus() {
        log.debug("REST request to get all WeekMenus");
        List<WeekMenu> weekMenus = weekMenuRepository.findAll();
        return weekMenus;
    }

    /**
     * GET  /week-menus/:id : get the "id" weekMenu.
     *
     * @param id the id of the weekMenu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weekMenu, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/week-menus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekMenu> getWeekMenu(@PathVariable Long id) {
        log.debug("REST request to get WeekMenu : {}", id);
        WeekMenu weekMenu = weekMenuRepository.findOne(id);
        return Optional.ofNullable(weekMenu)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /week-menus/:id : delete the "id" weekMenu.
     *
     * @param id the id of the weekMenu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/week-menus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeekMenu(@PathVariable Long id) {
        log.debug("REST request to delete WeekMenu : {}", id);
        weekMenuRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weekMenu", id.toString())).build();
    }

}
