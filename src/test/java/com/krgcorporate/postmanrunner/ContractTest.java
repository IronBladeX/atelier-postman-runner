package com.krgcorporate.postmanrunner;

import com.krgcorporate.postmanrunner.business.ContractManager;
import com.krgcorporate.postmanrunner.domain.Contract;
import com.krgcorporate.postmanrunner.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import com.krgcorporate.postmanrunner.ws.ContractController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = MongoDbInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContractTest {

    @Autowired
    private ContractRepository contractRepository;

    private final WebTestClient testClient =  WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:8080")
            .build();

    @BeforeAll
    public void before() {
        Contract contract = Contract.builder()
                .ref("CONTRACT_REF")
                .agency("AGENCY_CODE")
                .vendor("VENDOR_CODE")
                .status("DRAFT")
                .build();

        contractRepository.save(contract);
    }

    @Test
    public void testFindContract() {
        testClient.get()
                .uri("/contracts/CONTRACT_REF")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("ref").isEqualTo("CONTRACT_REF");
    }

}
