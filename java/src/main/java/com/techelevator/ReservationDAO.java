package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	public void bookReservation(long siteId, LocalDate to, LocalDate from, String name);
	public void confirmationId (long reservationId);
	public List<Reservation> getAllReservations(long campId);
	
	
}
