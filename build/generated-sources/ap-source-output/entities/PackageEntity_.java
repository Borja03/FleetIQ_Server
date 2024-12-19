package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PackageEntity.class)
public abstract class PackageEntity_ {

	public static volatile SingularAttribute<PackageEntity, Envio> envio;
	public static volatile SingularAttribute<PackageEntity, String> receiver;
	public static volatile SingularAttribute<PackageEntity, PackageSize> size;
	public static volatile SingularAttribute<PackageEntity, String> sender;
	public static volatile SingularAttribute<PackageEntity, Double> weight;
	public static volatile SingularAttribute<PackageEntity, Long> id;
	public static volatile SingularAttribute<PackageEntity, Boolean> fragile;
	public static volatile SingularAttribute<PackageEntity, Date> creationDate;

}

