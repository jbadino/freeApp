package com.devspark.freefoodapp.web.rest;

import com.devspark.freefoodapp.FreeFoodApp;
import com.devspark.freefoodapp.domain.MenuItem;
import com.devspark.freefoodapp.repository.MenuItemRepository;

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
 * Test class for the MenuItemResource REST controller.
 *
 * @see MenuItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FreeFoodApp.class)
@WebAppConfiguration
@IntegrationTest
public class MenuItemResourceIntTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private MenuItemRepository menuItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuItemResource menuItemResource = new MenuItemResource();
        ReflectionTestUtils.setField(menuItemResource, "menuItemRepository", menuItemRepository);
        this.restMenuItemMockMvc = MockMvcBuilders.standaloneSetup(menuItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        menuItem = new MenuItem();
        menuItem.setName(DEFAULT_NAME);
        menuItem.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem

        restMenuItemMockMvc.perform(post("/api/menu-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(menuItem)))
                .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItems.get(menuItems.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setName(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(menuItem)))
                .andExpect(status().isBadRequest());

        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItems
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(menuItem.getId());
        updatedMenuItem.setName(UPDATED_NAME);
        updatedMenuItem.setDescription(UPDATED_DESCRIPTION);

        restMenuItemMockMvc.perform(put("/api/menu-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMenuItem)))
                .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItems.get(menuItems.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);
        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Get the menuItem
        restMenuItemMockMvc.perform(delete("/api/menu-items/{id}", menuItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
