package com.nullers.restbookstore.rest.client.repositories;

import com.nullers.restbookstore.rest.client.models.Client;
import com.nullers.restbookstore.rest.client.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    Client clientTest = Client.builder()
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .build();

    @BeforeEach
    void setUp(){
        clientRepository.deleteAll();
    }

    @Test
    void findAllTest(){
        entityManager.merge(clientTest);
        entityManager.flush();

        var clients = clientRepository.findAll();

        assertAll(
                () -> assertNotNull(clients),
                () -> assertFalse(clients.isEmpty()),
                () -> assertEquals(1, clients.size()),
                () -> assertEquals(clientTest.getName(), clients.get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), clients.get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), clients.get(0).getEmail()),
                () -> assertEquals(clientTest.getPhone(), clients.get(0).getPhone()),
                () -> assertEquals(clientTest.getAddress(), clients.get(0).getAddress()),
                () -> assertEquals(clientTest.getImage(), clients.get(0).getImage())
        );
    }


    @Test
    void findByIdTest(){
        var res = entityManager.merge(clientTest);
        entityManager.flush();

        var client = clientRepository.findById(res.getId());

        assertAll(
                () -> assertNotNull(client),
                () -> assertEquals(clientTest.getName(), client.get().getName()),
                () -> assertEquals(clientTest.getSurname(), client.get().getSurname()),
                () -> assertEquals(clientTest.getEmail(), client.get().getEmail()),
                () -> assertEquals(clientTest.getPhone(), client.get().getPhone()),
                () -> assertEquals(clientTest.getAddress(), client.get().getAddress()),
                () -> assertEquals(clientTest.getImage(), client.get().getImage())
        );
    }

    @Test
    void findByIdNotFoundTest(){
        entityManager.merge(clientTest);
        entityManager.flush();

        var client = clientRepository.findById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"));

        assertAll(
                () -> assertNotNull(client),
                () -> assertTrue(client.isEmpty())
        );
    }

    @Test
    void findByEmailTest(){
        var res = entityManager.merge(clientTest);
        entityManager.flush();

        var client = clientRepository.getClientByEmail(res.getEmail());

        assertAll(
                () -> assertNotNull(client),
                () -> assertEquals(clientTest.getName(), client.get().getName()),
                () -> assertEquals(clientTest.getSurname(), client.get().getSurname()),
                () -> assertEquals(clientTest.getEmail(), client.get().getEmail()),
                () -> assertEquals(clientTest.getPhone(), client.get().getPhone()),
                () -> assertEquals(clientTest.getAddress(), client.get().getAddress()),
                () -> assertEquals(clientTest.getImage(), client.get().getImage())
        );
    }

    @Test
    void findByEmailNotFoundTest() {
        var res = entityManager.merge(clientTest);
        entityManager.flush();

        var client = clientRepository.getClientByEmail("eppe@gmail.com");

        assertAll(
                () -> assertNotNull(client),
                () -> assertTrue(client.isEmpty())
        );
    }

    @Test
    void saveTest(){
        var client = clientRepository.save(clientTest);

        assertAll(
                () -> assertNotNull(client),
                () -> assertEquals(clientTest.getName(), client.getName()),
                () -> assertEquals(clientTest.getSurname(), client.getSurname()),
                () -> assertEquals(clientTest.getEmail(), client.getEmail()),
                () -> assertEquals(clientTest.getPhone(), client.getPhone()),
                () -> assertEquals(clientTest.getAddress(), client.getAddress()),
                () -> assertEquals(clientTest.getImage(), client.getImage())
        );
    }

    @Test
    void updateTest() {
        var res = entityManager.merge(clientTest);
        entityManager.flush();

        var client = clientRepository.save(Client.builder()
                .id(res.getId())
                .name("Pepe")
                .surname("García")
                .email("juan@gmail.com")
                .phone("123456789")
                .address("Calle Falsa 123")
                .image("https://via.placeholder.com/150")
                .build());

        assertAll(
                () -> assertNotNull(client),
                () -> assertEquals("Pepe", client.getName()),
                () -> assertEquals("García", client.getSurname()),
                () -> assertEquals("juan@gmail.com", client.getEmail()),
                () -> assertEquals("123456789", client.getPhone()),
                () -> assertEquals("Calle Falsa 123", client.getAddress()),
                () -> assertEquals("https://via.placeholder.com/150", client.getImage())
        );
    }


    @Test
    void deleteTest(){
        var res = entityManager.merge(clientTest);
        entityManager.flush();

        clientRepository.delete(res);

        var client = clientRepository.findById(res.getId());

        assertAll(
                () -> assertNotNull(client),
                () -> assertTrue(client.isEmpty())
        );
    }


}
