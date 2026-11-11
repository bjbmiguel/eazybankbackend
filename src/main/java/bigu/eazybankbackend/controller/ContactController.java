package bigu.eazybankbackend.controller;


import bigu.eazybankbackend.model.Contact;
import bigu.eazybankbackend.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactRepository contactRepository;

    @PostMapping("/register")
   /*


    //Filter target must be a collection, array, map or stream type
    @PreFilter("filterObject.contactName != 'Test'") //Se for diferente executado, caso contrário, filtra..
    public Contact saveContactInquiryDetails(@RequestBody List<Contact> contacts) {
        if (contacts.isEmpty()) return null;
        var contact = contacts.getFirst();
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        return contactRepository.save(contact);
    }
  */
    @PostFilter("filterObject.contactName != 'Test'")
    public List<Contact> saveContactInquiryDetails(@RequestBody List<Contact> contacts) {
        if (contacts.isEmpty()) return null;
        List<Contact> contactsReturned = new ArrayList<>();
        var contact = contacts.getFirst();
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        contactsReturned.add(contactRepository.save(contact));
        return contactsReturned;
    }

    public String getServiceReqNumber() {
        Random random = new Random();
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return "SR" + ranNum;
    }
}
