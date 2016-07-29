package com.devspark.freefoodapp.web.rest;

import com.devspark.freefoodapp.FreeFoodApp;
import com.devspark.freefoodapp.domain.Provider;
import com.devspark.freefoodapp.repository.ProviderRepository;

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
 * Test class for the ProviderResource REST controller.
 *
 * @see ProviderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FreeFoodApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProviderResourceIntTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";
    private static final String DEFAULT_RESPONSIBLE_PERSON = "AA";
    private static final String UPDATED_RESPONSIBLE_PERSON = "BB";
    private static final String DEFAULT_EMAIL = "AA";
    private static final String UPDATED_EMAIL = "BB";
    private static final String DEFAULT_PHONE = "AA";
    private static final String UPDATED_PHONE = "BB";
    private static final String DEFAULT_SERVICE_HOURS = "AAAAA";
    private static final String UPDATED_SERVICE_HOURS = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_FACEBOOK = "AAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBB";
    private static final String DEFAULT_WHATSAPP = "AAAAA";
    private static final String UPDATED_WHATSAPP = "BBBBB";

    @Inject
    private ProviderRepository providerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProviderMockMvc;

    private Provider provider;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProviderResource providerResource = new ProviderResource();
        ReflectionTestUtils.setField(providerResource, "providerRepository", providerRepository);
        this.restProviderMockMvc = MockMvcBuilders.standaloneSetup(providerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        provider = new Provider();
        provider.setName(DEFAULT_NAME);
        provider.setResponsiblePerson(DEFAULT_RESPONSIBLE_PERSON);
        provider.setEmail(DEFAULT_EMAIL);
        provider.setPhone(DEFAULT_PHONE);
        provider.setServiceHours(DEFAULT_SERVICE_HOURS);
        provider.setAddress(DEFAULT_ADDRESS);
        provider.setFacebook(DEFAULT_FACEBOOK);
        provider.setWhatsapp(DEFAULT_WHATSAPP);
    }

    @Test
    @Transactional
    public void createProvider() throws Exception {
        int databaseSizeBeforeCreate = providerRepository.findAll().size();

        // Create the Provider

        restProviderMockMvc.perform(post("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(provider)))
                .andExpect(status().isCreated());

        // Validate the Provider in the database
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeCreate + 1);
        Provider testProvider = providers.get(providers.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvider.getResponsiblePerson()).isEqualTo(DEFAULT_RESPONSIBLE_PERSON);
        assertThat(testProvider.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProvider.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testProvider.getServiceHours()).isEqualTo(DEFAULT_SERVICE_HOURS);
        assertThat(testProvider.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProvider.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testProvider.getWhatsapp()).isEqualTo(DEFAULT_WHATSAPP);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setName(null);

        // Create the Provider, which fails.

        restProviderMockMvc.perform(post("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(provider)))
                .andExpect(status().isBadRequest());

        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsiblePersonIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setResponsiblePerson(null);

        // Create the Provider, which fails.

        restProviderMockMvc.perform(post("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(provider)))
                .andExpect(status().isBadRequest());

        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setEmail(null);

        // Create the Provider, which fails.

        restProviderMockMvc.perform(post("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(provider)))
                .andExpect(status().isBadRequest());

        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setPhone(null);

        // Create the Provider, which fails.

        restProviderMockMvc.perform(post("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(provider)))
                .andExpect(status().isBadRequest());

        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProviders() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get all the providers
        restProviderMockMvc.perform(get("/api/providers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(provider.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].responsiblePerson").value(hasItem(DEFAULT_RESPONSIBLE_PERSON.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].serviceHours").value(hasItem(DEFAULT_SERVICE_HOURS.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
                .andExpect(jsonPath("$.[*].whatsapp").value(hasItem(DEFAULT_WHATSAPP.toString())));
    }

    @Test
    @Transactional
    public void getProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get the provider
        restProviderMockMvc.perform(get("/api/providers/{id}", provider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(provider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.responsiblePerson").value(DEFAULT_RESPONSIBLE_PERSON.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.serviceHours").value(DEFAULT_SERVICE_HOURS.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
            .andExpect(jsonPath("$.whatsapp").value(DEFAULT_WHATSAPP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProvider() throws Exception {
        // Get the provider
        restProviderMockMvc.perform(get("/api/providers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Update the provider
        Provider updatedProvider = new Provider();
        updatedProvider.setId(provider.getId());
        updatedProvider.setName(UPDATED_NAME);
        updatedProvider.setResponsiblePerson(UPDATED_RESPONSIBLE_PERSON);
        updatedProvider.setEmail(UPDATED_EMAIL);
        updatedProvider.setPhone(UPDATED_PHONE);
        updatedProvider.setServiceHours(UPDATED_SERVICE_HOURS);
        updatedProvider.setAddress(UPDATED_ADDRESS);
        updatedProvider.setFacebook(UPDATED_FACEBOOK);
        updatedProvider.setWhatsapp(UPDATED_WHATSAPP);

        restProviderMockMvc.perform(put("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProvider)))
                .andExpect(status().isOk());

        // Validate the Provider in the database
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeUpdate);
        Provider testProvider = providers.get(providers.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvider.getResponsiblePerson()).isEqualTo(UPDATED_RESPONSIBLE_PERSON);
        assertThat(testProvider.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProvider.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testProvider.getServiceHours()).isEqualTo(UPDATED_SERVICE_HOURS);
        assertThat(testProvider.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProvider.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testProvider.getWhatsapp()).isEqualTo(UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    public void deleteProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);
        int databaseSizeBeforeDelete = providerRepository.findAll().size();

        // Get the provider
        restProviderMockMvc.perform(delete("/api/providers/{id}", provider.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
