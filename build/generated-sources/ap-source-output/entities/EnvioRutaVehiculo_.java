package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EnvioRutaVehiculo.class)
public abstract class EnvioRutaVehiculo_ {

	public static volatile SingularAttribute<EnvioRutaVehiculo, Envio> envio;
	public static volatile SingularAttribute<EnvioRutaVehiculo, Date> fechaAsignacion;
	public static volatile SingularAttribute<EnvioRutaVehiculo, Ruta> ruta;
	public static volatile SingularAttribute<EnvioRutaVehiculo, Integer> id;
	public static volatile SingularAttribute<EnvioRutaVehiculo, Vehiculo> vehiculo;

}

