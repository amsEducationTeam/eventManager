package dao;
import java.util.List;

import domain.Place;

public interface PlaceDao {


	String insert(List<Place> place, int count)throws Exception;
	void findMember()throws Exception;
}
