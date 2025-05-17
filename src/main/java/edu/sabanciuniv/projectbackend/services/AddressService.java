package edu.sabanciuniv.projectbackend.services;
import org.springframework.transaction.annotation.Transactional;
import edu.sabanciuniv.projectbackend.models.Address;
import edu.sabanciuniv.projectbackend.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(String addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    @Transactional
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(String addressId) {
        addressRepository.deleteById(addressId);
    }
}
