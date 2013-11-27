package com.espmail.util;

import com.espmail.message.ESPMailMessage;

import java.util.List;

/**
 * ESPMail
 * User: Luis
 */
public interface Translator {
   public ESPMailMessage translate(StringBuffer bf, String[] valores)throws TranslatorException;
   public StringBuffer returnBads(ESPMailMessage messages);
   public void badsProcess(List malos) throws TranslatorException;
   
}
