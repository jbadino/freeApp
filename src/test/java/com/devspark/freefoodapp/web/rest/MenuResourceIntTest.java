package com.devspark.freefoodapp.web.rest;

import com.devspark.freefoodapp.FreeFoodApp;
import com.devspark.freefoodapp.domain.Menu;
import com.devspark.freefoodapp.repository.MenuRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MenuResource REST controller.
 *
 * @see MenuResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FreeFoodApp.class)
@WebAppConfiguration
@IntegrationTest
public class MenuResourceIntTest {


    private static final LocalDate DEFAULT_WEEK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WEEK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PROMOTION = "AAAAA";
    private static final String UPDATED_PROMOTION = "BBBBB";

    @Inject
    private MenuRepository menuRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMenuMockMvc;

    private Menu menu;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuResource menuResource = new MenuResource();
        ReflectionTestUtils.setField(menuResource, "menuRepository", menuRepository);
        this.restMenuMockMvc = MockMvcBuilders.standaloneSetup(menuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        menu = new Menu();
        menu.setWeekDate(DEFAULT_WEEK_DATE);
        menu.setDescription(DEFAULT_DESCRIPTION);
        menu.setPromotion(DEFAULT_PROMOTION);
    }

    @Test
    @Transactional
    public void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // Create the Menu

        restMenuMockMvc.perform(post("/api/menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(menu)))
                .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menus = menuRepository.findAll();
        assertThat(menus).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menus.get(menus.size() - 1);
        assertThat(testMenu.getWeekDate()).isEqualTo(DEFAULT_WEEK_DATE);
        assertThat(testMenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenu.getPromotion()).isEqualTo(DEFAULT_PROMOTION);
    }

    @Test
    @Transactional
    public void checkWeekDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setWeekDate(null);

        // Create the Menu, which fails.

        restMenuMockMvc.perform(post("/api/menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(menu)))
                .andExpect(status().isBadRequest());

        List<Menu> menus = menuRepository.findAll();
        assertThat(menus).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenus() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menus
        restMenuMockMvc.perform(get("/api/menus?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
                .andExpect(jsonPath("$.[*].weekDate").value(hasItem(DEFAULT_WEEK_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].promotion").value(hasItem(DEFAULT_PROMOTION.toString())));
    }

    @Test
    @Transactional
    public void getMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc.perform(get("/api/menus/{id}", menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(menu.getId().intValue()))
            .andExpect(jsonPath("$.weekDate").value(DEFAULT_WEEK_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.promotion").value(DEFAULT_PROMOTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get("/api/menus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu
        Menu updatedMenu = new Menu();
        updatedMenu.setId(menu.getId());
        updatedMenu.setWeekDate(UPDATED_WEEK_DATE);
        updatedMenu.setDescription(UPDATED_DESCRIPTION);
        updatedMenu.setPromotion(UPDATED_PROMOTION);

        restMenuMockMvc.perform(put("/api/menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMenu)))
                .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menus = menuRepository.findAll();
        assertThat(menus).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menus.get(menus.size() - 1);
        assertThat(testMenu.getWeekDate()).isEqualTo(UPDATED_WEEK_DATE);
        assertThat(testMenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenu.getPromotion()).isEqualTo(UPDATED_PROMOTION);
    }

    @Test
    @Transactional
    public void deleteMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);
        int databaseSizeBeforeDelete = menuRepository.findAll().size();

        // Get the menu
        restMenuMockMvc.perform(delete("/api/menus/{id}", menu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Menu> menus = menuRepository.findAll();
        assertThat(menus).hasSize(databaseSizeBeforeDelete - 1);
    }
}
