package com.feelt.fleet.util;

import java.time.LocalDate;
import java.util.UUID;

public final class CodeGenerator {

    private CodeGenerator() {
    }

    public static String dailyCode(String prefix) {
        return prefix + "-" + LocalDate.now() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
