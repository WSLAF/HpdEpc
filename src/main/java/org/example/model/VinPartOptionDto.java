package org.example.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VinPartOptionDto {

    private Long id;

    private JSONObject diagnostic;

    private String driveType;

    private String weatherType;

    private String plantCode;

    private String vinTag;

    private JSONArray options;

    private JSONArray excludedOptions;

    private JSONArray uccCodes;

    private String oemRelatedPart;

    private String oemPart;

    private String localPart;

    private String localPartSourceType;

    private String supersessionCodeUS;

    private Integer optionWeight;

    private JSONArray nations;

    private JSONArray excludedNations;

    private Integer optionCount;

    private JSONObject partColor;

    private String otherEBOM;

    private String localModelOptionCode;

    private String sectionNumberDisplay;

    private Boolean isBomPart;

    private JSONArray lookups;

    private JSONArray bomPartSections;

    @JsonProperty("UID")
    private Integer UID;

    private JSONArray includeCountries;

    private JSONArray excludeCountries;

    private Boolean alsoIllustratedInChart;

    private Boolean associatedPart;

    private String baseCode;

    private String beginDate;

    private String calibrationAVSStream;

    private String calibrationCatchCode;

    private String calibrationHeader;

    private String calibrationIdentificationNumber;

    private String calibrationNumber;

    private String calibrationNumberDescription;

    private String calloutRemarkDescription;

    private String decode;

    private String effectiveDateDescription;

    private String endDate;

    private Boolean exportMarketPart;

    private String finisCode;

    private String groupDescription;

    private String groupKey;

    private String groupBasicBeginDate;

    private String groupBasicEndDate;

    private String handType;

    private Boolean hasImage;

    private Boolean hasKit;

    private Boolean hasNote;

    private Boolean hasPartsList;

    private Boolean illustratedInChart;

    private String interpretationCodes;

    private Boolean isExportMarketSection;

    private Boolean isGroup;

    private String lightTruckDescription;

    private Boolean mainPart;

    private String modelCode;

    private String motorCraftNumber;

    private String searchPartId;

    private String partId;

    private String partListId;

    private String partListKey;

    private String partListNumber;

    private String partNameCode;

    private String partTypeCode;

    private String recallNumber;

    private Boolean refersToGroup;

    private Boolean refersToPartsList;

    private String remark;

    private String replacingPartNumber;

    private String restrictionDescription;

    private String sectionId;

    private String sectionPath;

    private String source;

    private String usageDescription;

    private String type;

    private String notAvailable;

    private Boolean treatAsNineties;

    private JSONArray identificationNumbers;

    private Integer componentId;

    private Integer componentSourceId;

    private JSONArray notes;

    private JSONArray sections;

    private JSONArray calloutKeys;

    private Boolean localProduct;

    private JSONArray markets;

    private Boolean sectionApplicability;

    private JSONArray alternatePartList;

    private String catalogCode;

    private String partCode;

    private String description;

    private String quantity;

    private String supersessionCode;

    private String supersessionCodeDescription;

    private String supersessionAbbreviation;

    private String supersessionAbbreviationDescription;

    private String supersessionAbbreviationLexicon;

    private Integer hashcode;

    private String automakerDescription;

    private Boolean applicable;

    private Boolean excluded;

}
