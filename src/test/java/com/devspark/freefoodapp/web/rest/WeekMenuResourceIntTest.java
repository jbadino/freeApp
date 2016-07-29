package com.devspark.freefoodapp.web.rest;

import com.devspark.freefoodapp.FreeFoodApp;
import com.devspark.freefoodapp.domain.WeekMenu;
import com.devspark.freefoodapp.repository.WeekMenuRepository;

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
 * Test class for the WeekMenuResource REST controller.
 *
 * @see WeekMenuResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FreeFoodApp.class)
@WebAppConfiguration
@IntegrationTest
public class WeekMenuResourceIntTest {


    @Inject
    private WeekMenuRepository weekMenuRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeekMenuMockMvc;

    private WeekMenu weekMenu;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeekMenuResource weekMenuResource = new WeekMenuResource();
        ReflectionTestUtils.setField(weekMenuResource, "weekMenuRepository", weekMenuRepository);
        this.restWeekMenuMockMvc = MockMvcBuilders.standaloneSetup(weekMenuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        weekMenu = new WeekMenu();
    }

    @Test
    @Transactional
    public void createWeekMenu() throws Exception {
        int databaseSizeBeforeCreate = weekMenuRepository.findAll().size();

        // Create the WeekMenu

        restWeekMenuMockMvc.perform(post("/api/week-menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weekMenu)))
                .andExpect(status().isCreated());

        // Validate the WeekMenu in the database
        List<WeekMenu> weekMenus = weekMenuRepository.findAll();
        assertThat(weekMenus).hasSize(databaseSizeBeforeCreate + 1);
        WeekMenu testWeekMenu = weekMenus.get(weekMenus.size() - 1);
    }

    @Test
    @Transactional
    public void getAllWeekMenus() throws Exception {
        // Initialize the database
        weekMenuRepository.saveAndFlush(weekMenu);

        // Get all the weekMenus
        restWeekMenuMockMvc.perform(get("/api/week-menus?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(weekMenu.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWeekMenu() throws Exception {
        // Initialize the database
        weekMenuRepository.saveAndFlush(weekMenu);

        // Get the weekMenu
        restWeekMenuMockMvc.perform(get("/api/week-menus/{id}", weekMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(weekMenu.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWeekMenu() throws Exception {
        // Get the weekMenu
        restWeekMenuMockMvc.perform(get("/api/week-menus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeekMenu() throws Exception {
        // Initialize the database
        weekMenuRepository.saveAndFlush(weekMenu);
        int databaseSizeBeforeUpdate = weekMenuRepository.findAll().size();

        // Update the weekMenu
        WeekMenu updatedWeekMenu = new WeekMenu();
        updatedWeekMenu.setId(weekMenu.getId());

        restWeekMenuMockMvc.perform(put("/api/week-menus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWeekMenu)))
                .andExpect(status().isOk());

        // Validate the WeekMenu in the database
        List<WeekMenu> weekMenus = weekMenuRepository.findAll();
        assertThat(weekMenus).hasSize(databaseSizeBeforeUpdate);
        WeekMenu testWeekMenu = weekMenus.get(weekMenus.size() - 1);
    }

    @Test
    @Transactional
    public void deleteWeekMenu() throws Exception {
        // Initialize the database
        weekMenuRepository.saveAndFlush(weekMenu);
        int databaseSizeBeforeDelete = weekMenuRepository.findAll().size();

        // Get the weekMenu
        restWeekMenuMockMvc.perform(delete("/api/week-menus/{id}", weekMenu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WeekMenu> weekMenus = weekMenuRepository.findAll();
        assertThat(weekMenus).hasSize(databaseSizeBeforeDelete - 1);
    }
}
