package dao;

import java.util.List;

import domain.Events;

public interface EventsDao {

	List<Events> findAll() throws Exception;
	List<Events> findToday() throws Exception;
	Events findById(Integer id) throws Exception;
	void insert(Events events) throws Exception;
	void update(Events events) throws Exception;
	void delete(Events events) throws Exception;
	List<Events> findAll(int id) throws Exception;
	List<Events> findToday(int id) throws Exception;
	List<Events> findfive(List<Events> eventsList, int id) throws Exception;
	double countAll() throws Exception;
	double countAllToday() throws Exception;

}
