package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-08-02T07:20:16.585-0700")
@StaticMetamodel(BloodBank.class)
public class BloodBank_ {
	public static volatile SingularAttribute<BloodBank, String> name;
	public static volatile SetAttribute<BloodBank, BloodDonation> donations;
}
