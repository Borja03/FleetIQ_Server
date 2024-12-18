package entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Vehiculo.class)
public abstract class Vehiculo_ {

	public static volatile SingularAttribute<Vehiculo, String> marca;
	public static volatile ListAttribute<Vehiculo, EnvioRutaVehiculo> envioRutaVehiculoList;
	public static volatile SingularAttribute<Vehiculo, String> estado;
	public static volatile SingularAttribute<Vehiculo, String> matricula;
	public static volatile SingularAttribute<Vehiculo, Integer> id;
	public static volatile SingularAttribute<Vehiculo, Float> capacidadCarga;
	public static volatile SingularAttribute<Vehiculo, String> modelo;

}

