package entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ruta.class)
public abstract class Ruta_ {

	public static volatile SingularAttribute<Ruta, String> descripcion;
	public static volatile ListAttribute<Ruta, EnvioRutaVehiculo> envioRutaVehiculoList;
	public static volatile SingularAttribute<Ruta, Double> distancia;
	public static volatile SingularAttribute<Ruta, Integer> id;
	public static volatile SingularAttribute<Ruta, String> nombre;
	public static volatile SingularAttribute<Ruta, Double> duracionEstimada;

}

