package bloodbank.ejb;

import static bloodbank.entity.SecurityUser.USER_FOR_OWNING_PERSON_QUERY;
import static bloodbank.utility.MyConstants.PARAM1;
import static bloodbank.utility.MyConstants.PU_NAME;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.entity.DonationRecord;
import bloodbank.entity.Person;
import bloodbank.entity.SecurityUser;

@Singleton
public class DonationRecordService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;

	public List<DonationRecord> getAllDonationRecords() {
		// Added the following code
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DonationRecord> cq = cb.createQuery(DonationRecord.class);
		cq.select(cq.from(DonationRecord.class));
		return em.createQuery(cq).getResultList();
		// return null;
	}

	public DonationRecord getDonationRecordId(int id) {
		return em.find(DonationRecord.class, id);
		// return null;
	}

	@Transactional
	public DonationRecord persistDonationRecord(DonationRecord newDonationRecord) {
		em.persist(newDonationRecord);
		return newDonationRecord;
		// return null;
	}

	@Transactional
	public DonationRecord updateDonationRecordById(int id, DonationRecord recordWithUpdates) {
		DonationRecord recordToBeUpdated = getDonationRecordId(id);
		if (recordToBeUpdated != null) {
			em.refresh(recordToBeUpdated);
			recordToBeUpdated.setTested(recordWithUpdates.getTested() == 1);
			recordToBeUpdated.setDonation(recordWithUpdates.getDonation());
			recordToBeUpdated.setOwner(recordWithUpdates.getOwner());
			em.merge(recordToBeUpdated);
			em.flush();
		}
		return recordToBeUpdated;
	}
	
	@Transactional
    public void deleteDonationRecordById(int id) {
        DonationRecord donationRecord = getDonationRecordId(id);
        if (donationRecord != null) {
            em.refresh(donationRecord);
            em.remove(donationRecord);
        }
    }
	
}
