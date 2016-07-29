package com.devspark.freefoodapp.web.rest;

import com.devspark.freefoodapp.FreeFoodApp;
import com.devspark.freefoodapp.domain.DayMenu;
import com.devspark.freefoodapp.repository.DayMenuRepository;

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
 * Test class for the DayMenuResource REST controller.
 *
 * @see DayMenuResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FreeFoodApp.class)
@WebAppConfiguration
@IntegrationTest
public class DayMenuResourceIntTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";

    @Inject
    private DayMenuRepository dayMenuRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDayMenuMockMvc;

    private DayMenu dayMenu;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DayMenuResource dayMenuResource = new DayMenuResource();
        ReflectionTestUtils.setField(dayMenuResource, "dayMenuRepository", dayMenuRepository);
        this.restDayMenuMockMvc = MockMvcBuilders.standaloneSetup(dayMenuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dayMenu = new DayMenu();
        dayMenu.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDayMenu() throws Exception {
        int databaseSizeBeforeCreate = dayMenuRepository.findAll().size();

        // Create the DayMenu

        restDayMenuMockMvc.perform(post("/api/day-menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dayMenu)))
                .andExpect(status().isCreated());

        // Validate the DayMenu in the database
        List<DayMenu> dayMenus = dayMenuRepository.findAll();
        assertThat(dayMenus).hasSize(databaseSizeBeforeCreate + 1);
        DayMenu testDayMenu = dayMenus.get(dayMenus.size() - 1);
        assertThat(testDayMenu.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayMenuRepository.findAll().size();
        // set the field null
        dayMenu.setName(null);

        // Create the DayMenu, which fails.

        restDayMenuMockMvc.perform(post("/api/day-menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dayMenu)))
                .andExpect(status().isBadRequest());

        List<DayMenu> dayMenus = dayMenuRepository.findAll();
        assertThat(dayMenus).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDayMenus() throws Exception {
        // Initialize the database
        dayMenuRepository.saveAndFlush(dayMenu);

        // Get all the dayMenus
        restDayMenuMockMvc.perform(get("/api/day-menus?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dayMenu.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDayMenu() throws Exception {
        // Initialize the database
        dayMenuRepository.saveAndFlush(dayMenu);

        // Get the dayMenu
        restDayMenuMockMvc.perform(get("/api/day-menus/{id}", dayMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dayMenu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDayMenu() throws Exception {
        // Get the dayMenu
        restDayMenuMockMvc.perform(get("/api/day-menus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDayMenu() throws Exception {
        // Initialize the database
        dayMenuRepository.saveAndFlush(dayMenu);
        int databaseSizeBeforeUpdate = dayMenuRepository.findAll().size();

        // Update the dayMenu
        DayMenu updatedDayMenu = new DayMenu();
        updatedDayMenu.setId(dayMenu.getId());
        updatedDayMenu.setName(UPDATED_NAME);

        restDayMenuMockMvc.perform(put("/api/day-menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDayMenu)))
                .andExpect(status().isOk());

        // Validate the DayMenu in the database
        List<DayMenu> dayMenus = dayMenuRepository.findAll();
        assertThat(dayMenus).hasSize(databaseSizeBeforeUpdate);
        DayMenu testDayMenu = dayMenus.get(dayMenus.size() - 1);
        assertThat(testDayMenu.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteDayMenu() throws Exception {
        // Initialize the database
        dayMenuRepository.saveAndFlush(dayMenu);
        int databaseSizeBeforeDelete = dayMenuRepository.findAll().size();

        // Get the dayMenu
        restDayMenuMockMvc.perform(delete("/api/day-menus/{id}", dayMenu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DayMenu> dayMenus = dayMenuRepository.findAll();
        assertThat(dayMenus).hasSize(databaseSizeBeforeDelete - 1);
    }
}
