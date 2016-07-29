package com.devspark.freefoodapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.devspark.freefoodapp.domain.MenuOrder;
import com.devspark.freefoodapp.repository.MenuOrderRepository;
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
 * REST controller for managing MenuOrder.
 */
@RestController
@RequestMapping("/api")
public class MenuOrderResource {

    private final Logger log = LoggerFactory.getLogger(MenuOrderResource.class);
        
    @Inject
    private MenuOrderRepository menuOrderRepository;
    
    /**
     * POST  /menu-orders : Create a new menuOrder.
     *
     * @param menuOrder the menuOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new menuOrder, or with status 400 (Bad Request) if the menuOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/menu-orders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuOrder> createMenuOrder(@RequestBody MenuOrder menuOrder) throws URISyntaxException {
        log.debug("REST request to save MenuOrder : {}", menuOrder);
        if (menuOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("menuOrder", "idexists", "A new menuOrder cannot already have an ID")).body(null);
        }
        MenuOrder result = menuOrderRepository.save(menuOrder);
        return ResponseEntity.created(new URI("/api/menu-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("menuOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menu-orders : Updates an existing menuOrder.
     *
     * @param menuOrder the menuOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated menuOrder,
     * or with status 400 (Bad Request) if the menuOrder is not valid,
     * or with status 500 (Internal Server Error) if the menuOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/menu-orders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuOrder> updateMenuOrder(@RequestBody MenuOrder menuOrder) throws URISyntaxException {
        log.debug("REST request to update MenuOrder : {}", menuOrder);
        if (menuOrder.getId() == null) {
            return createMenuOrder(menuOrder);
        }
        MenuOrder result = menuOrderRepository.save(menuOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("menuOrder", menuOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menu-orders : get all the menuOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of menuOrders in body
     */
    @RequestMapping(value = "/menu-orders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MenuOrder> getAllMenuOrders() {
        log.debug("REST request to get all MenuOrders");
        List<MenuOrder> menuOrders = menuOrderRepository.findAll();
        return menuOrders;
    }

    /**
     * GET  /menu-orders/:id : get the "id" menuOrder.
     *
     * @param id the id of the menuOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the menuOrder, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/menu-orders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuOrder> getMenuOrder(@PathVariable Long id) {
        log.debug("REST request to get MenuOrder : {}", id);
        MenuOrder menuOrder = menuOrderRepository.findOne(id);
        return Optional.ofNullable(menuOrder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /menu-orders/:id : delete the "id" menuOrder.
     *
     * @param id the id of the menuOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/menu-orders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMenuOrder(@PathVariable Long id) {
        log.debug("REST request to delete MenuOrder : {}", id);
        menuOrderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("menuOrder", id.toString())).build();
    }

}
