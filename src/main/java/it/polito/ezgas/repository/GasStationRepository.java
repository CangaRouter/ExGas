package it.polito.ezgas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.polito.ezgas.entity.GasStation;

@Repository
public interface GasStationRepository extends JpaRepository<GasStation, Integer> {

	GasStation findBygasStationName(String name);

	Iterable<GasStation> findBylatBetweenAndLonBetween(double lat1, double lat2, double lon1, double lon2);

	// non funzionante, da correggere
	/*
	 * @Query("Select G from GasStation G where G.?1=True") Iterable<GasStation>
	 * findByGasolineType(String fuel);
	 */

	// da aggiungere clausola per carburante
	@Query("Select G from GasStation G where   G.carSharing= ?2 and G.lat>?3 and G.lat<?4 and G.lon>?5 and G.lon<?6 ")
	Iterable<GasStation> findWithCoordinates(String fuel, String carSharing, double lat1, double lat2, double lon1,
			double lon2);

	// da aggiungere clausola per carburante
	@Query("Select G from GasStation G where  G.carSharing= ?2 ")
	Iterable<GasStation> findWithoutCoordinates(String fuel, String carSharing);

	Iterable<GasStation> findByCarSharing(String carSharing);

}
