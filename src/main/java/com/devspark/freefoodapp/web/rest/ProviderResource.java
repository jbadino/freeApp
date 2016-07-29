package com.devspark.freefoodapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.devspark.freefoodapp.domain.Provider;
import com.devspark.freefoodapp.repository.ProviderRepository;
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
 * REST controller for managing Provider.
 */
@RestController
@RequestMapping("/api")
public class ProviderResource {

    private final Logger log = LoggerFactory.getLogger(ProviderResource.class);
        
    @Inject
    private ProviderRepository providerRepository;
    
    /**
     * POST  /providers : Create a new provider.
     *
     * @param provider the provider to create
     * @return the ResponseEntity with status 201 (Created) and with body the new provider, or with status 400 (Bad Request) if the provider has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/providers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Provider> createProvider(@Valid @RequestBody Provider provider) throws URISyntaxException {
        log.debug("REST request to save Provider : {}", provider);
        if (provider.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("provider", "idexists", "A new provider cannot already have an ID")).body(null);
        }
        Provider result = providerRepository.save(provider);
        return ResponseEntity.created(new URI("/api/providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("provider", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /providers : Updates an existing provider.
     *
     * @param provider the provider to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated provider,
     * or with status 400 (Bad Request) if the provider is not valid,
     * or with status 500 (Internal Server Error) if the provider couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/providers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Provider> updateProvider(@Valid @RequestBody Provider provider) throws URISyntaxException {
        log.debug("REST request to update Provider : {}", provider);
        if (provider.getId() == null) {
            return createProvider(provider);
        }
        Provider result = providerRepository.save(provider);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("provider", provider.getId().toString()))
            .body(result);
    }

    /**
     * GET  /providers : get all the providers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of providers in body
     */
    @RequestMapping(value = "/providers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Provider> getAllProviders() {
        log.debug("REST request to get all Providers");
        List<Provider> providers = providerRepository.findAll();
        return providers;
    }

    /**
     * GET  /providers/:id : get the "id" provider.
     *
     * @param id the id of the provider to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the provider, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/providers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Provider> getProvider(@PathVariable Long id) {
        log.debug("REST request to get Provider : {}", id);
        Provider provider = providerRepository.findOne(id);
        return Optional.ofNullable(provider)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /providers/:id : delete the "id" provider.
     *
     * @param id the id of the provider to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/providers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        log.debug("REST request to delete Provider : {}", id);
        providerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("provider", id.toString())).build();
    }

}
