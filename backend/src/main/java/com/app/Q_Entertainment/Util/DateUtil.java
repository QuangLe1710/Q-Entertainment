package com.app.Q_Entertainment.Util;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@AllArgsConstructor
public class DateUtil {

    public static Date generateExpirationRefreshToken(Long timeToLive){
        Date now = new Date();
        return new Date(now.getTime() + timeToLive);
    }
}
