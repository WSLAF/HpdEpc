package org.example.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionPartNewDto {
    private String catalogCode;

    private String sectionCode;

    private String applicable;

    private String beginDate;

    private String callout;

    private String data;

    private String endDate;

    private String hasImage;

    private String label;

    private String automakerLabel;

    private String qty;

    private String remark;

    private String type;

    private String sourceType;

    private Boolean hasNotes;

    private JSONObject userNote;

    private String pdfLinks;

    private Boolean hasTSB;

    private Boolean hasAccNote;

    private Boolean hasPANT;

    private Boolean isTranslatePart;

    private Boolean hasHistoricalParts;

    private Boolean selectiveFit;

    private String partSequenceNo;

    private String brand;

    private Integer selectiveFitGroupId;

    private String avsCodes;

    private String modelCodes;

    private String revisionRef;

    private String labelLanguage;

//    @TableField(typeHandler = JacksonTypeHandler.class)
//    private String clientGlossaries;
    private JSONArray notes;

    private JSONObject price;

    private JSONArray lookups;

    private String fitmentInstructions;

    private JSONObject clientIdentificationData;

    private JSONArray sections;

    private String basePart;

    private JSONArray notesReferenceKeys;

    private Integer jwfPartMasterNextPartNumber;

    private Boolean openPartPrice;

    private String applicability;

    private Boolean isOpenPartPrice;

    private JSONArray optionIds;
}