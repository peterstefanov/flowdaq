package com.flowdaq.app.service.date;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class DateServiceImpl implements DateService {

	private final DateTimeZone timeZone;

	public DateServiceImpl(final DateTimeZone timeZone) {
		super();
		this.timeZone = checkNotNull(timeZone);
		TimeZone.setDefault(timeZone.toTimeZone());
		DateTimeZone.setDefault(timeZone);
	}

	@Override
	public DateTime now() {
		return DateTime.now(timeZone);
	}
}
