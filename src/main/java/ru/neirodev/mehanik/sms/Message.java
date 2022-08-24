/*
 * Copyright MinuSoft (c) 2022.
 */
package ru.neirodev.mehanik.sms;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Minu <<a href=minu-moto@mail.ru>minu-moto@mail.ru</a>>
 * @since 27.03.2022 22:29:11
 */
@Data
@AllArgsConstructor
public class Message {

	private String phone;
	private String message;

}