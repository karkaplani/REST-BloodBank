/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bloodbank.ejb;

import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;
import static bloodbank.utility.MyConstants.PU_NAME;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

/**
 *
 * @author Ottawa
 */
@Singleton
public class BloodDonationService implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<BloodDonation> getAllDonations() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BloodDonation> cq = cb.createQuery(BloodDonation.class);
		cq.select(cq.from(BloodDonation.class));
		return em.createQuery(cq).getResultList();
		// return null;
	}

	public BloodDonation getBloodDonationId(int id) {
		return em.find(BloodDonation.class, id);
		// return null;
	}

	@Transactional
	public BloodDonation persistBloodDonation(BloodDonation newBloodDonation) {
		em.persist(newBloodDonation);
		return newBloodDonation;
		// return null;
	}

	@Transactional
	public BloodDonation updateBloodDonationById(int id, BloodDonation bloodDonationWithUpdates) {
		BloodDonation bloodDonationToBeUpdated = getBloodDonationId(id);
		if (bloodDonationToBeUpdated != null) {
			em.refresh(bloodDonationToBeUpdated);
			em.merge(bloodDonationToBeUpdated);
			em.flush();
		}
		return bloodDonationToBeUpdated;

	}

	@Transactional
	public void deleteBloodDonationById(int id) {
		BloodDonation bloodDonation = getBloodDonationId(id);
		if (bloodDonation != null) {
			em.refresh(bloodDonation);

			em.remove(bloodDonation);
		}

	}
}
