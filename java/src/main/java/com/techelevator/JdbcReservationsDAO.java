package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcReservationsDAO implements ReservationDAO{

	private JdbcTemplate jdbcTemplate;
	
	public JdbcReservationsDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private Reservation addRowToReservation(SqlRowSet sqlRowSet) {
		Reservation newRes = new Reservation();
		newRes.setCreateDate(sqlRowSet.getDate("create_date").toLocalDate());
		newRes.setFromDate(sqlRowSet.getDate("from_date").toLocalDate());
		newRes.setToDate(sqlRowSet.getDate("to_date").toLocalDate());
		newRes.setName(sqlRowSet.getString("name"));
		newRes.setReservationId(sqlRowSet.getLong("reservation_id"));
		newRes.setSiteId(sqlRowSet.getLong("site_id"));
		return newRes;		
	}

	@Override
	public List<Reservation> getAllReservations(long campId) {
		List<Reservation> reservations = new ArrayList<>();
		String sqlReserve = ("SELECT s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities, c.daily_fee FROM reservation r JOIN site s ON r.site_id = s.site_id JOIN campground c ON s.campground_id = c.campground_id WHERE c.campground_id = ?");
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlReserve, campId);
		while (sqlRowSet.next()) {
			Reservation res = addRowToReservation(sqlRowSet);
			reservations.add(res);
		}	
		return reservations;
	}
	
	@Override
	public void bookReservation(long siteId, LocalDate to, LocalDate from, String name) {
		String bookRes = "INSERT INTO reservation (site_id, name, from_date, to_date) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(bookRes, siteId, name, from, to);		

	}

	@Override
	public void confirmationId(long reservationId) {
		List<Reservation> confirm = new ArrayList<>();
		String confirmId = "SELECT * FROM reservation WHERE reservation_id = ?";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(confirmId, reservationId);
		while (sqlRowSet.next()) {
			Reservation con = addRowToReservation(sqlRowSet);
			confirm.add(con);
		}
		System.out.println(confirm.get(0));
	}


}
