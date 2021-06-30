package com.krgcorporate.postmanrunner;

import com.krgcorporate.postmanrunner.domain.Contract;
import com.krgcorporate.postmanrunner.repository.ContractRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = MongoDbInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContractControllerTest {

    @Autowired
    private ContractRepository contractRepository;

    private final WebTestClient testClient = WebTestClient
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
    public void testListContracts() {
        // Test get /contracts
        // return status Ok
        // Header Content-Type is application/json
        // and body respect:
        // json field content is array
        // json field pageable.pageNumber is number
        // json field pageable.pageSize is number

        testClient.get()
                .uri("/contracts/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("content").isArray()
                .jsonPath("pageable.pageNumber").isNumber()
                .jsonPath("pageable.pageSize").isNumber();
    }

    @Test
    public void testFindContractExist() {
        // Test get /contracts/{ref} with ref CONTRACT_REF
        // return status Ok
        // Header Content-Type is application/json
        // and body contract has ref CONTRACT_REF

        testClient.get()
                .uri("/contracts/{ref}", "CONTRACT_REF")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("ref").isEqualTo("CONTRACT_REF");
    }

    @Test
    public void testPostContract() {
        // Test post /contracts
        // with body {"ref": "CONTRACT_REF2", "agency": "AGENCY_CODE", "vendor": "VENDOR_CODE", "status": "DRAFT" , "persons": [{ "code": "code", "gender": "M", "firstname": "John", "lastname": "Doe", "email": "john.doe@gmail.com", "role": "subscriber" }]}
        // return status Ok
        // Header Content-Type is application/json
        // and body respect:
        // json collection persons hasSize 1 (Use Matchers.hasSize)
        // all elements in collection persons has code

        testClient.post()
                .uri("/contracts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"ref\": \"CONTRACT_REF2\", \"agency\": \"AGENCY_CODE\", \"vendor\": \"VENDOR_CODE\", \"status\": \"DRAFT\" , \"persons\": [{ \"code\": \"code\", \"gender\": \"M\", \"firstname\": \"John\", \"lastname\": \"Doe\", \"email\": \"john.doe@gmail.com\", \"role\": \"subscriber\" }]}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("ref").isEqualTo("CONTRACT_REF2")
                .jsonPath("persons").value(v -> Matchers.hasSize(1))
                .jsonPath("persons[*].code").exists();
    }

    @Test
    public void testPutContract() {
        // Test put /contracts/{ref} with ref CONTRACT_REF
        // with body {"ref": "CONTRACT_REF", "agency": "AGENCY_CODE", "vendor": "VENDOR_CODE", "status": "ACTIVE"}
        // return status Ok
        // Header Content-Type is application/json
        // and body respect:
        // ref is CONTRACT_REF
        // status is ACTIVE

        testClient.put()
                .uri("/contracts/{ref}", "CONTRACT_REF")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"ref\": \"CONTRACT_REF\", \"agency\": \"AGENCY_CODE\", \"vendor\": \"VENDOR_CODE\", \"status\": \"ACTIVE\"}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("ref").isEqualTo("CONTRACT_REF")
                .jsonPath("status").isEqualTo("ACTIVE");


        // Test get /contracts/{ref} with ref CONTRACT_REF
        // return status Ok
        // Header Content-Type is application/json
        // and body contract has ref CONTRACT_REF
        // ref is CONTRACT_REF
        // status is updated to ACTIVE
        testClient.get()
                .uri("/contracts/{ref}", "CONTRACT_REF")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("ref").isEqualTo("CONTRACT_REF")
                .jsonPath("status").isEqualTo("ACTIVE");
    }

    @Test
    public void testPostContractAlreadyExist() {
        // Test post /contracts
        // with body {"ref": "CONTRACT_REF", "agency": "AGENCY_CODE", "vendor": "VENDOR_CODE", "status": "DRAFT" , "persons": [{ "code": "code", "gender": "M", "firstname": "John", "lastname": "Doe", "email": "john.doe@gmail.com", "role": "subscriber" }]}
        // return status is HttpStatus.CONFLICT

        testClient.post()
                .uri("/contracts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"ref\": \"CONTRACT_REF\", \"agency\": \"AGENCY_CODE\", \"vendor\": \"VENDOR_CODE\", \"status\": \"DRAFT\" , \"persons\": [{ \"code\": \"code\", \"gender\": \"M\", \"firstname\": \"John\", \"lastname\": \"Doe\", \"email\": \"john.doe@gmail.com\", \"role\": \"subscriber\" }]}")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void testPostContractWithError() {
        // Test post /contracts
        // with body {"ref": "CONTRACT_REF", "agency": "AGENCY_CODE", "vendor": "VENDOR_CODE", "status": "DRAFT" , "persons": [{ "code": "code", "gender": "SOCIETY", "firstname": "John", "lastname": "Doe", "email": "john.doe@gmail.com", "role": "subscriber" }]}
        // dont works and return status is BAD_REQUEST
        // body fields is:
        // data.fieldErrors['persons[0].firstname'] is array
        // data.fieldErrors['persons[0].firstname'][0].code is equal to 'must-be-blank'
        // data.fieldErrors['persons[0].firstname'][0].message is equal to 'Firstname must be blank on society.'

        testClient.post()
                .uri("/contracts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"ref\": \"CONTRACT_REF\", \"agency\": \"AGENCY_CODE\", \"vendor\": \"VENDOR_CODE\", \"status\": \"DRAFT\" , \"persons\": [{ \"code\": \"code\", \"gender\": \"SOCIETY\", \"firstname\": \"John\", \"lastname\": \"Doe\", \"email\": \"john.doe@gmail.com\", \"role\": \"subscriber\" }]}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("code").isEqualTo("validation_failed")
                .jsonPath("data.fieldErrors").exists()
                .jsonPath("data.fieldErrors['persons[0].firstname']").isArray()
                .jsonPath("data.fieldErrors['persons[0].firstname'][0].code").isEqualTo("must-be-blank")
                .jsonPath("data.fieldErrors['persons[0].firstname'][0].message").isEqualTo("Firstname must be blank on society.");
    }

    @Test
    public void testFindNotFound() {
        // Test get /contracts/{ref} with ref CONTRACT_REF4
        // return status is NotFound

        testClient.get()
                .uri("/contracts/{ref}", "CONTRACT_REF4")
                .exchange()
                .expectStatus().isNotFound();
    }
}
