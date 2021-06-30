package com.krgcorporate.postmanrunner;

import com.krgcorporate.postmanrunner.domain.Contract;
import com.krgcorporate.postmanrunner.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        // content contains contract.ref with CONTRACT_REF
        // json field pageable.pageNumber is number
        // json field pageable.pageSize is number

    }

    @Test
    public void testFindContractExist() {
        // Test get /contracts/{ref} with ref CONTRACT_REF
        // return status Ok
        // Header Content-Type is application/json
        // and body contract has ref CONTRACT_REF

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



        // Test get /contracts/{ref} with ref CONTRACT_REF
        // return status Ok
        // Header Content-Type is application/json
        // and body contract has ref CONTRACT_REF
        // ref is CONTRACT_REF
        // status is updated to ACTIVE
    }

    @Test
    public void testPostContractAlreadyExist() {
        // Test post /contracts
        // with body {"ref": "CONTRACT_REF", "agency": "AGENCY_CODE", "vendor": "VENDOR_CODE", "status": "DRAFT" , "persons": [{ "code": "code", "gender": "M", "firstname": "John", "lastname": "Doe", "email": "john.doe@gmail.com", "role": "subscriber" }]}
        // return status is HttpStatus.CONFLICT

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
