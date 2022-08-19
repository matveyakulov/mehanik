/*
 * Copyright MinuSoft (c) 2022.
 */
package ru.neirodev.mehanik.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Minu <<a href=minu-moto@mail.ru>minu-moto@mail.ru</a>>
 * @since 27.03.2022 21:02:18
 */
public class PropUtil {

	private static Logger logger = LoggerFactory.getLogger(PropUtil.class);
	private static Properties systemProps;
	private static Properties props;

	public static synchronized Properties getSystemProps() {
		if (systemProps == null) {
			String catalinaHome = System.getProperty("catalina.base");
			File pFile = new File((catalinaHome != null) ? catalinaHome + "/conf/sms.properties" : "../sms.properties");
			if (pFile.exists())
				try (InputStream is = new FileInputStream(pFile)) {
					systemProps = new Properties();
					systemProps.load(is);
				} catch (IOException e) {
					logger.error("", e);
				}
		}
		return systemProps;
	}

	public static synchronized Properties getProps() {
		if (props == null)
			try (InputStream is = PropUtil.class.getResourceAsStream("/core.properties")) {
				props = new Properties(getSystemProps());
				props.load(is);
			} catch (IOException e) {
				logger.error("Файл настроек приложения не найден", e);
			}
		return props;
	}

	public static String getProp(String key) {
		return getProps().getProperty(key);
	}

	public static boolean getBooleanProp(String key) {
		return "true".equalsIgnoreCase(getProp(key));
	}

	private static Integer getIntProp(String key) {
		Integer ret = null;
		try {
			String val = getProp(key);
			if ((val != null) && !val.isEmpty())
				ret = Integer.valueOf(val);
		} catch (Exception e) {
			logger.error("Произошла ошибка при получении параметров приложения", e);
		}
		return ret;
	}

	public static String getSmppHost() {
		return getProp("smpp.host.name");
	}

	public static Integer getSmppPort() {
		return getIntProp("smpp.host.port");
	}

	public static String getSmppUser() {
		return getProp("smpp.auth.user");
	}

	public static String getSmppPassword() {
		return getProp("smpp.auth.password");
	}

	public static String getSmsFromAddr() {
		return getProp("smpp.addr.from");
	}

	public static String getApiSecret() {
		return getProp("api.secret");
	}

}