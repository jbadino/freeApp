package com.devspark.freefoodapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.devspark.freefoodapp.domain.MenuItem;
import com.devspark.freefoodapp.repository.MenuItemRepository;
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
 * REST controller for managing MenuItem.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);
        
    @Inject
    private MenuItemRepository menuItemRepository;
    
    /**
     * POST  /menu-items : Create a new menuItem.
     *
     * @param menuItem the menuItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new menuItem, or with status 400 (Bad Request) if the menuItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/menu-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("menuItem", "idexists", "A new menuItem cannot already have an ID")).body(null);
        }
        MenuItem result = menuItemRepository.save(menuItem);
        return ResponseEntity.created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("menuItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menu-items : Updates an existing menuItem.
     *
     * @param menuItem the menuItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated menuItem,
     * or with status 400 (Bad Request) if the menuItem is not valid,
     * or with status 500 (Internal Server Error) if the menuItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/menu-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuItem> updateMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}", menuItem);
        if (menuItem.getId() == null) {
            return createMenuItem(menuItem);
        }
        MenuItem result = menuItemRepository.save(menuItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("menuItem", menuItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menu-items : get all the menuItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of menuItems in body
     */
    @RequestMapping(value = "/menu-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MenuItem> getAllMenuItems() {
        log.debug("REST request to get all MenuItems");
        List<MenuItem> menuItems = menuItemRepository.findAll();
        return menuItems;
    }

    /**
     * GET  /menu-items/:id : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the menuItem, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/menu-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        MenuItem menuItem = menuItemRepository.findOne(id);
        return Optional.ofNullable(menuItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /menu-items/:id : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/menu-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("menuItem", id.toString())).build();
    }

}
