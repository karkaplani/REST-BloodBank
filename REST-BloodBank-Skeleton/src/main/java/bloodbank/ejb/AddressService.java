/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bloodbank.ejb;

import bloodbank.entity.Address;
import static bloodbank.entity.Contact_.address;
import bloodbank.entity.Person;
import bloodbank.entity.SecurityUser;
import static bloodbank.entity.SecurityUser.USER_FOR_OWNING_PERSON_QUERY;
import static bloodbank.utility.MyConstants.PARAM1;
import static bloodbank.utility.MyConstants.PU_NAME;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Ottawa
 */
 @Singleton
public class AddressService implements Serializable {
    /**
     * 
     */ 
    private static final Logger LOG = LogManager.getLogger();
    
    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;
    
    public List<Address> getAllAddresses() {
    	//Added the following code
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Address> cq = cb.createQuery(Address.class);
    	cq.select(cq.from(Address.class));
    	return em.createQuery(cq).getResultList();
    	//return null;
    }
    public Address getAddressId(int id) {
    	return em.find(Address.class, id);
    	//return null;
    }
     @Transactional
    public Address persistAddress(Address newAddress) {
    	em.persist(newAddress);
    	return newAddress;
    	// return null;
    }
     @Transactional
    public Address updateAddressById(int id, Address addressWithUpdates) {
        Address addressToBeUpdated = getAddressId(id);
        if (addressToBeUpdated != null) {
            em.refresh(addressToBeUpdated);
            em.merge(addressWithUpdates);
            em.flush();
        }
        return addressToBeUpdated;
    }
    @Transactional
    public void deleteAddressById(int id) {
        Address address = getAddressId(id);
        if (address != null) {
            em.refresh(address);
            TypedQuery<SecurityUser> findUser = em
                .createNamedQuery(USER_FOR_OWNING_PERSON_QUERY, SecurityUser.class)
                .setParameter(PARAM1, address.getId());
            SecurityUser sUser = findUser.getSingleResult();
            em.remove(sUser);
            em.remove(address);
        }
    }
    
}
