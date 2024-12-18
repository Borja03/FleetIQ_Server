package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Envio.class)
public abstract class Envio_ {

	public static volatile SingularAttribute<Envio, String> descripcion;
	public static volatile SingularAttribute<Envio, String> direccionEntrega;
	public static volatile SingularAttribute<Envio, String> estado;
	public static volatile SingularAttribute<Envio, Date> fechaEnvio;
	public static volatile SingularAttribute<Envio, Date> fechaEntrega;
	public static volatile SingularAttribute<Envio, Integer> id;
	public static volatile SingularAttribute<Envio, String> destinatario;
	public static volatile SingularAttribute<Envio, EnvioRutaVehiculo> envioRutaVehiculo;

}

