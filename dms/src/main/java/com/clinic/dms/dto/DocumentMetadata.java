package com.clinic.dms.dto;

public record DocumentMetadata(String key, String filename, Long size, String lastModified) {
}
