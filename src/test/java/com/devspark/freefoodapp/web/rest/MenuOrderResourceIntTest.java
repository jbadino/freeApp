package com.devspark.freefoodapp.web.rest;

import com.devspark.freefoodapp.FreeFoodApp;
import com.devspark.freefoodapp.domain.MenuOrder;
import com.devspark.freefoodapp.repository.MenuOrderRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MenuOrderResource REST controller.
 *
 * @see MenuOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FreeFoodApp.class)
@WebAppConfiguration
@IntegrationTest
public class MenuOrderResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private MenuOrderRepository menuOrderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMenuOrderMockMvc;

    private MenuOrder menuOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuOrderResource menuOrderResource = new MenuOrderResource();
        ReflectionTestUtils.setField(menuOrderResource, "menuOrderRepository", menuOrderRepository);
        this.restMenuOrderMockMvc = MockMvcBuilders.standaloneSetup(menuOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        menuOrder = new MenuOrder();
        menuOrder.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMenuOrder() throws Exception {
        int databaseSizeBeforeCreate = menuOrderRepository.findAll().size();

        // Create the MenuOrder

        restMenuOrderMockMvc.perform(post("/api/menu-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(menuOrder)))
                .andExpect(status().isCreated());

        // Validate the MenuOrder in the database
        List<MenuOrder> menuOrders = menuOrderRepository.findAll();
        assertThat(menuOrders).hasSize(databaseSizeBeforeCreate + 1);
        MenuOrder testMenuOrder = menuOrders.get(menuOrders.size() - 1);
        assertThat(testMenuOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMenuOrders() throws Exception {
        // Initialize the database
        menuOrderRepository.saveAndFlush(menuOrder);

        // Get all the menuOrders
        restMenuOrderMockMvc.perform(get("/api/menu-orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(menuOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMenuOrder() throws Exception {
        // Initialize the database
        menuOrderRepository.saveAndFlush(menuOrder);

        // Get the menuOrder
        restMenuOrderMockMvc.perform(get("/api/menu-orders/{id}", menuOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(menuOrder.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMenuOrder() throws Exception {
        // Get the menuOrder
        restMenuOrderMockMvc.perform(get("/api/menu-orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuOrder() throws Exception {
        // Initialize the database
        menuOrderRepository.saveAndFlush(menuOrder);
        int databaseSizeBeforeUpdate = menuOrderRepository.findAll().size();

        // Update the menuOrder
        MenuOrder updatedMenuOrder = new MenuOrder();
        updatedMenuOrder.setId(menuOrder.getId());
        updatedMenuOrder.setDescription(UPDATED_DESCRIPTION);

        restMenuOrderMockMvc.perform(put("/api/menu-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMenuOrder)))
                .andExpect(status().isOk());

        // Validate the MenuOrder in the database
        List<MenuOrder> menuOrders = menuOrderRepository.findAll();
        assertThat(menuOrders).hasSize(databaseSizeBeforeUpdate);
        MenuOrder testMenuOrder = menuOrders.get(menuOrders.size() - 1);
        assertThat(testMenuOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteMenuOrder() throws Exception {
        // Initialize the database
        menuOrderRepository.saveAndFlush(menuOrder);
        int databaseSizeBeforeDelete = menuOrderRepository.findAll().size();

        // Get the menuOrder
        restMenuOrderMockMvc.perform(delete("/api/menu-orders/{id}", menuOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MenuOrder> menuOrders = menuOrderRepository.findAll();
        assertThat(menuOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
