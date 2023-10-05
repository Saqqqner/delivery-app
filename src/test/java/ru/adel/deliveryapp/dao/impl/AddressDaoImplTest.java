package ru.adel.deliveryapp.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.entity.Address;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class AddressDaoImplTest {
    @Container
    private  final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("scripts.sql");

    private SessionManager sessionManager;
    private AddressDaoImpl addressDao;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        postgresContainer.start();
        postgresContainer.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger(AddressDaoImplTest.class)));

        System.setProperty("jdbc.url", postgresContainer.getJdbcUrl());
        System.setProperty("jdbc.username", postgresContainer.getUsername());
        System.setProperty("jdbc.password", postgresContainer.getPassword());

        CustomDataSourceConfig.updateDataSourceProperties();

        dataSource = CustomDataSourceConfig.getHikariDataSource();
        sessionManager = new SessionManagerJdbc(dataSource);
        addressDao = new AddressDaoImpl(sessionManager);
    }

    @AfterEach
    void tearDown() {
        postgresContainer.stop();
    }


    @Test
    void findById_shouldReturnAddress() throws SQLException {
        Long addressId = saveTestAddress("TestCity", "TestStreet", "1", "1");
        Optional<Address> addressById = addressDao.findById(addressId);
        Assertions.assertTrue(addressById.isPresent());
        Assertions.assertEquals("TestCity", addressById.get().getCity());
        Assertions.assertEquals("TestStreet", addressById.get().getStreet());
        Assertions.assertEquals("1", addressById.get().getHouseNumber());
        Assertions.assertEquals("1", addressById.get().getApartmentNumber());
    }

    @Test
    void findById_shouldReturnEmptyOptionalForNonExistingAddress() throws SQLException {
        Optional<Address> result = addressDao.findById(-1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_shouldReturnTrueForExistingAddress() throws SQLException {
        Long addressId = saveTestAddress("TestCity", "TestStreet", "1", "1");
        boolean result = addressDao.deleteById(addressId);
        Assertions.assertTrue(result);
    }

    @Test
    void deleteById_shouldReturnFalseForNonExistingAddress() throws SQLException {
        // Act
        Assertions.assertThrows(SQLException.class, () -> addressDao.deleteById(-1L));
    }

    @Test
    void findAll_shouldReturnListOfAddress() throws SQLException {
        saveTestAddress("TestCity", "TestStreet", "1", "1");
        saveTestAddress("TestCity1", "TestStreet1", "1", "1");
        List<Address> addresses = addressDao.findAll();
        Assertions.assertEquals(2, addresses.size());
        Assertions.assertEquals("TestCity", addresses.get(0).getCity());
        Assertions.assertEquals("TestCity1", addresses.get(1).getCity());
    }


    @Test
    void save_shouldSaveAddress() throws SQLException {
        Address address = new Address();
        address.setCity("TestCity");
        address.setStreet("TestStreet");
        address.setHouseNumber("1");
        address.setApartmentNumber("1");
        Long addressId = addressDao.save(address);
        Optional<Address> addressById = addressDao.findById(addressId);
        Assertions.assertTrue(addressById.isPresent());
        Assertions.assertEquals(1L, addressById.get().getId());
        Assertions.assertEquals("TestCity", addressById.get().getCity());
        Assertions.assertEquals("TestStreet", addressById.get().getStreet());
        Assertions.assertEquals("1", addressById.get().getHouseNumber());
        Assertions.assertEquals("1", addressById.get().getApartmentNumber());
    }

    @Test
    void update_shouldUpdateAddressDetails() throws SQLException {
        Long addressId = saveTestAddress("TestCity", "TestStreet", "1", "1");
        Address updatedAddress = new Address();
        updatedAddress.setId(addressId);
        updatedAddress.setCity("TestCity1");
        updatedAddress.setStreet("TestStreet1");
        updatedAddress.setHouseNumber("2");
        updatedAddress.setHouseNumber("2");
        addressDao.update(updatedAddress);
        Optional<Address> addressById = addressDao.findById(addressId);
        Assertions.assertTrue(addressById.isPresent());
        Assertions.assertEquals(1L, addressById.get().getId());
        Assertions.assertEquals(updatedAddress.getCity(), addressById.get().getCity());
        Assertions.assertEquals(updatedAddress.getStreet(), addressById.get().getStreet());
        Assertions.assertEquals(updatedAddress.getHouseNumber(), addressById.get().getHouseNumber());
        Assertions.assertEquals(updatedAddress.getApartmentNumber(), addressById.get().getApartmentNumber());
    }

    private Long saveTestAddress(String city, String street, String houseNumber, String apartmentNumber) throws SQLException {
        Address address = new Address();
        address.setCity(city);
        address.setStreet(street);
        address.setHouseNumber(houseNumber);
        address.setApartmentNumber(apartmentNumber);
        return addressDao.save(address);
    }
}
