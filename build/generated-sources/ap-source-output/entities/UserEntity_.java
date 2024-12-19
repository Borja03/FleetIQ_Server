package entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserEntity.class)
public abstract class UserEntity_ {

	public static volatile SingularAttribute<UserEntity, Integer> zip;
	public static volatile SingularAttribute<UserEntity, String> country;
	public static volatile SingularAttribute<UserEntity, String> password;
	public static volatile ListAttribute<UserEntity, Envio> enviosList;
	public static volatile SingularAttribute<UserEntity, String> city;
	public static volatile SingularAttribute<UserEntity, String> street;
	public static volatile SingularAttribute<UserEntity, String> name;
	public static volatile SingularAttribute<UserEntity, Long> id;
	public static volatile SingularAttribute<UserEntity, UserType> userType;
	public static volatile SingularAttribute<UserEntity, String> email;
	public static volatile SingularAttribute<UserEntity, Boolean> activo;

}

