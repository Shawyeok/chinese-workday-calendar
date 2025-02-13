package io.github.shawyeok.chinese.workday;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class RemoteCalendar implements Calendar {

    private static final Logger log = Logger.getLogger(RemoteCalendar.class.getName());

    private volatile Properties cache = new Properties();

    private final URL url;

    private final Lock lock = new ReentrantLock();

    private final AtomicLong fetchRemoteDataTimes = new AtomicLong(0);

    public RemoteCalendar(String remoteUrl) {
        try {
            URL url = new URL(remoteUrl);
            if (!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
                throw new IllegalArgumentException("RemoteCalendar only support HTTP/HTTPS URL: " + url);
            }
            this.url = url;
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid URL: " + remoteUrl, e);
        }
    }

    @Override
    public Boolean isWorkday(LocalDate date) {
        String year = String.valueOf(date.getYear());
        String property = cache.getProperty(year);
        if (property == null) {
            fetchRemoteData();
            property = cache.getProperty(year);
        }
        if (property != null) {
            int dayOfYear = date.getDayOfYear();
            if (property.length() >= dayOfYear) {
                return property.charAt(dayOfYear - 1) == '1';
            }
        }
        return null;
    }

    private void fetchRemoteData() {
        Properties thisCache = cache;
        lock.lock();
        try {
            if (thisCache != cache) {
                return;
            }
            fetchRemoteDataTimes.incrementAndGet();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("User-Agent", "Chinese-Workday-Calendar");
            connection.connect();
            try (InputStream inputStream = connection.getInputStream()) {
                Properties props = new Properties();
                props.load(inputStream);
                cache = props;
            }
        } catch (SocketTimeoutException e) {
            log.log(Level.WARNING, "Fetch remote calendar data timeout: " + url);
        } catch (IOException e) {
            log.log(Level.WARNING, "Fetch remote calendar data failed: " + url, e);
        } finally {
            lock.unlock();
        }
    }

    long getFetchRemoteDataTimes() {
        return fetchRemoteDataTimes.get();
    }
}
