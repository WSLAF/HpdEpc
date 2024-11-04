package com.domain;

import lombok.Data;

@Data
public class VinSectionOption2 {
    private String driveType;

    private String weatherType;

    private String beginDate;

    private String endDate;

    //将string转为数组
    private String uccCodes;

    private String optionCodes;

    private String excludedOptionCodes;

    private String nationCodes;

    private String excludedNationCodes;

    private String catalogCode;

    private String sectionNumberDisplay;

    private String pageDisplay;

    private String indexLevel;

    private String sectionCode;

    private String sectionId;

    private String parentInterpretationStream;

    private String topInterpretationStream;

    private String illustrations;

    private Byte anyParts;

    private String sections;

    private String sectionClientData;

}