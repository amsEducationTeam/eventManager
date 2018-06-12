package dao;
import java.util.List;

import domain.Place;

public interface PlaceDao {

	void insert(List<Place> place, int count)throws Exception;
	void findMember()throws Exception;
}
