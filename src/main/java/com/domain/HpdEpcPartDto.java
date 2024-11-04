package com.domain;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class HpdEpcPartDto {
    private String id;

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

    @JsonIgnore
    private String userNote2;

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

    @JsonIgnore
    private String notes2;

    private JSONArray notes;

    private JSONObject price;

    @JsonIgnore
    private String price2;

    private JSONArray lookups;

    @JsonIgnore
    private String lookups2;

    private String fitmentInstructions;

    @JsonIgnore
    private String clientIdentificationData2;

    private JSONObject clientIdentificationData;

    @JsonIgnore
    private String sections2;

    private JSONArray sections;

    private String basePart;

    @JsonIgnore
    private String notesReferenceKeys2;

    private JSONArray notesReferenceKeys;

    private Integer jwfPartMasterNextPartNumber;

    private Boolean openPartPrice;

    private String applicability;

    private List<ModelPartOption> modelPartOptionList;

}
